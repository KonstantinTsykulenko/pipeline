package org.objectweb.asm.wrapper;

import org.objectweb.asm.ClassWriter;
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

    private final String className;

    private int accessModifier;

    private String superClassName;

    private Set<Class> interfaces;

    private Set<MethodNode> constructors;

    private Map<String, FieldBuilder> fields;

    /////////////////////////////////////////////
    // CONSTRUCTORS

    public ClassBuilder(String className) {
        this.className = className.replace('.', '/');
        this.accessModifier = DefaultSettings.METHOD_ACCESS_MODIFIER;
        this.superClassName = DefaultSettings.SUPER_CLASS;

        this.interfaces = new HashSet<Class>();
        this.constructors = new HashSet<MethodNode>();
        this.fields = new HashMap<String, FieldBuilder>();
    }

    /////////////////////////////////////////////
    // METHODS

    public ClassBuilder implementing(Class ... interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));

        return this;
    }

    public ClassBuilder field(FieldBuilder fieldBuilder) {
        if (fields.containsKey(fieldBuilder.getFieldName())) {
            throw new IllegalStateException("Field with name " + fieldBuilder.getFieldName() + " is already defined");
        }
        fields.put(fieldBuilder.getFieldName(), fieldBuilder);
        return this;
    }

    @SuppressWarnings("unchecked")
    public byte[] build() {
        ClassNode classNode = buildClass();

        classNode.fields.addAll(buildFields());

        return produceBytecode(classNode);
    }

    private Collection<FieldNode> buildFields() {
        HashSet<FieldNode> fieldNodes = new HashSet<FieldNode>();

        for (FieldBuilder fieldBuilder : fields.values()) {
            fieldNodes.add(fieldBuilder.build());
        }

        return fieldNodes;
    }

    private byte[] produceBytecode(ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(cw);

        return cw.toByteArray();
    }

    @SuppressWarnings("unchecked")
    private ClassNode buildClass() {
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
