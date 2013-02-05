package org.objectweb.asm.wrapper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public interface MethodBodyGenerator {
    void generateMethodBody(ClassNode classNode, MethodNode methodNode);
}