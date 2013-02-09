package org.objectweb.asm.wrapper;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.objectweb.asm.wrapper.FieldBuilder._field;

/**
 * @author Alex Chychkan
 * @since 2013/02/05
 */
public class ClassBuilder {
    public static final float DEFAULT_HASHSET_LOAD_FACTOR = .75f;

    /////////////////////////////////////////////
    // ATTRIBUTES

    private final String className;

    private int accessModifier;

    private String superClassName;

    private Set<Class> interfaces;

    private Set<MethodNode> constructors;

    private Map<String, FieldBuilder> fields;

    private Set<MethodBuilder> methods;

    /////////////////////////////////////////////
    // CONSTRUCTORS

    private ClassBuilder(String className) {
        this.className = className.replace('.', '/');
        this.accessModifier = DefaultSettings.METHOD_ACCESS_MODIFIER;
        this.superClassName = DefaultSettings.SUPER_CLASS;

        this.interfaces = new HashSet<Class>();
        this.constructors = new HashSet<MethodNode>();
        this.fields = new HashMap<String, FieldBuilder>();
        this.methods = new HashSet<MethodBuilder>();
    }

    /////////////////////////////////////////////
    // METHODS

    public static ClassBuilder _class(String className) {
        return new ClassBuilder(className);
    }

    public ClassBuilder implementing(Class ... interfaces) {
        this.interfaces.addAll(Arrays.asList(interfaces));

        return this;
    }

    /**
     * Convenience method to add a field without using any {@link FieldBuilder} methods
     */
    public ClassBuilder field(Class<?> fieldType, String fieldName) {
        return field(_field(fieldType, fieldName));
    }

    public ClassBuilder field(FieldBuilder fieldBuilder) {
        if (fields.containsKey(fieldBuilder.getFieldName())) {
            throw new IllegalStateException("Field with name " + fieldBuilder.getFieldName() + " is already defined");
        }
        fields.put(fieldBuilder.getFieldName(), fieldBuilder);
        return this;
    }

    public ClassBuilder fields(FieldBuilder... fieldBuilders) {
        for (FieldBuilder fieldBuilder : fieldBuilders) {
            field(fieldBuilder);
        }
        return this;
    }

    public ClassBuilder fields(Collection<FieldBuilder> fieldBuilders) {
        for (FieldBuilder fieldBuilder : fieldBuilders) {
            field(fieldBuilder);
        }
        return this;
    }

    public ClassBuilder method(MethodBuilder methodBuilder) {
        methods.add(methodBuilder);
        return this;
    }

    @SuppressWarnings("unchecked")
    public byte[] build() {
        ClassNode classNode = buildClass();

        classNode.fields.addAll(buildFields());
        classNode.methods.addAll(buildMethods());

        return produceBytecode(classNode);
    }

    private Collection<FieldNode> buildFields() {
        HashSet<FieldNode> fieldNodes = createFittingHashSet(fields.size());

        for (FieldBuilder fieldBuilder : fields.values()) {
            fieldNodes.add(fieldBuilder.build());
        }

        return fieldNodes;
    }

    private Collection<MethodNode> buildMethods() {
        HashSet<MethodNode> methodNodes = this.createFittingHashSet(methods.size());

        for (MethodBuilder methodBuilder : methods) {
            methodNodes.add(methodBuilder.build());
        }

        return methodNodes;
    }

    /**
     * Convenience method that created a HashSet with capacity enough to fit a given number of elements without resizing
     * @param numElements number of elements to be added to the HashSet
     * @param <T> Type of the HashSet elements
     * @return HashSet with capacity enough to fit a given number of elements without resizing
     */
    private <T> HashSet<T> createFittingHashSet(int numElements) {
        return new HashSet<T>((int) (numElements / DEFAULT_HASHSET_LOAD_FACTOR) + 1);
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
