package org.objectweb.asm.wrapper;

import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.wrapper.ClassBuilder._class;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public class ClassBuilderTest extends AbstractAsmWrapperTest {

    /////////////////////////////////////////////
    // TEST METHODS

    @Test
    public void testBuildReturnsBytecode() {
        assertNotNull(_class("TestClass").build());
    }

    @Test
    public void testClassNameIsAsSpecified() {
        Class<?> clazz = loadClass(_class("com.some.package.test.SomeClass"));

        assertEquals("com.some.package.test.SomeClass", clazz.getCanonicalName());
    }

    @Test
    public void testByDefaultClassIsPublic() {
        Class<?> clazz = loadClass(_class("SomePublicClass"));

        assertTrue(Modifier.isPublic(clazz.getModifiers()));
    }

    @Test
    public void testClassImplementsAnInterface() {
        Class<?> clazz = loadClass(_class("SerializableClass").implementing(Serializable.class));

        assertTrue(Serializable.class.isAssignableFrom(clazz));
    }

    @Test
    public void testDefaultDefaultConstructor() throws IllegalAccessException, InstantiationException {
        Class<?> clazz = loadClass(_class("ClassWithDefaultConstructor"));

        assertNotNull(clazz.newInstance());
    }

    @Test
    public void testSeveralAttributes() throws NoSuchFieldException {
        Class<?> clazz = loadClass(_class("TestClass").
                field(String.class, "fieldOne").
                field(String.class, "fieldTwo").
                field(String.class, "fieldThree"));

        assertNotNull(clazz.getDeclaredField("fieldOne"));
        assertNotNull(clazz.getDeclaredField("fieldTwo"));
        assertNotNull(clazz.getDeclaredField("fieldThree"));
    }

    @Test(expected = IllegalStateException.class)
    public void testTwoFieldsCannotHaveSameName() {
        _class("TestClass").
                field(String.class, "sameField").
                field(Object.class, "sameField").build();
    }
}
