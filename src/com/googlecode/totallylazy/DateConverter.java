package com.googlecode.totallylazy;

import java.util.Date;

public interface DateConverter {
    String toString(Date value);

    Date toDate(String value);
}
