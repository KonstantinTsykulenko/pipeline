package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
public class CastBuilder extends InstructionBuilder {
    private Class<?> castTo;

    private CastBuilder(Class<?> castTo) {
        this.castTo = castTo;
    }

    @Override
    protected List<AbstractInsnNode> build() {
        return Arrays.<AbstractInsnNode>asList(new TypeInsnNode(Opcodes.CHECKCAST, Type.getInternalName(castTo)));
    }

    public static CastBuilder cast(Class<?> castTo) {
        return new CastBuilder(castTo);
    }
}
