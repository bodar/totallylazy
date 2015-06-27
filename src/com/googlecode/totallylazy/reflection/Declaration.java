package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.predicates.Predicates;
import com.googlecode.totallylazy.Sequence;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static com.googlecode.totallylazy.functions.Functions.instanceOf;
import static com.googlecode.totallylazy.predicates.Predicates.not;

public class Declaration {
    private final String name;
    private final java.lang.reflect.Type type;

    public Declaration(String name, java.lang.reflect.Type type) {
        this.name = name;
        this.type = type;
    }

    public String name() {
        return name;
    }

    public java.lang.reflect.Type type() {
        return type;
    }

    public static Declaration declaration() {
        Sequence<StackFrame> stackFrame = StackFrames.stackFrames().tail();
        StackFrame callee = stackFrame.first();
        StackFrame caller = stackFrame.second();
        return caller.instructions().dropWhile(not(Predicates.instanceOf(MethodInsnNode.class,
                insn -> insn.owner.equals(callee.classNode().name) &&
                        insn.desc.equals(callee.methodNode().desc) &&
                        insn.name.equals(callee.methodNode().name)))).
                collect(instanceOf(VarInsnNode.class, node -> {
                            LocalVariableNode variable = Asm.localVariables(caller.methodNode()).filter(v -> v.index == node.var).head();
                            return new Declaration(variable.name, Signature.parse(variable.signature, callee.aClass()));
                        }),
                        instanceOf(FieldInsnNode.class, node -> {
                            FieldNode field = Asm.fields(caller.classNode()).filter(f -> f.name.equals(node.name)).head();
                            return new Declaration(field.name, Signature.parse(field.signature, callee.aClass()));
                        })).
                head();
    }
}
