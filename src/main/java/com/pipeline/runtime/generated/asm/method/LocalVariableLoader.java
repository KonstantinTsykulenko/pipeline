package com.pipeline.runtime.generated.asm.method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class LocalVariableLoader extends  AbstractMethodBodyGenerator {
    private int localVariableIndex;

    public LocalVariableLoader(List<? extends MethodBodyGenerator> methodBodyGenerators, int localVariableIndex) {
        super(methodBodyGenerators);
        this.localVariableIndex = localVariableIndex;
    }

    public LocalVariableLoader(MethodBodyGenerator methodBodyGenerator, int localVariableIndex) {
        super(methodBodyGenerator);
        this.localVariableIndex = localVariableIndex;
    }

    public LocalVariableLoader(int localVariableIndex) {
        this((MethodBodyGenerator) null,localVariableIndex);
        this.localVariableIndex = localVariableIndex;
    }

    @Override
    protected void preGenerate(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new VarInsnNode(Opcodes.ALOAD, localVariableIndex));
    }

    @Override
    protected void postGenerate(ClassNode classNode, MethodNode methodNode) {
        //Nothing to generate
    }
}
