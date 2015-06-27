package com.googlecode.totallylazy;

import com.googlecode.totallylazy.predicates.Predicates;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Maps.filterKeys;
import static com.googlecode.totallylazy.Maps.filterValues;
import static com.googlecode.totallylazy.Maps.find;
import static com.googlecode.totallylazy.Maps.get;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.Maps.mapKeys;
import static com.googlecode.totallylazy.Maps.mapValues;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Strings.contains;
import static com.googlecode.totallylazy.Strings.toLowerCase;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;

public class MapsTest {
    @Test
    public void supportsGettingAValueAsAnOption() throws Exception {
        assertThat(get(map(pair("Dan", 2)), "Dan"), is(some(2)));
        assertThat(get(map(pair("Dan", 2)), "Matt"), is(none(Integer.class)));
    }

    @Test
    public void supportsFindingAValueAsAnOption() throws Exception {
        assertThat(find(map(pair("Dan", 2)), contains("a")), is(some(2)));
        assertThat(find(map(pair("Dan", 2)), contains("b")), is(none(Integer.class)));
    }

    @Test
    public void supportsFilteringByKey() throws Exception {
        assertThat(filterKeys(map(pair("Dan", 2)), contains("a")), is(map(pair("Dan", 2))));
        assertThat(filterKeys(map(pair("Dan", 2)), contains("b")), is(Maps.<String, Integer>map()));
    }

    @Test
    public void supportsFilteringByValue() throws Exception {
        assertThat(filterValues(map(pair("Dan", 2)), Predicates.is(2)), is(map(pair("Dan", 2))));
        assertThat(filterValues(map(pair("Dan", 2)), Predicates.is(3)), is(Maps.<String, Integer>map()));
    }

    @Test
    public void supportsMappingKeys() throws Exception {
        assertThat(mapKeys(map(pair("Dan", 2)), toLowerCase()), is(map(pair("dan", 2))));
    }

    @Test
    public void supportsMappingValues() throws Exception {
        assertThat(mapValues(map(pair("Dan", 2)), add(2)), is(map(pair("Dan", (Number)4))));
    }

    @Test
    public void maintainsMapEntriesOrder() throws Exception {
        Map<String, String> map = map(Pair.pair("name", "Dan"), Pair.pair("tel", "123432"));
        assertThat(map.keySet().iterator().next(), is("name"));
    }

    @Test
    public void supportsMultiValuedMapCreation() throws Exception {
        Map<String, List<String>> map = Maps.multiMap(Pair.pair("name", "Dan"), Pair.pair("name", "Mat"));
        List<String> values = map.entrySet().iterator().next().getValue();
        assertThat(values, hasExactly("Dan", "Mat"));
    }

    @Test
    public void supportsConvertingPairsToMapEntries() throws Exception {
        Map<String, Integer> map = new AbstractMap<String, Integer>() {
            @Override
            public Set<Entry<String, Integer>> entrySet() {
                return set(Sequences.<Entry<String, Integer>>sequence(pair("dan", 1), pair("matt", 2)));
            }
        };

        assertThat(map, hasEntry("dan", 1));
        assertThat(map, hasEntry("matt", 2));
    }

    @Test
    public void supportsConvertingMapEntriesToPairs() throws Exception {
        Map<String, Integer> map = map();
        map.put("Dan", 2);

        assertThat(sequence(Maps.pairs(map)), hasExactly(pair("Dan", 2)));
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
