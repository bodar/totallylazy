package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.GenericType;
import com.googlecode.totallylazy.Unchecked;
import com.googlecode.totallylazy.reflection.Declaration;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

interface Keyword<T> extends Function1<Map<String, Object>, T>, GenericType<T> {
    String name();

    @Override
    default T call(Map<String, Object> map) throws Exception {
        Object value = map.get(name());
        return forClass().cast(value);
    }

    static <T> Keyword<T> keyword(String name, Class<T> aClass) {
        return new Keyword<T>() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public Class<T> forClass() {
                return aClass;
            }
        };
    }

    static <T> Keyword<T> keyword() {
        Declaration declaration = Declaration.declaration(1);
        ParameterizedType parameterizedType = (ParameterizedType) declaration.type();

        return Keyword.keyword(declaration.name(), Unchecked.<Class <T>>cast(parameterizedType.getActualTypeArguments()[0]));
    }

}
