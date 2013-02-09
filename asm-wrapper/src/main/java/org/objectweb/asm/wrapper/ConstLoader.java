package org.objectweb.asm.wrapper;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
public class ConstLoader extends InstructionBuilder {
    private Object value;

    private ConstLoader(Object value) {
        this.value = value;
    }

    @Override
    protected List<AbstractInsnNode> build() {
        return Arrays.<AbstractInsnNode>asList(new LdcInsnNode(value));
    }

    public static ConstLoader _c(Object value) {
        return new ConstLoader(value);
    }
}
