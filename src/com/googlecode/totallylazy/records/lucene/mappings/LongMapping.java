package com.googlecode.totallylazy.records.lucene.mappings;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;

public class LongMapping implements Mapping<Long> {
    public Fieldable toField(String name, Long value) {
        return new NumericField(name, Field.Store.YES, true).setLongValue(value);
    }

    public Long toValue(Fieldable fieldable) {
        return ((NumericField) fieldable).getNumericValue().longValue();
    }

    public Query equalTo(String name, Long value) {
        return NumericRangeQuery.newLongRange(name, value, value, true, true);
    }

    public Query greaterThan(String name, Long value) {
        return NumericRangeQuery.newLongRange(name, value, null, false, true);
    }
}
