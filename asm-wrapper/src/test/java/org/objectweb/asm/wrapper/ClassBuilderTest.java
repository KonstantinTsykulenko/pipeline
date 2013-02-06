package org.objectweb.asm.wrapper;

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
    // TEST METHODS

    @Test
    public void testBuildReturnsBytecode() {
        assertNotNull(new ClassBuilder("TestClass").build());
    }

    @Test
    public void testClassNameIsAsSpecified() {
        Class<?> clazz = loadClass(new ClassBuilder("com.some.package.test.SomeClass"));

        assertEquals("com.some.package.test.SomeClass", clazz.getCanonicalName());
    }

    @Test
    public void testByDefaultClassIsPublic() {
        Class<?> clazz = loadClass(new ClassBuilder("SomePublicClass"));

        assertTrue(Modifier.isPublic(clazz.getModifiers()));
    }

    @Test
    public void testClassImplementsAnInterface() {
        Class<?> clazz = loadClass(new ClassBuilder("SerializableClass").implementing(Serializable.class));

        assertTrue(Serializable.class.isAssignableFrom(clazz));
    }

    @Test
    public void testDefaultDefaultConstructor() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = loadClass(new ClassBuilder("ClassWithDefaultConstructor"));

        assertNotNull(clazz.newInstance());
    }

    @Test
    public void testSeveralAttributes() throws NoSuchFieldException {
        ClassBuilder builder =
                new ClassBuilder("TestClass").
                        field(new FieldBuilder(String.class, "fieldOne")).
                        field(new FieldBuilder(String.class, "fieldTwo")).
                        field(new FieldBuilder(String.class, "fieldThree"));

        Class<?> clazz = loadClass(builder);

        assertNotNull(clazz.getDeclaredField("fieldOne"));
        assertNotNull(clazz.getDeclaredField("fieldTwo"));
        assertNotNull(clazz.getDeclaredField("fieldThree"));
    }

    @Test(expected = IllegalStateException.class)
    public void testTwoFieldsCannotHaveSameName() {
        new ClassBuilder("TestClass").
                field(new FieldBuilder(String.class, "sameField")).
                field(new FieldBuilder(Object.class, "sameField")).build();
    }

    /////////////////////////////////////////////
    // HELPER METHODS

    private Class<?> loadClass(ClassBuilder classBuilder) {
        TestDynamicClassLoader classLoader = new TestDynamicClassLoader(Thread.currentThread().getContextClassLoader());
        return classLoader.loadClass(classBuilder.build());
    }
}
