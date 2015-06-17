package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Lense.lense;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LenseTest {
    final Person originalBarry = new Person("Barry", 40, new Address("123 Elm St", new Postcode("E1", "1AB")));

    @Test
    public void getValueViewedThroughLens() throws Exception {
        Lense<Person, Number> personAge = lense(Person.functions.age, Person.functions.setAge);

        assertThat(personAge.get(originalBarry).intValue(), is(40));
    }

    @Test
    public void setValueViewedThroughLens() throws Exception {
        Lense<Person, Number> personAge = lense(Person.functions.age, Person.functions.setAge);

        Person olderBarry = personAge.set(45, originalBarry);
        assertThat(olderBarry.age.intValue(), is(45));
    }

    @Test
    public void modifyValueViewThroughLens() throws Exception {
        Lense<Person, Number> personAge = lense(Person.functions.age, Person.functions.setAge);

        Person oneYearOlderBarry = personAge.modify(originalBarry, increment());

        assertThat(oneYearOlderBarry.age.intValue(), is(41));

    }

    @Test
    public void lensCanBeComposed() throws Exception {
        Lense<Person, Postcode> personPostcode = Person.lenses.address.then(Address.lenses.postcode);

        Person movedBarry = personPostcode.set(new Postcode("SE1", "1AA"), originalBarry);

        assertThat(movedBarry.address.postcode.asString(), is("SE1 1AA"));
    }

    private static class Person {
        private final String name;
        private final Number age;
        private final Address address;

        Person(String name, Number age, Address address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }

        public String toString() {
            return name + ": age=" + age + ", address=" + address;
        }


        public static class lenses {
            public static final Lense<Person,Address> address = lense(functions.address, functions.setAddress);
        }

        public static class functions {
            public static final Function1<Person, Number> age = new Function1<Person, Number>() {
                public Number call(Person person) throws Exception {
                    return person.age;
                }
            };
            public static final Curried2<Number, Person, Person> setAge = new Curried2<Number, Person, Person>() {
                public Person call(Number newAge, Person person) throws Exception {
                    return new Person(person.name, newAge, person.address);
                }
            };
            public static final Function1<Person, Address> address = new Function1<Person, Address>() {
                public Address call(Person person) throws Exception {
                    return person.address;
                }
            };
            public static final Curried2<Address, Person, Person> setAddress = new Curried2<Address, Person, Person>() {
                public Person call(Address newAddress, Person person) throws Exception {
                    return new Person(person.name, person.age, newAddress);
                }
            };
        }
    }

    static class Address {
        private final String street;
        private final Postcode postcode;

        Address(String street, Postcode postcode) {
            this.street = street;
            this.postcode = postcode;
        }

        public String toString() {
            return street + " " + postcode;
        }

        public static class lenses {
            public static final Lense<Address,Postcode> postcode = lense(functions.postcode, functions.setPostcode);
        }

        public static class functions {
            public static final Function1<Address, Postcode> postcode = new Function1<Address, Postcode>() {
                public Postcode call(Address address) throws Exception {
                    return address.postcode;
                }
            };
            public static final Curried2<Postcode, Address, Address> setPostcode = new Curried2<Postcode, Address, Address>() {
                public Address call(Postcode newPostcode, Address address) throws Exception {
                    return new Address(address.street, newPostcode);
                }
            };
        }
    }

    class Postcode {
        private final String outward;
        private final String inward;

        Postcode(String outward, String inward) {
            this.outward = outward;
            this.inward = inward;
        }

        public String asString() {
            return outward + " " + inward;
        }

        public String toString() {
            return asString();
        }
    }
}