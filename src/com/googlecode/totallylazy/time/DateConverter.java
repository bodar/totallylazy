package com.googlecode.totallylazy.time;

import java.util.Date;

public interface DateConverter {
    String format(Date value);

    Date parse(String value);
}
