package org.objectweb.asm.wrapper;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/17/13
 */
public class MethodBuilder {

    private Method methodSignature;
    private List<InstructionBuilder> instructions;

    private MethodBuilder(Method signature) {
        instructions = new LinkedList<InstructionBuilder>();
        methodSignature = signature;
    }

    public static MethodBuilder _method(Method signature) {
        return new MethodBuilder(signature);
    }

    MethodNode build() {
        MethodNode methodNode = buildMethodNode();
        for (InstructionBuilder instructionBuilder : instructions) {
            for (AbstractInsnNode instrNode : instructionBuilder.build()) {
                methodNode.instructions.add(instrNode);
            }
        }
        return methodNode;
    }

    private MethodNode buildMethodNode() {
        return new MethodNode(DefaultSettings.ASM_API_VERSION,
                DefaultSettings.METHOD_ACCESS_MODIFIER,
                methodSignature.getName(),
                Type.getMethodDescriptor(methodSignature),
                null /* signature : not used for now */,
                null /* exceptions : not used for now */);
    }

    public MethodBuilder $(List<InstructionBuilder> instructionBuilders) {
        instructions.addAll(instructionBuilders);
        return this;
    }

    public MethodBuilder $(InstructionBuilder... instructionBuilders) {
        instructions.addAll(Arrays.asList(instructionBuilders));
        return this;
    }
}
