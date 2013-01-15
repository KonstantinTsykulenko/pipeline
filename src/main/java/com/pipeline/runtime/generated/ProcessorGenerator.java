package com.pipeline.runtime.generated;

import com.pipeline.annotation.ContextAttribute;
import com.pipeline.definition.Node;
import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.ActionArgumentBinder;
import com.pipeline.runtime.ActionArgumentBinding;
import com.pipeline.runtime.Processor;
import com.pipeline.runtime.generated.asm.method.FieldLoader;
import com.pipeline.runtime.generated.asm.method.LocalVariableLoader;
import com.pipeline.runtime.generated.asm.method.MethodBodyGenerator;
import com.pipeline.runtime.generated.asm.method.MethodInvoker;
import com.pipeline.runtime.reflection.ExecutionContext;
import com.pipeline.util.AnnotationUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class ProcessorGenerator {
    private static final DynamicClassLoader pipelineRuntimeLoader =
            new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
    public static final String CONTEXT_FIELD = "executionContext";
    public static final String RUN_METHOD_NAME = "run";

    private ThreadLocal<Map<String, Node>> nodeToFieldMapping = new ThreadLocal<Map<String, Node>>();

    public Processor getProcessor(Pipeline pipeline) {
        Class<?> processorClass = pipelineRuntimeLoader.loadClass(createProcessorBytecode(pipeline));
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
        for (Field field : processor.getClass().getFields()) {
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
        ClassNode classNode = createProcessorClassDefinition();

        classNode.fields.add(createContextField());

        mapFields(pipeline);

        createNodeFields(classNode);

        classNode.methods.add(createDefaultConstructor(classNode));

        Method method;
        try {
            method = Processor.class.getMethod(RUN_METHOD_NAME, Map.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        classNode.methods.add(createRunMethod(classNode, method));

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    private void createNodeFields(ClassNode classNode) {
        for (Map.Entry<String, Node> nodeMapEntry : nodeToFieldMapping.get().entrySet()) {
            classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,
                    nodeMapEntry.getKey(),
                    Type.getDescriptor(nodeMapEntry.getValue().getAction().getClass()),
                    null,
                    null));
        }
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

    private MethodNode createRunMethod(ClassNode classNode, Method method) {
        MethodNode runMethod = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, RUN_METHOD_NAME, Type.getMethodDescriptor(method), null, null);

        createInitialContextParameterBinding(classNode, runMethod);

        createNodeInvocations(classNode, runMethod);

        runMethod.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
        runMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
        return runMethod;
    }

    private void createInitialContextParameterBinding(ClassNode classNode, MethodNode runMethod) {
        Method method;
        try {
            method = ExecutionContext.class.getMethod("putAll", Map.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        MethodBodyGenerator methodBodyGenerator = new MethodInvoker(
                Arrays.asList(
                        new FieldLoader(CONTEXT_FIELD, ExecutionContext.class),
                        new LocalVariableLoader(1)),
                ExecutionContext.class,
                method);
        methodBodyGenerator.generateMethodBody(classNode, runMethod);
    }

    private void createNodeInvocations(ClassNode classNode, MethodNode methodNode) {
        for (Map.Entry<String, Node> nodeMapEntry : nodeToFieldMapping.get().entrySet()) {

            Class<?> actionClass = nodeMapEntry.getValue().getAction().getClass();

            Method methodToInvoke = AnnotationUtils.getHanlderMethod(actionClass);

            String fieldName = nodeMapEntry.getKey();

            MethodBodyGenerator methodBodyGenerator =
                    new MethodInvoker(new FieldLoader(fieldName, actionClass), actionClass, methodToInvoke);
            methodBodyGenerator.generateMethodBody(classNode, methodNode);

            List<ActionArgumentBinding> actionArgumentBindings =
                    getActionArgumentBinder().createActionArgumentBindings(methodToInvoke);

        }
    }

    private ActionArgumentBinder getActionArgumentBinder() {
        return new ActionArgumentBinder();
    }

    private FieldNode createContextField() {
        return new FieldNode(Opcodes.ACC_PUBLIC, CONTEXT_FIELD, Type.getDescriptor(ExecutionContext.class), null, null);
    }

    private MethodNode createDefaultConstructor(ClassNode classNode) {
        MethodNode defaultConstructor = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

        defaultConstructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        defaultConstructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));

        initContext(classNode, defaultConstructor);

        defaultConstructor.instructions.add(new InsnNode(Opcodes.RETURN));
        return defaultConstructor;
    }

    private void initContext(ClassNode classNode, MethodNode defaultConstructor) {
        defaultConstructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        // stack: this
        defaultConstructor.instructions.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(ExecutionContext.class)));
        // stack: this :: <ExecutionContext>
        defaultConstructor.instructions.add(new InsnNode(Opcodes.DUP));
        // stack: this :: <ExecutionContext> :: <ExecutionContext>
        defaultConstructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(ExecutionContext.class), "<init>", "()V"));
        // stack: this :: <ExecutionContext>
        defaultConstructor.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, classNode.name, CONTEXT_FIELD, Type.getDescriptor(ExecutionContext.class)));
    }

    private ClassNode createProcessorClassDefinition() {
        ClassNode classNode = new ClassNode(Opcodes.ASM4);//ASM API version
        classNode.version = Opcodes.V1_6;//JRE 1.6+
        classNode.access = Opcodes.ACC_PUBLIC;
        classNode.interfaces = Arrays.asList(new String[]{"com/pipeline/runtime/Processor"});
        classNode.superName = "java/lang/Object";
        classNode.name = "com/pipeline/runtime/ProcessorImpl";
        return classNode;
    }
}
