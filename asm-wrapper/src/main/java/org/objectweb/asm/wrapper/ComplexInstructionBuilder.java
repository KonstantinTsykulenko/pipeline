package org.objectweb.asm.wrapper;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public abstract class ComplexInstructionBuilder extends InstructionBuilder {
    private List<? extends InstructionBuilder> instructionBuilders;

    protected abstract List<AbstractInsnNode> preBuild();

    protected abstract List<AbstractInsnNode> postBuild();

    @Override
    public List<AbstractInsnNode> build() {
        List<AbstractInsnNode> resultList = new LinkedList<AbstractInsnNode>();

        resultList.addAll(preBuild());

        resultList.addAll(callSubBuilders());

        resultList.addAll(postBuild());

        return resultList;
    }

    private List<AbstractInsnNode> callSubBuilders() {
        if (instructionBuilders == null) {
            return Collections.emptyList();
        }

        List<AbstractInsnNode> resultList = new LinkedList<AbstractInsnNode>();

        for (InstructionBuilder instructionBuilder : instructionBuilders) {
            if (instructionBuilder != null) {
                resultList.addAll(instructionBuilder.build());
            }
        }
        return resultList;
    }

    protected void setInstructionBuilders(List<? extends InstructionBuilder> instructionBuilders) {
        this.instructionBuilders = instructionBuilders;
    }
}
