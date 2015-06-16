package com.googlecode.totallylazy.proxy;

import com.googlecode.totallylazy.Bytes;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.iterators.ReadOnlyIterator;
import net.sf.cglib.proxy.InvocationHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Fields.fields;
import static com.googlecode.totallylazy.Fields.name;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.proxy.Generics.getGenericSuperclassType;
import static com.googlecode.totallylazy.proxy.Proxy.createProxy;
import static java.lang.Class.forName;

public abstract class FieldOn<T, S> implements Function1<T, S>, InvocationHandler {
    private final FieldInvocation<T, S> invocation;
    protected final T instance;


    public FieldOn() {
        Class<T> aClass = getGenericSuperclassType(this.getClass(), 0);
        instance = createProxy(aClass, this);
        invocation = new FieldInvocation<T, S>(readFieldFromByteCode());
    }

    private Field readFieldFromByteCode() {
        try {
            ClassNode classNode = Asm.classNode(getClass());
            MethodNode constructor = (MethodNode) classNode.methods.get(0);
            FieldInsnNode fieldInsnNode = Asm.instructions(constructor).safeCast(FieldInsnNode.class).last();
            Class<?> aClass = forName(fieldInsnNode.owner.replace('/', '.'));
            return fields(aClass).
                    find(where(name, is(fieldInsnNode.name))).
                    get();
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    protected void get(S s) {
    }

    @Override
    public S call(T t) throws Exception {
        return invocation.call(t);
    }

    public FieldInvocation<T, S> invocation() {
        return invocation;
    }

    public Field field() {
        return invocation.field();
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        throw new UnsupportedOperationException("Please use CallOn for method invocation");
    }

    private static class Asm {
        public static ClassNode classNode(final Class<?> aClass) {
            return classNode(Bytes.bytes(aClass));
        }

        public static ClassNode classNode(final byte[] bytes) {
            ClassReader reader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, 0);
            return classNode;
        }

        @SuppressWarnings("unchecked")
        public static Sequence<AbstractInsnNode> instructions(MethodNode method) {
            return instructions(method.instructions);
        }

        @SuppressWarnings("unchecked")
        public static Sequence<AbstractInsnNode> instructions(final InsnList instructions) {
            return new Sequence<AbstractInsnNode>() {
                @Override
                public Iterator<AbstractInsnNode> iterator() {
                    return new InsnIterator(instructions);
                }
            };
        }

        public static class InsnIterator extends ReadOnlyIterator<AbstractInsnNode> {
            private final InsnList list;
            private int index = 0;

            public InsnIterator(final InsnList list) {
                this.list = list;
            }

            public final boolean hasNext() {
                return index < list.size();
            }

            public final AbstractInsnNode next() {
                if(hasNext()){
                    return list.get(index++);
                }
                throw new NoSuchElementException();
            }
        }


    }
}
