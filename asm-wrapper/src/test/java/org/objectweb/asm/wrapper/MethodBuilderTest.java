package org.objectweb.asm.wrapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.wrapper.ClassBuilder._class;
import static org.objectweb.asm.wrapper.FieldBuilder._field;
import static org.objectweb.asm.wrapper.FieldLoader._f;
import static org.objectweb.asm.wrapper.InvocationBuilder.call;
import static org.objectweb.asm.wrapper.MethodBuilder._method;
import static org.objectweb.asm.wrapper.ReturnBuilder._return;

/**
 * @author Konstantin Tsykulenko
 * @since 2/9/13
 */
public class MethodBuilderTest extends AbstractAsmWrapperTest {

    @Test
    public void testSimpleMethod() throws Exception {
        Method method = MethodContainer.class.getMethod("simple");

        ClassBuilder classBuilder =
                _class("com.pipeline.TestClass1").method(
                        _method(method).$(
                                _return()
                        )
                );


        Class<?> aClass = loadClass(classBuilder);

        Method simple = aClass.getMethod("simple");

        assertTrue(Modifier.isPublic(simple.getModifiers()));
        assertTrue(simple.getReturnType().equals(void.class));
        assertTrue(simple.getParameterTypes().length == 0);
    }

    @Test
    public void testComplexMethod() throws Exception {
        Method methodToImpl = MethodContainer.class.getMethod("simple");
        Method methodToCall = Messenger.class.getMethod("deliverMessage");

        ClassBuilder classBuilder =
                _class("com.pipeline.TestClass1").implementing(MethodContainer.class).

                        field(Messenger.class, "messenger").

                        method(
                                _method(methodToImpl).$(
                                        call(methodToCall).on(_f(_field(Messenger.class, "messenger"), "com.pipeline.TestClass1")),
                                        _return()
                                )
                        );

        MethodContainer methodContainer = instantiate(classBuilder);
        Field messenger = methodContainer.getClass().getDeclaredField("messenger");
        messenger.setAccessible(true);
        messenger.set(methodContainer, new Messenger());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        methodContainer.simple();

        assertEquals(out.toString().trim(), Messenger.I_M_SORRY_BUT_YOUR_UNCLE_IS_DEAD);
    }

    public static interface MethodContainer {
        public void simple();
    }

    public static class Messenger {

        public static final String I_M_SORRY_BUT_YOUR_UNCLE_IS_DEAD = "I'm sorry, but your uncle is dead.";

        public void deliverMessage() {
            System.out.println(I_M_SORRY_BUT_YOUR_UNCLE_IS_DEAD);
        }
    }
}
