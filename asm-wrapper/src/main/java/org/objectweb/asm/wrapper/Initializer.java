package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 2/9/13
 */
public class Initializer extends InstructionBuilder {
    private Class<?> classToInstantiate;
    private String className;
    private String fieldName;

    @Override
    protected List<AbstractInsnNode> build() {
        List<AbstractInsnNode> result = new ArrayList<AbstractInsnNode>(5);
        //load implicit this reference
        result.add(new VarInsnNode(Opcodes.ALOAD, 0));
        result.add(new TypeInsnNode(Opcodes.NEW, Type.getInternalName(classToInstantiate)));
        result.add(new InsnNode(Opcodes.DUP));
        //only support default constructor for now
        result.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Type.getInternalName(classToInstantiate), "<init>", "()V"));
        result.add(new FieldInsnNode(Opcodes.PUTFIELD, className, fieldName, Type.getDescriptor(classToInstantiate)));
        return result;
    }

    public static InstructionBuilder _new(Class<?> classToInstantiate, FieldBuilder fieldBuilder, String owner) {
        Initializer initializer = new Initializer();
        initializer.classToInstantiate = classToInstantiate;
        initializer.className = owner.replace(".", "/");
        initializer.fieldName = fieldBuilder.getFieldName();
        return initializer;
    }
}
