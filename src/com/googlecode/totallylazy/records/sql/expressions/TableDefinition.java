package com.googlecode.totallylazy.records.sql.expressions;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.records.Keyword;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.sql.expressions.Expressions.textOnly;
import static java.lang.String.format;

public class TableDefinition extends TextOnlyExpression {
    public TableDefinition(Keyword recordName, Keyword<?>[] fields) {
        super(format("create table %s (%s)", recordName, sequence(fields).map(asColumn())));
    }

    public static TableDefinition tableDefinition(Keyword recordName, Keyword<?>[] fields) {
        return new TableDefinition(recordName, fields);
    }

    public static final Map<Class, String> mappings = new HashMap<Class, String>() {{
        put(Date.class, "timestamp");
        put(Integer.class, "integer");
        put(Long.class, "bigint");
        put(String.class, "varchar(4000)");
        put(Timestamp.class, "timestamp");
        put(URI.class, "varchar(4000)");
        put(Boolean.class, "bit");
        put(UUID.class, "varchar(36)");
    }};

    public static Callable1<? super Keyword<?>, String> asColumn() {
        return new Callable1<Keyword<?>, String>() {
            public String call(Keyword<?> keyword) throws Exception {
                Class<?> aClass = keyword.forClass();
                if (!mappings.containsKey(aClass)) {
                    throw new UnsupportedOperationException("Unsupported SQL data type" + aClass.getName());
                }
                return format("%s %s", keyword, mappings.get(aClass));
            }
        };
    }

    public static CompoundExpression dropTable(Keyword recordName) {
        return textOnly("drop table").join(textOnly(recordName));
    }
}
