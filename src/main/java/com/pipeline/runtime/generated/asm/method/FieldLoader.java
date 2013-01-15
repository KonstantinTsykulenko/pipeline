package com.pipeline.runtime.generated.asm.method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/16/13
 */
public class FieldLoader extends AbstractMethodBodyGenerator {

    private String fieldName;
    private Class<?> fieldType;

    public FieldLoader(List<? extends MethodBodyGenerator> methodBodyGenerators, String fieldName, Class<?> fieldType) {
        super(methodBodyGenerators);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public FieldLoader(MethodBodyGenerator methodBodyGenerator, String fieldName, Class<?> fieldType) {
        super(methodBodyGenerator);
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public FieldLoader(String fieldName, Class<?> fieldType) {
        this((MethodBodyGenerator) null, fieldName, fieldType);
    }

    @Override
    protected void preGenerate(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new FieldInsnNode(Opcodes.GETFIELD,
                classNode.name,
                fieldName,
                Type.getDescriptor(fieldType)));
    }

    @Override
    protected void postGenerate(ClassNode classNode, MethodNode methodNode) {
        //Nothing to generate
    }
}
