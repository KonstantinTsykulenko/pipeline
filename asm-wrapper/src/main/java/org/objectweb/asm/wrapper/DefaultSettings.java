package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public abstract class DefaultSettings {

    /////////////////////////////////////////////
    // CONSTANTS

    public static final int ASM_API_VERSION = Opcodes.ASM4;
    public static final int JAVA_VERSION = Opcodes.V1_6;
    public static final int ACCESS_MODIFIER = Opcodes.ACC_PUBLIC;
    public static final String SUPER_CLASS = "java/lang/Object";
}
