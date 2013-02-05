package org.objectweb.asm.wrapper;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public class ClassBuilderTest {

    /////////////////////////////////////////////
    // ATTRIBUTES

    private TestDynamicClassLoader classLoader;
    private ClassBuilder builder;

    /////////////////////////////////////////////
    // METHODS

    @Before
    public void setUp() {
        builder = new ClassBuilder();
        classLoader = new TestDynamicClassLoader(Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void testBuildReturnsNotNull() {
        assertNotNull(builder.publicClass("TestClass").build());
    }

    @Test
    public void testClassNameIsAsSpecified() {
        String className = "com.some.package.test.SomeClass";

        ClassBuilder someClass = builder.publicClass(className);

        Class<?> clazz = classLoader.loadClass(someClass.build());

        assertEquals(className, clazz.getCanonicalName());
    }

    @Test
    public void testClassIsPublic() {
        ClassBuilder publicClass = builder.publicClass("SomePublicClass");

        Class<?> clazz = classLoader.loadClass(publicClass.build());

        assertTrue(Modifier.isPublic(clazz.getModifiers()));
    }

    @Test
    public void testImplements() {
        ClassBuilder serializableClass = builder.publicClass("SerializableClass").implementing(Serializable.class);

        Class<?> clazz = classLoader.loadClass(serializableClass.build());

        assertTrue(Serializable.class.isAssignableFrom(clazz));
    }

    @Test
    public void testDefaultDefaultConstructor() throws IllegalAccessException, InstantiationException {
        ClassBuilder classWithDefaultConstructor = builder.publicClass("ClassWithDefaultConstructor");

        Class<?> clazz = classLoader.loadClass(classWithDefaultConstructor.build());

        assertNotNull(clazz.newInstance());
    }
}
