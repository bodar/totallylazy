package com.googlecode.totallylazy;

public abstract class Eq {
    @Override
    public boolean equals(Object obj) {
        return new multi(){}.<Boolean>methodOption(obj).getOrElse(this == obj);
    }
}
