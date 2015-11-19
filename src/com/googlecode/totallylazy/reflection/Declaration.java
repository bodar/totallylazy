package com.googlecode.totallylazy.reflection;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.predicates.Predicates;
import com.googlecode.totallylazy.Sequence;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.AbstractInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldInsnNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.LocalVariableNode;
import jdk.internal.org.objectweb.asm.tree.MethodInsnNode;
import jdk.internal.org.objectweb.asm.tree.TypeInsnNode;
import jdk.internal.org.objectweb.asm.tree.VarInsnNode;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
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
        return declaration(StackFrames.stackFrames().tail()).get();
    }

    private static Option<Declaration> declaration(Sequence<StackFrame> stackFrame) {
        StackFrame callee = stackFrame.first();
        StackFrame caller = stackFrame.second();
        Sequence<AbstractInsnNode> instructions = caller.instructions().dropWhile(not(Predicates.instanceOf(MethodInsnNode.class,
                insn -> insn.owner.equals(callee.classNode().name) &&
                        insn.desc.equals(callee.methodNode().desc) &&
                        insn.name.equals(callee.methodNode().name)))).tail().realise();
        AbstractInsnNode nextInstruction = instructions.reject(Predicates.instanceOf(TypeInsnNode.class)).head();

        if(nextInstruction instanceof VarInsnNode){
            VarInsnNode node = (VarInsnNode) nextInstruction;
            LocalVariableNode variable = Asm.localVariables(caller.methodNode()).filter(v -> v.index == node.var).head();
            return some(new Declaration(variable.name, Signature.parse(variable.signature == null ? variable.desc : variable.signature, callee.aClass())));
        }
        if(nextInstruction instanceof FieldInsnNode) {
            FieldInsnNode node = (FieldInsnNode) nextInstruction;
            FieldNode field = Asm.fields(caller.classNode()).filter(f -> f.name.equals(node.name)).head();
            return some(new Declaration(field.name, Signature.parse(field.signature == null ? field.desc : field.signature, callee.aClass())));
        }
        if(nextInstruction instanceof InsnNode){
            InsnNode node = (InsnNode) nextInstruction;
            if(node.getOpcode() == Opcodes.ARETURN){
                return declaration(stackFrame.tail()).orElse(() -> some(new Declaration("{anonymous}", caller.method().getGenericReturnType())));
            }
        }
        return none();
    }
}
