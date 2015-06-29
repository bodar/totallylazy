package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Randoms;
import com.googlecode.totallylazy.functions.Function0;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.reflection.Asm;
import com.googlecode.totallylazy.reflection.Reflection;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.numbers.Numbers.sum;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Proxy.newProxyInstance;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Proxy {
    public static final String HANDLER = "handler";
    private static ConcurrentMap<Class<?>, Class<?>> cache = new ConcurrentHashMap<>();

    public static <T> T proxy(Class<T> aClass, InvocationHandler handler) {
        try {
            if(aClass.isInterface()) {
                Object instance = newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, handler);
                return cast(instance);
            }

            Class<?> definedClass = cache.computeIfAbsent(aClass, k -> {
                String name = k.getName() + Randoms.integers().head();
                String jvmName = name.replace('.', '/');
                byte[] bytes = bytes(jvmName, k);
                return new DefinableClassLoader().defineClass(name, bytes);
            });

            T instance = Reflection.create(definedClass);
            Field field = definedClass.getDeclaredField(HANDLER);
            field.setAccessible(true);
            field.set(instance, handler);
            return instance;
        } catch (Exception e) {
            throw LazyException.lazyException(e);
        }
    }

    public static byte[] bytes(String name, Class<?> superClass) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        String superName = Type.getInternalName(superClass);
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, name, null, superName, null);

        handlerField(cw);

        for (Method method : sequence(superClass.getMethods()).
                reject(m -> isFinal(m.getModifiers())).
                reject(m -> isStatic(m.getModifiers())).
                reject(m -> m.getName().equals("toString"))) {
            method(cw, name, method, superName);
        }

        cw.visitEnd();
        return cw.toByteArray();
    }

    private static void handlerField(ClassWriter cw) {
        FieldVisitor fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, HANDLER, "Ljava/lang/reflect/InvocationHandler;", null, null);
        fv.visitEnd();
    }

    private static void method(ClassWriter cw, String name, Method method, String superClass) {
        String[] exceptions = sequence(method.getExceptionTypes()).map(Type::getInternalName).toArray(String.class);
        String methodName = method.getName();
        String methodDescriptor = Type.getMethodDescriptor(method);
        int size = sequence(Type.getArgumentTypes(methodDescriptor)).map(Type::getSize).reduce(sum).intValue() + 1;

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, methodName, methodDescriptor, null, exceptions);
        mv.visitCode();
        mv.visitLdcInsn(superClass);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(methodDescriptor);
        mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/totallylazy/reflection/Methods", "method", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/reflect/Method;", false);
        mv.visitVarInsn(ASTORE, size);

        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, name, HANDLER, "Ljava/lang/reflect/InvocationHandler;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, size);

        int arguments = method.getParameterCount();
        mv.visitLdcInsn(arguments);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");

        handleArguments(mv, method);

        mv.visitMethodInsn(INVOKEINTERFACE, "java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);

        handleReturnType(mv, method);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void handleArguments(MethodVisitor mv, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0, local = 0 ; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            local++;
            mv.visitInsn(DUP);
            mv.visitLdcInsn(i);
            mv.visitVarInsn(Asm.load(parameterType), local);
            if(parameterType.isPrimitive() && !parameterType.equals(void.class)) {
                String internalName = Type.getInternalName(Reflection.box(parameterType));
                mv.visitMethodInsn(INVOKESTATIC, internalName, "valueOf", format("(%s)L%s;", Type.getDescriptor(parameterType), internalName), false);
            }
            if(parameterType.equals(double.class) || parameterType.equals(long.class)) local++;
            mv.visitInsn(AASTORE);
        }
    }

    private static void handleReturnType(MethodVisitor mv, Method method) {
        Class<?> returnType = method.getReturnType();
        if(returnType.equals(void.class)) {
            mv.visitInsn(POP);
        } else if(returnType.isPrimitive()){
            String internalName = Type.getInternalName(Reflection.box(returnType));
            mv.visitTypeInsn(CHECKCAST, internalName);
            mv.visitMethodInsn(INVOKEVIRTUAL, internalName, format("%sValue", returnType.getSimpleName()), format("()%s", Type.getDescriptor(returnType)), false);
        } else {
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
        }
        mv.visitInsn(Asm.returns(returnType));
    }

    public static <T> T lazy(Class<T> aClass, Callable<T> callable) {
        Lazy<T> lazy = Lazy.lazy(callable);
        return proxy(aClass, (proxy, method, arguments) -> method.invoke(lazy.value(), arguments));
    }
}