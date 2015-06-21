package com.googlecode.totallylazy.reflection;

import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.parser.SignatureParser;
import sun.reflect.generics.scope.ClassScope;
import sun.reflect.generics.tree.TypeSignature;
import sun.reflect.generics.visitor.Reifier;

public class Signature {
    public static java.lang.reflect.Type parse(String signature) {
        return parse(signature, Signature.class);
    }

    public static java.lang.reflect.Type parse(String signature, Class<?> scope) {
        GenericsFactory factory = CoreReflectionFactory.make(scope, ClassScope.make(scope));
        Reifier reifier = Reifier.make(factory);
        TypeSignature typeSignature = SignatureParser.make().parseTypeSig(signature);
        typeSignature.accept(reifier);
        return reifier.getResult();
    }
}
