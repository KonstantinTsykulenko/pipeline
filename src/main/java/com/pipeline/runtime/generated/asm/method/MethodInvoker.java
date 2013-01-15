package com.pipeline.runtime.generated.asm.method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class MethodInvoker extends AbstractComplexMethodBodyGenerator {

    private Class<?> ownerClass;
    private Method methodToInvoke;

    public MethodInvoker(List<? extends MethodBodyGenerator> methodBodyGenerators, Class<?> ownerClass, Method methodToInvoke) {
        super(methodBodyGenerators);
        this.ownerClass = ownerClass;
        this.methodToInvoke = methodToInvoke;
    }

    public MethodInvoker(MethodBodyGenerator methodBodyGenerator, Class<?> ownerClass, Method methodToInvoke) {
        super(methodBodyGenerator);
        this.ownerClass = ownerClass;
        this.methodToInvoke = methodToInvoke;
    }

    public MethodInvoker(Class<?> clazz, Method method) {
        this((MethodBodyGenerator) null, clazz, method);
    }

    @Override
    protected void preGenerate(ClassNode classNode, MethodNode methodNode) {
        // load implicit "this" reference
        methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
    }

    @Override
    protected void postGenerate(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(ownerClass),
                methodToInvoke.getName(),
                Type.getMethodDescriptor(methodToInvoke)));
    }
}
