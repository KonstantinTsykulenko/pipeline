package org.objectweb.asm.wrapper;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

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

    public Class<?> loadClass(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        byte[] bytes = cw.toByteArray();

        return defineClass(null, bytes, 0, bytes.length);
    }

}
