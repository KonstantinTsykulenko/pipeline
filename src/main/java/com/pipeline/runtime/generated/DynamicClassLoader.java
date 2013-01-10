package com.pipeline.runtime.generated;

/**
 * @author Konstantin Tsykulenko
 * @since 1/10/13
 */
public class DynamicClassLoader extends ClassLoader {
    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public DynamicClassLoader() {
    }

    public Class<?> loadClass(byte[] bytes) {
        return defineClass(null, bytes, 0, bytes.length);
    }
}
