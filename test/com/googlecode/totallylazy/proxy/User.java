package com.googlecode.totallylazy.proxy;

public class User {
    public final String firstName;
    public final String lastName;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String say(String value){
        return firstName + " says '" + value + "'";
    }

    public static User user(String firstName, String lastName) {
        return new User(firstName, lastName);
    }
}
