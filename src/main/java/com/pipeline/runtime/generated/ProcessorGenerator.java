package com.pipeline.runtime.generated;

import com.pipeline.definition.Pipeline;
import com.pipeline.runtime.Processor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class ProcessorGenerator {
    private static final DynamicClassLoader pipelineRuntimeLoader =
            new DynamicClassLoader(Thread.currentThread().getContextClassLoader());

    public Processor getProcessor(Pipeline pipeline) {
        Class<?> processorClass = pipelineRuntimeLoader.loadClass(createProcessorBytecode(pipeline));
        try {
            return (Processor) processorClass.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private byte[] createProcessorBytecode(Pipeline pipeline) {
        ClassNode classNode = createProcessorClassDefinition();

        classNode.methods.add(createDefaultConstructor());

        Method method;
        try {
            method = Processor.class.getMethod("run", Map.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

        MethodNode runMethod = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, "run", Type.getMethodDescriptor(method), null, null);
        runMethod.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        runMethod.instructions.add(new LdcInsnNode("I'm running!"));
        runMethod.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"));
        runMethod.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
        runMethod.instructions.add(new InsnNode(Opcodes.ARETURN));
        classNode.methods.add(runMethod);

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);
        return cw.toByteArray();
    }

    private MethodNode createDefaultConstructor() {
        MethodNode defaultConstructor = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        defaultConstructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        defaultConstructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V"));
        defaultConstructor.instructions.add(new InsnNode(Opcodes.RETURN));
        return defaultConstructor;
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
