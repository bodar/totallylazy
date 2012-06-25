package com.googlecode.totallylazy.proxy;

public class User {
    private final String firstName;
    private final String lastName;

    User(String firstName, String lastName) {
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
