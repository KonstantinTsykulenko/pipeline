package com.pipeline.runtime.generated.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class LocalVariableLoader implements ReferenceLoader {
    private int localVariableIndex;

    public LocalVariableLoader(int localVariableIndex) {
        this.localVariableIndex = localVariableIndex;
    }

    @Override
    public void generateMethodBody(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, localVariableIndex));
    }

    @Override
    public void setReferenceType(Class<?> referenceType) {
        // Not required
    }
}