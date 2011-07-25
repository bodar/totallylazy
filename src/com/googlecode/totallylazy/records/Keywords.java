package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable1;

public class Keywords {
    public static Callable1<Keyword, String> name() {
        return new Callable1<Keyword, String>() {
            public String call(Keyword keyword) throws Exception {
                return keyword.name();
            }
        };
    }

}
