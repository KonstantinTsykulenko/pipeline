package org.objectweb.asm.wrapper;

/**
 * @author Konstantin Tsykulenko
 * @since 1/18/13
 */
public interface ReferenceLoader extends MethodBodyGenerator{
    void setReferenceType(Class<?> referenceType);
}
