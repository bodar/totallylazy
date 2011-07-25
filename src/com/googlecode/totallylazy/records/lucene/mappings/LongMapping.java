package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;

public class LongMapping extends AbstractMapping<Long> {
    public Fieldable toField(String name, Long value) {
        return new NumericField(name, Field.Store.YES, true).setLongValue(value);
    }

    public Long toValue(Fieldable fieldable) {
        return ((NumericField) fieldable).getNumericValue().longValue();
    }

    @Override
    protected Query newRange(String name, Long lower, Long upper, boolean minInclusive, boolean maxInclusive) throws Exception {
        return NumericRangeQuery.newLongRange(name, lower, upper, minInclusive, minInclusive);
    }
}
