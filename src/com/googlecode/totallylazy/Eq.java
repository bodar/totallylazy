package com.googlecode.totallylazy;

public abstract class Eq {
    private multi multi;
    @Override
    public boolean equals(Object obj) {
        if(multi == null) multi = new multi(){};
        return multi.<Boolean>methodOption(obj).getOrElse(this == obj);
    }
}
