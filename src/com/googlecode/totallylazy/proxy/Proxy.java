package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Randoms;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.functions.Lazy;
import com.googlecode.totallylazy.reflection.Asm;
import com.googlecode.totallylazy.reflection.Declaration;
import com.googlecode.totallylazy.reflection.Reflection;
import com.googlecode.totallylazy.reflection.Types;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static java.lang.String.format;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Proxy.newProxyInstance;
import static jdk.internal.org.objectweb.asm.Opcodes.*;

public class Proxy {
    public static final String HANDLER = "handler";
    private static ConcurrentMap<Class<?>, Class<?>> cache = new ConcurrentHashMap<>();

    public static <T> T proxy(Class<T> aClass, InvocationHandler handler) {
        if(isFinal(aClass.getModifiers())) throw new UnsupportedOperationException("Can not create a Proxy instance of final class: " + aClass);

        try {
            if (aClass.isInterface()) {
                Object instance = newProxyInstance(aClass.getClassLoader(), new Class[]{aClass}, handler);
                return cast(instance);
            }

            Class<?> definedClass = cache.computeIfAbsent(aClass, k -> {
                String name = generatePackageName(k) + ".Proxy" + UUID.randomUUID();
                String jvmName = name.replace('.', '/');
                byte[] bytes = bytes(jvmName, k);
                return defineClass(k, name, bytes);
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

    private static Class<?> defineClass(Class<?> aClass, String name, byte[] bytes) {
        return Reflection.defineClass(isPublic(aClass.getModifiers()) ? Proxy.class.getClassLoader() : aClass.getClassLoader(), name, bytes);
    }

    private static String generatePackageName(Class<?> aClass) {
        return isPublic(aClass.getModifiers()) ? packageName(Proxy.class) : packageName(aClass);
    }

    private static String packageName(Class<?> aClass){
        return sequence(aClass.getName().split("\\.")).init().toString(".");
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
        cw.visitField(ACC_PROTECTED, HANDLER, "Ljava/lang/reflect/InvocationHandler;", null, null).visitEnd();
    }

    private static void method(ClassWriter cw, String name, Method method, String superClass) {
        String[] exceptions = sequence(method.getExceptionTypes()).map(Type::getInternalName).toArray(String.class);
        String methodName = method.getName();
        String methodDescriptor = Type.getMethodDescriptor(method);

        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + (method.isVarArgs() ? ACC_VARARGS : 0), methodName, methodDescriptor, null, exceptions);
        mv.visitCode();

        loadHandler(mv, name);
        loadThis(mv);
        loadMethod(mv, superClass, methodName, methodDescriptor);
        loadArguments(mv, method);
        invokeHandler(mv);
        returnResult(mv, method);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void invokeHandler(MethodVisitor mv) {
        mv.visitMethodInsn(INVOKEINTERFACE, "java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;", true);
    }

    private static void loadThis(MethodVisitor mv) {
        mv.visitVarInsn(ALOAD, 0);
    }

    private static void loadHandler(MethodVisitor mv, String name) {
        loadThis(mv);
        mv.visitFieldInsn(GETFIELD, name, HANDLER, "Ljava/lang/reflect/InvocationHandler;");
    }

    private static void loadMethod(MethodVisitor mv, String superClass, String methodName, String methodDescriptor) {
        mv.visitLdcInsn(superClass);
        mv.visitLdcInsn(methodName);
        mv.visitLdcInsn(methodDescriptor);
        mv.visitMethodInsn(INVOKESTATIC, "com/googlecode/totallylazy/reflection/Methods", "method", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/reflect/Method;", false);
    }

    private static void loadArguments(MethodVisitor mv, Method method) {
        int arguments = method.getParameterCount();
        mv.visitLdcInsn(arguments);
        mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0, local = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            local++;
            mv.visitInsn(DUP);
            mv.visitLdcInsn(i);
            mv.visitVarInsn(Asm.load(parameterType), local);
            if (parameterType.isPrimitive() && !parameterType.equals(void.class)) {
                String internalName = Type.getInternalName(Reflection.box(parameterType));
                mv.visitMethodInsn(INVOKESTATIC, internalName, "valueOf", format("(%s)L%s;", Type.getDescriptor(parameterType), internalName), false);
            }
            if (parameterType.equals(double.class) || parameterType.equals(long.class)) local++;
            mv.visitInsn(AASTORE);
        }
    }

    private static void returnResult(MethodVisitor mv, Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(void.class)) {
            mv.visitInsn(POP);
        } else if (returnType.isPrimitive()) {
            String internalName = Type.getInternalName(Reflection.box(returnType));
            mv.visitTypeInsn(CHECKCAST, internalName);
            mv.visitMethodInsn(INVOKEVIRTUAL, internalName, format("%sValue", returnType.getSimpleName()), format("()%s", Type.getDescriptor(returnType)), false);
        } else {
            mv.visitTypeInsn(CHECKCAST, Type.getInternalName(returnType));
        }
        mv.visitInsn(Asm.returns(returnType));
    }

    public static <T> T lazy(Callable<? extends T> callable) {
        Declaration declaration = Declaration.declaration();
        return lazy(Types.classOf(declaration.type()), callable);
    }

    public static <T> T lazy(Class<? extends T> aClass, Callable<? extends T> callable) {
        if(aClass.equals(void.class) || aClass.equals(Void.class)) return null;
        Lazy<T> lazy = Lazy.lazy(callable);
        return proxy(aClass, (proxy, method, arguments) -> method.invoke(lazy.value(), arguments));
    }
}