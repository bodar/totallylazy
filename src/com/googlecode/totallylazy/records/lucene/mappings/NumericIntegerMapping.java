package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.search.NumericRangeQuery;

public class NumericIntegerMapping extends AbstractMapping<Integer> {
    public Fieldable toField(String name, Integer value) {
        return new NumericField(name, Field.Store.YES, true).setIntValue(value);
    }

    public Integer toValue(Fieldable fieldable) {
        return ((NumericField) fieldable).getNumericValue().intValue();
    }

    @Override
    protected NumericRangeQuery<Integer> newRange(final String name, final Integer lower, final Integer upper, final boolean minInclusive, final boolean maxInclusive) {
        return NumericRangeQuery.newIntRange(name, lower, upper, minInclusive, maxInclusive);
    }
}
