package com.pipeline.runtime.generated;

import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.ActionArgumentBinder;
import com.pipeline.runtime.ActionArgumentBinding;
import com.pipeline.runtime.ExecutionContext;
import com.pipeline.runtime.Processor;
import com.pipeline.util.AnnotationUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.wrapper.ClassBuilder;
import org.objectweb.asm.wrapper.FieldBuilder;
import org.objectweb.asm.wrapper.InstructionBuilder;
import org.objectweb.asm.wrapper.InvocationBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.objectweb.asm.wrapper.CastBuilder.cast;
import static org.objectweb.asm.wrapper.ClassBuilder._class;
import static org.objectweb.asm.wrapper.ConstLoader._c;
import static org.objectweb.asm.wrapper.FieldBuilder._field;
import static org.objectweb.asm.wrapper.FieldLoader._f;
import static org.objectweb.asm.wrapper.Initializer._new;
import static org.objectweb.asm.wrapper.InvocationBuilder.call;
import static org.objectweb.asm.wrapper.LocalVariableLoader._v;
import static org.objectweb.asm.wrapper.MethodBuilder._method;
import static org.objectweb.asm.wrapper.ReturnBuilder._returnNull;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class ProcessorGenerator {
    private static final DynamicClassLoader pipelineRuntimeLoader =
            new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
    private static final AtomicInteger definedProcessors = new AtomicInteger(0);

    private ThreadLocal<Map<String, Node>> nodeToFieldMapping = new ThreadLocal<Map<String, Node>>();

    public Processor getProcessor(Pipeline pipeline) {
        byte[] processorBytecode = createProcessorBytecode(pipeline);
        try {
            OutputStream outputStream = new FileOutputStream("carp.class");
            outputStream.write(processorBytecode);
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Class<?> processorClass = pipelineRuntimeLoader.loadClass(processorBytecode);
        try {
            Processor processor = (Processor) processorClass.newInstance();
            injectDependencies(processor, nodeToFieldMapping.get());
            return processor;
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private void injectDependencies(Processor processor, Map<String, Node> nodeToFieldMapping) {
        for (Field field : processor.getClass().getDeclaredFields()) {
            try {
                Node node = nodeToFieldMapping.get(field.getName());
                if (node != null) {
                    field.set(processor, node.getAction());
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private byte[] createProcessorBytecode(Pipeline pipeline) {
        String processorName = getProcessorName();
        FieldBuilder contextField = _field(ExecutionContext.class, GeneratorConstants.CONTEXT_FIELD).isPublic();
        ClassBuilder classBuilder = _class(processorName).implementing(Processor.class).
                field(contextField);

        mapFields(pipeline);

        classBuilder.fields(createNodeFields());

        InstructionBuilder contextInitializer = _new(ExecutionContext.class, contextField, processorName);

        InvocationBuilder contextPopulator =
                call(GeneratorConstants.EXECUTION_CONTEXT_PUT_ALL).on(_f(contextField, processorName)).params(
                        _v(1)
                );

        List<InstructionBuilder> instructionBuilders = buildNodeInvocations(processorName, contextField);
        instructionBuilders.add(0, contextInitializer);
        instructionBuilders.add(1, contextPopulator);
        instructionBuilders.add(_returnNull());


        classBuilder.method(_method(GeneratorConstants.PROCESSOR_RUN).$(
                instructionBuilders
        ));

        return classBuilder.build();
    }

    private List<InstructionBuilder> buildNodeInvocations(String processorName, FieldBuilder contextField) {
        List<InstructionBuilder> instructions = new LinkedList<InstructionBuilder>();

        for (Map.Entry<String, Node> nodeMapEntry : nodeToFieldMapping.get().entrySet()) {

            Class<?> actionClass = nodeMapEntry.getValue().getAction().getClass();

            Method methodToInvoke = AnnotationUtils.getHanlderMethod(actionClass);

            String fieldName = nodeMapEntry.getKey();

            List<ActionArgumentBinding> actionArgumentBindings =
                    getActionArgumentBinder().createActionArgumentBindings(methodToInvoke);

            List<InstructionBuilder> params = new ArrayList<InstructionBuilder>(actionArgumentBindings.size() + 1);

            for (ActionArgumentBinding argumentBinding : actionArgumentBindings) {
                InstructionBuilder builder =
                        call(GeneratorConstants.EXECUTION_CONTEXT_GET).on(
                                _f(contextField, processorName)).params(
                                _c(argumentBinding.getArgumentName()),
                                _c(Type.getType(argumentBinding.getArgumentType())
                                ));
                params.add(builder);

                params.add(cast(argumentBinding.getArgumentType()));
            }

            instructions.addAll(params);

            instructions.add(
                call(methodToInvoke).on(_f(_field(actionClass, fieldName), processorName)).params(params)
            );
        }

        return instructions;
    }

    private List<FieldBuilder> createNodeFields() {
        List<FieldBuilder> fieldBuilders = new ArrayList<FieldBuilder>(nodeToFieldMapping.get().size());

        for (Map.Entry<String, Node> nodeMapEntry : nodeToFieldMapping.get().entrySet()) {
            fieldBuilders.add(_field(nodeMapEntry.getValue().getAction().getClass(), nodeMapEntry.getKey()).isPublic());
        }

        return fieldBuilders;
    }

    private void mapFields(Pipeline pipeline) {
        Iterator<String> fieldNameGenerator = new FieldNameGenerator().iterator();

        Map<String, Node> nodeToFieldMapping = new HashMap<String, Node>(pipeline.getNodes().size());

        for (Node node : pipeline.getNodes()) {
            String name = fieldNameGenerator.next();
            nodeToFieldMapping.put(name, node);
        }

        this.nodeToFieldMapping.set(nodeToFieldMapping);
    }

    private ActionArgumentBinder getActionArgumentBinder() {
        return new ActionArgumentBinder();
    }

//    private void initContext(ClassNode classNode, MethodNode defaultConstructor) {
//        defaultConstructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
//        // stack: this
//        defaultConstructor.instructions.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(ExecutionContext.class)));
//        // stack: this :: <ExecutionContext>
//        defaultConstructor.instructions.add(new InsnNode(Opcodes.DUP));
//        // stack: this :: <ExecutionContext> :: <ExecutionContext>
//        defaultConstructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(ExecutionContext.class), "<init>", "()V"));
//        // stack: this :: <ExecutionContext>
//        defaultConstructor.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, GeneratorConstants.CONTEXT_FIELD, Type.getDescriptor(ExecutionContext.class)));
//    }

    private String getProcessorName() {
        return Processor.class.getCanonicalName() + definedProcessors.incrementAndGet();
    }
}
