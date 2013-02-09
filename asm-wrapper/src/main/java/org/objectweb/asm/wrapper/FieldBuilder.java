package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;

/**
 * @author Alex Chychkan
 * @since 2013/02/06
 */
public class FieldBuilder {

    /////////////////////////////////////////////
    // ATTRIBUTES

    private final Class<?> fieldType;

    private final String fieldName;

    private int accessModifiers;

    private boolean isStatic;

    /////////////////////////////////////////////
    // CONSTRUCTORS

    private FieldBuilder(Class<?> fieldType, String fieldName) {
        this.fieldType = fieldType;
        this.fieldName = fieldName;
        this.accessModifiers = DefaultSettings.FIELD_ACCESS_MODIFIER;
        this.isStatic = false;
    }

    /////////////////////////////////////////////
    // METHODS

    public static FieldBuilder _field(Class<?> fieldType, String fieldName) {
        return new FieldBuilder(fieldType, fieldName);
    }

    public FieldBuilder isPublic() {
        accessModifiers = Opcodes.ACC_PUBLIC;
        return this;
    }

    public FieldBuilder isStatic() {
        isStatic = true;
        return this;
    }

    FieldNode build() {
        if (isStatic) {
            accessModifiers |= Opcodes.ACC_STATIC;
        }
        return new FieldNode(accessModifiers, fieldName, Type.getDescriptor(fieldType), null, null);
    }

    /////////////////////////////////////////////
    // GETTERS AND SETTERS

    String getFieldName() {
        return fieldName;
    }
}
