package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.Count.count;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.empty;
import static com.googlecode.totallylazy.collections.TreeList.treeList;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class TreeListTest {
    @Test
    public void supportsHashcode() throws Exception {
        assertThat(treeList("Dan", "Matt").hashCode(), is(treeList("Dan", "Matt").hashCode()));
    }

    @Test
    public void supportsEquality() throws Exception {
        assertThat(treeList("Dan", "Matt"), is(treeList("Dan", "Matt")));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(treeList("Dan", "Matt").toString(), is("(Dan,Matt)"));
    }

    @Test
    public void canFold() throws Exception {
        assertThat(treeList("Dan", "Matt").fold(0, count()).intValue(), is(2));
    }

    @Test
    public void canConsAnElementOntoTheHead() throws Exception {
        assertThat(TreeList.<String>treeList().cons("Dan").cons("Matt"), hasExactly("Matt", "Dan"));
    }

    @Test
    public void canLookupByIndex() throws Exception {
        assertThat(TreeList.<String>treeList().cons("Dan").cons("Matt").get(0), is("Matt"));
    }

    @Test
    public void canAddAnElementOntoTheEnd() throws Exception {
        assertThat(TreeList.<String>treeList().append("Dan").append("Matt"), hasExactly("Dan", "Matt"));
    }

    @Test
    public void supportsEasyConstruction() throws Exception {
        assertThat(treeList(sequence("Dan", "Matt")), hasExactly("Dan", "Matt"));
        assertThat(treeList("Dan"), hasExactly("Dan"));
        assertThat(treeList("Dan", "Matt"), hasExactly("Dan", "Matt"));
        assertThat(treeList("Dan", "Matt", "Raymond"), hasExactly("Dan", "Matt", "Raymond"));
        assertThat(treeList("Dan", "Matt", "Raymond", "Tom"), hasExactly("Dan", "Matt", "Raymond", "Tom"));
        assertThat(treeList("Dan", "Matt", "Raymond", "Tom", "Stu"), hasExactly("Dan", "Matt", "Raymond", "Tom", "Stu"));
    }

    @Test
    public void supportsHeadOption() {
        assertThat(treeList("Dan", "Matt").headOption(), is(Option.some("Dan")));
        assertThat(empty(String.class).headOption(), is(Option.none(String.class)));
    }


    @Test
    public void supportsRemoveAll() throws Exception {
        assertThat(treeList(1, 2, 3, 4, 5, 6).deleteAll(sequence(3, 4)), hasExactly(1, 2, 5, 6));
    }
}
