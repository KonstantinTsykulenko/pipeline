package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 2/9/13
 */
public class ReturnBuilder extends InstructionBuilder {

    private int[] opcodes;

    @Override
    protected List<AbstractInsnNode> build() {
        List<AbstractInsnNode> result = new ArrayList<AbstractInsnNode>(opcodes.length);

        for (int opcode : opcodes) {
            result.add(new InsnNode(opcode));
        }

        return result;
    }

    public static InstructionBuilder _return() {
        ReturnBuilder returnBuilder = new ReturnBuilder();
        returnBuilder.opcodes = new int[]{Opcodes.RETURN};
        return returnBuilder;
    }

    public static InstructionBuilder _returnNull() {
        ReturnBuilder returnBuilder = new ReturnBuilder();
        returnBuilder.opcodes = new int[] {Opcodes.ACONST_NULL, Opcodes.ARETURN};
        return returnBuilder;
    }
}
