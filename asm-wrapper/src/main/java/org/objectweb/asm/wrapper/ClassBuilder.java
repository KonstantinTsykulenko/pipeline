package org.objectweb.asm.wrapper;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public class ClassBuilder {

    /////////////////////////////////////////////
    // ATTRIBUTES

    private String className;

    private String superClassName = DefaultSettings.SUPER_CLASS;

    private int accessModifier = DefaultSettings.ACCESS_MODIFIER;

    private Set<Class> interfaces = new HashSet<Class>();

    private Set<MethodNode> constructors = new HashSet<MethodNode>();

    /////////////////////////////////////////////
    // METHODS

    public ClassBuilder publicClass(String className) {
        this.className = className.replace('.', '/');
        this.accessModifier = Opcodes.ACC_PUBLIC;

        return this;
    }

    public ClassBuilder implementing(Class ... interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));

        return this;
    }

    @SuppressWarnings("unchecked")
    public ClassNode build() {
        ClassNode classNode = new ClassNode(DefaultSettings.ASM_API_VERSION);

        classNode.version = DefaultSettings.JAVA_VERSION;
        classNode.access = accessModifier;
        classNode.superName = superClassName;
        classNode.name = className;
        classNode.interfaces = buildInterfaces();
        classNode.methods.addAll(buildConstructors());

        return classNode;
    }

    private List<String> buildInterfaces() {
        if (interfaces.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> interfaceNameList = new ArrayList<String>(interfaces.size());

        for (Class anInterface : interfaces) {
            interfaceNameList.add(Type.getInternalName(anInterface));
        }

        return interfaceNameList;
    }

    private Set<MethodNode> buildConstructors() {
        if (constructors.isEmpty()) {
            return Collections.singleton(buildDefaultConstructor());
        }
        return constructors;
    }

    private MethodNode buildDefaultConstructor() {
        MethodNode defaultConstructor = new MethodNode(Opcodes.ASM4, Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

        defaultConstructor.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        defaultConstructor.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, superClassName, "<init>", "()V"));
        defaultConstructor.instructions.add(new InsnNode(Opcodes.RETURN));

        return defaultConstructor;
    }
}
