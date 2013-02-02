package com.pipeline.runtime.generated.asm.wrapper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Konstantin Tsykulenko
 * @since 1/17/13
 */
public class MethodBuilder {

    private Method methodToInvoke;
    private ReferenceLoader referenceToInvokeOn;
    private List<MethodBodyGenerator> params = new ArrayList<MethodBodyGenerator>(6);

    private MethodBuilder() {}

    public static MethodBuilder callOn(ReferenceLoader referenceLoader) {
        MethodBuilder methodBuilder = new MethodBuilder();
        methodBuilder.referenceToInvokeOn = referenceLoader;
        return methodBuilder;
    }

    public static ReferenceLoader getField(String fieldName) {
        FieldLoader fieldLoader = new FieldLoader(fieldName);
        return fieldLoader;
    }

    public static ReferenceLoader getLocalVariable(int index) {
        LocalVariableLoader localVariableLoader = new LocalVariableLoader(index);
        return localVariableLoader;
    }

    public static ReferenceLoader value(Object value) {
        ValueLoader valueLoader = new ValueLoader(value);
        return valueLoader;
    }

    public static MethodBodyGenerator cast(MethodBuilder builder, Class<?> to) {
        Caster caster = new Caster(builder.getGenerator(), to);
        return caster;
    }

    public MethodBuilder method(Method method) {
        methodToInvoke = method;
        return this;
    }

    public MethodBuilder param(MethodBodyGenerator paramSource) {
        params.add(paramSource);
        return this;
    }

    public MethodBuilder params(MethodBodyGenerator... paramSources) {
        params.addAll(Arrays.asList(paramSources));
        return this;
    }

    public MethodBuilder params(List<MethodBodyGenerator> paramSources) {
        params.addAll(paramSources);
        return this;
    }

    public MethodBodyGenerator getGenerator() {
        referenceToInvokeOn.setReferenceType(methodToInvoke.getDeclaringClass());
        params.add(0, referenceToInvokeOn);
        MethodInvoker methodInvoker = new MethodInvoker(params, methodToInvoke.getDeclaringClass(), methodToInvoke);
        return methodInvoker;
    }

    public void writeTo(ClassNode classNode, MethodNode methodNode) {
        getGenerator().generateMethodBody(classNode, methodNode);
    }
}
