package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class LocalVariableLoader extends Invokable {
    private int localVariableIndex;

    private LocalVariableLoader(int localVariableIndex) {
        this.localVariableIndex = localVariableIndex;
    }

    @Override
    protected List<AbstractInsnNode> build() {
        return Arrays.<AbstractInsnNode>asList(new VarInsnNode(Opcodes.ALOAD, localVariableIndex));
    }

    public static LocalVariableLoader _v(int localVariableIndex) {
        return new LocalVariableLoader(localVariableIndex);
    }
}
