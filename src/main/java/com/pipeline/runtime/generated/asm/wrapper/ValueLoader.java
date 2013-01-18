package com.pipeline.runtime.generated.asm.wrapper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
public class ValueLoader implements ReferenceLoader{
    private Object value;

    public ValueLoader(Object value) {
        this.value = value;
    }

    @Override
    public void setReferenceType(Class<?> referenceType) {
        // Not needed
    }

    @Override
    public void generateMethodBody(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new LdcInsnNode(value));
    }
}
