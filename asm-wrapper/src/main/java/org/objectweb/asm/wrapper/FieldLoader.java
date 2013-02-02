package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class FieldLoader implements ReferenceLoader {

    private String fieldName;
    private Class<?> fieldType;

    public FieldLoader(String fieldName, Class<?> fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public FieldLoader(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public void generateMethodBody(ClassNode classNode, MethodNode methodNode) {
        if (fieldType == null) {
            throw new IllegalStateException();
        }

        methodNode.instructions.add(new FieldInsnNode(Opcodes.GETFIELD,
                classNode.name,
                fieldName,
                Type.getDescriptor(fieldType)));
    }

    @Override
    public void setReferenceType(Class<?> referenceType) {
        this.fieldType = referenceType;
    }
}
