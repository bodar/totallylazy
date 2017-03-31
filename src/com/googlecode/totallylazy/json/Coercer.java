package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.reflection.Reflection;
import com.googlecode.totallylazy.reflection.Types;
import com.googlecode.totallylazy.time.Dates;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

interface Coercer {
    static Object coerce(Type targetType, Object value) {
        if (targetType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) targetType;
            if (List.class.isAssignableFrom(Types.classOf(parameterizedType))) {
                Type valueType = parameterizedType.getActualTypeArguments()[0];
                return sequence((List<?>) value).map(v -> coerce(valueType, v)).toList();
            }
        }

        return coerce(Types.classOf(targetType), value);
    }

    static Object coerce(Class<?> targetType, Object parsedValue) {
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
        if (targetType.isInterface() && parsedValue instanceof Map) {
            return PersistentJsonRecord.create(cast(targetType), cast(parsedValue));
        }
        if (Date.class.isAssignableFrom(targetType) && parsedValue instanceof String) {
            return Dates.parse((String) parsedValue);
        }
        if (parsedValue != null && !parsedValue.getClass().isAssignableFrom(targetType)) {
            return Reflection.valueOf(targetType, parsedValue);
        }
        return parsedValue;
    }
}
