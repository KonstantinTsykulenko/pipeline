package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.util.List;

/**
 * @author KonstantinTsykulenko
 * @since 1/18/13
 */
public class Caster extends AbstractComplexMethodBodyGenerator {
    private Class<?> castTo;

    public Caster(List<? extends MethodBodyGenerator> methodBodyGenerators, Class<?> castTo) {
        super(methodBodyGenerators);
        this.castTo = castTo;
    }

    public Caster(MethodBodyGenerator methodBodyGenerator, Class<?> castTo) {
        super(methodBodyGenerator);
        this.castTo = castTo;
    }

    public Caster(Class<?> castTo) {
        this((MethodBodyGenerator) null, castTo);
        this.castTo = castTo;
    }


    @Override
    protected void preGenerate(ClassNode classNode, MethodNode methodNode) {
        //do nothing
    }

    @Override
    protected void postGenerate(ClassNode classNode, MethodNode methodNode) {
        methodNode.instructions.add(new TypeInsnNode(Opcodes.CHECKCAST, Type.getInternalName(castTo)));
    }
}
