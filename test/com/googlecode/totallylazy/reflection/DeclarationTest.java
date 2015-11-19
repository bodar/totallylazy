package com.googlecode.totallylazy.reflection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class DeclarationTest {
    private List<String> aField;
    private Declaration declaration;

    @Test
    public void worksWithFields() throws Exception {
        aField = useDeclaration(ArrayList::new);
        assertThat(declaration.name(), is("aField"));
        assertThat(declaration.type(), is(new TypeFor<List<String>>() {}.get()));
    }

    @Test
    public void worksWithLocalVaiables() throws Exception {
        List<String> aLocalVariable = useDeclaration(ArrayList::new);
        assertThat(declaration.name(), is("aLocalVariable"));
        assertThat(declaration.type(), is(new TypeFor<List<String>>() {}.get()));
    }

    @Test
    public void worksEvenIfAnotherMethodIsBetween() throws Exception {
        List<String> aLocalVariable = concreteReturnType();
        assertThat(declaration.name(), is("aLocalVariable"));
        assertThat(declaration.type(), is(new TypeFor<List<String>>() {}.get()));
    }

    @Test
    public void canGetTypeEvenIfAnonymous() throws Exception {
        concreteReturnType();
        assertThat(declaration.name(), is("{anonymous}"));
        assertThat(declaration.type(), is(new TypeFor<List<String>>() {}.get()));
    }

    public List<String> concreteReturnType() throws Exception {
        return useDeclaration(ArrayList::new);
    }

    public <T> T useDeclaration(Callable<T> callable) throws Exception {
        declaration = Declaration.declaration();
        return callable.call();
    }


}