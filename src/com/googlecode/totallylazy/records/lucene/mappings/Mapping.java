package com.googlecode.totallylazy.records.lucene.mappings;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.records.Keyword;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.search.Query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapping<T> {
    Fieldable toField(String name, T value);

    T toValue(Fieldable fieldable);

    Query equalTo(String name, T value);
}
