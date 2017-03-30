package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.reflection.Fields;
import com.googlecode.totallylazy.reflection.Reflection;
import com.googlecode.totallylazy.reflection.Types;
import com.googlecode.totallylazy.time.Dates;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Classes.allClasses;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sets.set;
import static com.googlecode.totallylazy.Sets.union;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.predicates.Predicates.and;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.where;
import static com.googlecode.totallylazy.reflection.Fields.modifiers;
import static com.googlecode.totallylazy.reflection.Reflection.synthetic;
import static com.googlecode.totallylazy.reflection.Types.matches;
import static java.util.Collections.unmodifiableSet;

public abstract class JsonRecord extends AbstractMap<String, Object> {
    private final Map<String, Object> _otherFields = new HashMap<>();

    public static <T extends JsonRecord> T parse(Class<T> recordType, String data) {
        return create(recordType, Json.map(data));
    }

    public static <T extends JsonRecord> T create(Class<T> recordType, Map<String, Object> data) {
        try {
            T instance = Reflection.newInstance(recordType);
            for (Entry<String, Object> entry : data.entrySet()) {
                instance.put(entry.getKey(), entry.getValue());
            }
            return instance;
        } catch (Exception e) {
            throw lazyException(e);
        }
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(union(fieldSet(), _otherFields.entrySet()));
    }

    private Set<Entry<String, Object>> fieldSet() {
        Sequence<Field> fields = fields();
        return set(fields.
                reject(where(modifiers, synthetic)).
                map(f -> pair(f.getName(), Fields.get(f, this))));
    }

    private Sequence<Field> fields() {
        Sequence<Class<?>> classes = allClasses(getClass()).
                reject(Class::isInterface).
                takeWhile(c -> !c.equals(JsonRecord.class));
        return classes.flatMap(Fields.fields());
    }

    @Override
    public Object get(Object key) {
        return field(key.toString()).
                map(Fields.value(this)).
                getOrElse(() -> _otherFields.get(key));
    }

    private Option<Field> field(String name) {
        return fields().
                find(f -> f.getName().equalsIgnoreCase(name)).
                map(Fields::access);
    }

    @Override
    public Object put(String key, Object value) {
        try {
            Option<Field> field = field(key);
            if (field.isDefined()) {
                Field actual = field.get();
                actual.set(this, coerce(actual.getGenericType(), value));
                return Fields.get(actual, this);
            } else {
                return _otherFields.put(key, value);
            }
        } catch (IllegalAccessException e) {
            throw lazyException(e);
        }
    }

    private Object coerce(Type targetType, Object value) {
        if (targetType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) targetType;
            if (List.class.isAssignableFrom(Types.classOf(parameterizedType))) {
                Type valueType = parameterizedType.getActualTypeArguments()[0];
                Class<Object> valueCLass = Types.classOf(valueType);
                return sequence((List<?>) value).map(v -> coerce(valueType, v)).toList();
            }
        }

        return coerce(Types.classOf(targetType), value);
    }

    private Object coerce(Class<?> targetType, Object parsedValue) {
        if (int.class.isAssignableFrom(targetType) || Integer.class.isAssignableFrom(targetType)) {
            BigDecimal number = (BigDecimal) parsedValue;
            return number == null ? null : number.intValue();
        }
        if (long.class.isAssignableFrom(targetType) || Long.class.isAssignableFrom(targetType)) {
            BigDecimal number = (BigDecimal) parsedValue;
            return number == null ? null : number.longValue();
        }
        if (JsonRecord.class.isAssignableFrom(targetType) && parsedValue instanceof Map) {
            return JsonRecord.create(cast(targetType), cast(parsedValue));
        }
        if (Date.class.isAssignableFrom(targetType) && parsedValue instanceof String) {
            return Dates.parse((String) parsedValue);
        }
        if (parsedValue != null && !parsedValue.getClass().isAssignableFrom(targetType)) {
            return Reflection.valueOf(targetType, parsedValue);
        }
        return parsedValue;
    }

    @Override
    public String toString() {
        return Json.json(this);
    }
}