package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class FieldLoader extends Invokable {

    private String fieldName;
    private Class<?> fieldType;
    private String ownerName;

    private FieldLoader(String fieldName, Class<?> fieldType, String ownerName) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.ownerName = ownerName;
    }

    @Override
    protected List<AbstractInsnNode> build() {
        if (fieldType == null) {
            throw new IllegalStateException("Field type can not be null");
        }
        return Arrays.<AbstractInsnNode>asList(new FieldInsnNode(Opcodes.GETFIELD,
                ownerName,
                fieldName,
                Type.getDescriptor(fieldType)));
    }

    public static FieldLoader _f(FieldBuilder fieldBuilder, String ownerName) {
        return new FieldLoader(fieldBuilder.getFieldName(),
                fieldBuilder.getFieldType(),
                ownerName.replace(".", "/"));
    }
}
