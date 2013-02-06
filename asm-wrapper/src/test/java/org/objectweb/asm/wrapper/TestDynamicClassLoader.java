package org.objectweb.asm.wrapper;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public class TestDynamicClassLoader extends ClassLoader {

    /////////////////////////////////////////////
    // CONSTRUCTORS

    public TestDynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    /////////////////////////////////////////////
    // METHODS

    public Class<?> loadClass(byte[] bytes) {
        return defineClass(null, bytes, 0, bytes.length);
    }
}
