package org.objectweb.asm.wrapper;

/**
 * @author Konstantin Tsykulenko
 * @since 2/9/13
 */
public class AbstractAsmWrapperTest {
    protected Class<?> loadClass(ClassBuilder classBuilder) {
        TestDynamicClassLoader classLoader = new TestDynamicClassLoader(Thread.currentThread().getContextClassLoader());
        return classLoader.loadClass(classBuilder.build());
    }

    protected <T> T instantiate(ClassBuilder classBuilder) throws IllegalAccessException, InstantiationException {
        return (T) loadClass(classBuilder).newInstance();
    }
}
