package org.objectweb.asm.wrapper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public abstract class AbstractComplexMethodBodyGenerator implements  MethodBodyGenerator{
    private List<? extends MethodBodyGenerator> methodBodyGenerators;

    public AbstractComplexMethodBodyGenerator(List<? extends MethodBodyGenerator> methodBodyGenerators) {
        this.methodBodyGenerators = methodBodyGenerators;
    }

    public AbstractComplexMethodBodyGenerator(MethodBodyGenerator methodBodyGenerator) {
        this.methodBodyGenerators = Arrays.asList(methodBodyGenerator);
    }

    @Override
    public void generateMethodBody(ClassNode classNode, MethodNode methodNode) {
        preGenerate(classNode, methodNode);

        callSubGenerator(classNode, methodNode);

        postGenerate(classNode, methodNode);
    }

    private void callSubGenerator(ClassNode classNode, MethodNode methodNode) {
        if (methodBodyGenerators == null) {
            return;
        }

        for (MethodBodyGenerator methodBodyGenerator : methodBodyGenerators) {
            if (methodBodyGenerator != null) {
                methodBodyGenerator.generateMethodBody(classNode, methodNode);
            }
        }
    }

    protected abstract void preGenerate(ClassNode classNode, MethodNode methodNode);

    protected abstract void postGenerate(ClassNode classNode, MethodNode methodNode);
}
