package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class MapsTest {
    @Test
    public void supportsConvertingPairsToMapEntries() throws Exception {
        Map<String, Integer> map = new AbstractMap<String, Integer>() {
            @Override
            public Set<Entry<String, Integer>> entrySet() {
                return set(sequence(pair("dan", 1), pair("matt", 2)).map(Maps.<String, Integer>pairToEntry()));
            }
        };

        assertThat(map, hasEntry("dan", 1));
        assertThat(map, hasEntry("matt", 2));
    }

    @Test
    public void supportsConvertingMapEntriesToPairs() throws Exception {
        Map<String, Integer> map = Maps.map();
        map.put("Dan", 2);

        assertThat(sequence(map.entrySet()).map(Maps.<String, Integer>entryToPair()), hasExactly(pair("Dan", 2)));
    }

    @Test
    public void supportsConvertingASequenceOfPairsToAMap() throws Exception {
        Map<String, Integer> map = sequence(pair("dan", 1), pair("matt", 2)).fold(Maps.<String, Integer>map(), Maps.<String, Integer>asMap());
        assertThat(map, hasEntry("dan", 1));
        assertThat(map, hasEntry("matt", 2));
    }

    @Test
    public void supportsConvertingASequenceOfPairToAMultiValuedMap() throws Exception {
        Map<String, List<Integer>> map = sequence(pair("dan", 1), pair("dan", 2)).fold(Maps.<String, List<Integer>>map(), Maps.<String, Integer>asMultiValuedMap());
        assertThat(map.get("dan"), hasExactly(1, 2));
    }
}
