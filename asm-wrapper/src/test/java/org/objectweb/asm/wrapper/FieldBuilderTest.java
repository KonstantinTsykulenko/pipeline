package org.objectweb.asm.wrapper;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.wrapper.ClassBuilder._class;
import static org.objectweb.asm.wrapper.FieldBuilder._field;

/**
 * @author Alex Chychkan
 * @since 2013/02/06
 */
public class FieldBuilderTest extends AbstractAsmWrapperTest {

    /////////////////////////////////////////////
    // TEST METHODS

    @Test
    public void testAddFieldToClass() throws NoSuchFieldException {
        ClassBuilder classBuilder = _class("TestClass").field(Object.class, "someField");

        Class<?> clazz = loadClass(classBuilder);

        assertNotNull(clazz.getDeclaredField("someField"));
    }

    @Test
    public void testByDefaultFieldIsPrivate() throws NoSuchFieldException {
        FieldBuilder fieldBuilder = _field(Object.class, "someField");

        Class<?> clazz = wrapIntoClass(fieldBuilder);

        Field field = clazz.getDeclaredField("someField");

        assertTrue(Modifier.isPrivate(field.getModifiers()));
    }

    @Test
    public void testFieldType() throws NoSuchFieldException {
        FieldBuilder doubleField = _field(Double.class, "doubleField");

        Class<?> clazz = wrapIntoClass(doubleField);

        Field field = clazz.getDeclaredField("doubleField");

        assertEquals(Double.class, field.getType());
    }

    @Test
    public void testPublicStaticField() throws NoSuchFieldException {
        FieldBuilder fieldBuilder = _field(String.class, "stringField").isPublic().isStatic();

        Class<?> clazz = wrapIntoClass(fieldBuilder);

        Field field = clazz.getDeclaredField("stringField");

        assertTrue(Modifier.isPublic(field.getModifiers()));
        assertTrue(Modifier.isStatic(field.getModifiers()));
    }

    /////////////////////////////////////////////
    // HELPER METHODS

    private Class<?> wrapIntoClass(FieldBuilder ...fieldBuilders) {
        ClassBuilder classBuilder = _class("TestClass");

        for (FieldBuilder fieldBuilder : fieldBuilders) {
            classBuilder.field(fieldBuilder);
        }

        return loadClass(classBuilder);
    }
}
