package org.objectweb.asm.wrapper;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 2/9/13
 */
public abstract class InstructionBuilder {
    protected abstract List<AbstractInsnNode> build();
}
