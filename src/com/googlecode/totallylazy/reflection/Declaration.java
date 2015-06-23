package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Sequence;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.sequence;

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

    public static Declaration declaration(int drop) {
        Sequence<StackFrame> stackFrame = StackFrames.stackFrames().drop(drop + 1);
        StackFrame callee = stackFrame.first();
        StackFrame caller = stackFrame.second();
        Sequence<AbstractInsnNode> instructions = caller.instructions();
        AbstractInsnNode assignment = assignment(instructions, callee);
        if(assignment instanceof VarInsnNode) {
            VarInsnNode varInsnNode = (VarInsnNode) assignment;
            Sequence<LocalVariableNode> localVariables = Asm.localVariables(caller.methodNode());
            LocalVariableNode variableNode = localVariables.filter(v -> v.index == varInsnNode.var).head();
            return new Declaration(variableNode.name, Signature.parse(variableNode.signature, callee.aClass()));
        }
        if(assignment instanceof FieldInsnNode) {
            FieldInsnNode fieldInsnNode = (FieldInsnNode) assignment;
            FieldNode field = Asm.fields(caller.classNode()).filter(f -> f.name.equals(fieldInsnNode.name)).head();
            return new Declaration(field.name, Signature.parse(field.signature, callee.aClass()));
        }
        throw new UnsupportedOperationException();
    }

    static AbstractInsnNode assignment(Sequence<AbstractInsnNode> instructions, StackFrame callee) {
        return instructions.dropWhile(not(instanceOf(MethodInsnNode.class,
                insn -> insn.owner.equals(callee.classNode().name) &&
                        insn.desc.equals(callee.methodNode().desc) &&
                        insn.name.equals(callee.methodNode().name)))).
                find(instanceOf(VarInsnNode.class).or(instanceOf(FieldInsnNode.class))).
                get();
    }
}
