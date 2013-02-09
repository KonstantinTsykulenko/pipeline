package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class InvocationBuilder extends ComplexInstructionBuilder {

    private Method methodToInvoke;
    private Invokable invocationTarget;

    private InvocationBuilder() { }

    @Override
    protected List<AbstractInsnNode> preBuild() {
        List<AbstractInsnNode> result = new LinkedList<AbstractInsnNode>();

        // load implicit "this" reference
        result.add(new VarInsnNode(Opcodes.ALOAD, 0));
        //load a reference to invoke method on
        result.addAll(invocationTarget.build());

        return result;
    }

    @Override
    protected List<AbstractInsnNode> postBuild() {
        return Arrays.<AbstractInsnNode>asList(
                new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                Type.getInternalName(methodToInvoke.getDeclaringClass()),
                methodToInvoke.getName(),
                Type.getMethodDescriptor(methodToInvoke)));
    }

    public static InvocationBuilder call(Method method) {
        InvocationBuilder invocationBuilder = new InvocationBuilder();
        invocationBuilder.methodToInvoke = method;
        return  invocationBuilder;
    }

    public InvocationBuilder on(Invokable target) {
        this.invocationTarget = target;
        return this;
    }

    public InvocationBuilder params(InstructionBuilder... params) {
        this.setInstructionBuilders(Arrays.asList(params));
        return this;
    }

    public InvocationBuilder params(List<InstructionBuilder> params) {
        this.setInstructionBuilders(params);
        return this;
    }
}
