package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
class DefaultSettings {

    /////////////////////////////////////////////
    // CONSTRUCTORS

    /**
     * not designed for inheritance
     */
    private DefaultSettings() {}

    /////////////////////////////////////////////
    // CONSTANTS

    public static final int ASM_API_VERSION = Opcodes.ASM4;
    public static final int JAVA_VERSION = Opcodes.V1_6;
    public static final int METHOD_ACCESS_MODIFIER = Opcodes.ACC_PUBLIC;
    public static final int FIELD_ACCESS_MODIFIER = Opcodes.ACC_PRIVATE;
    public static final String SUPER_CLASS = Type.getInternalName(Object.class);
    public static final int METHOD_PARAM_COUNT = 6;
}
