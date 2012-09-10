package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Lens.lens;
import static com.googlecode.totallylazy.numbers.Numbers.increment;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class LensTest {
    final Person originalBarry = new Person("Barry", 40, new Address("123 Elm St", new Postcode("E1", "1AB")));

    @Test
    public void getValueViewedThroughLens() throws Exception {
        Lens<Person, Number> personAge = lens(Person.functions.age, Person.functions.setAge);

        assertThat(personAge.get(originalBarry).intValue(), is(40));
    }

    @Test
    public void setValueViewedThroughLens() throws Exception {
        Lens<Person, Number> personAge = lens(Person.functions.age, Person.functions.setAge);

        Person olderBarry = personAge.set(originalBarry, 45);

        assertThat(olderBarry.age.intValue(), is(45));
    }

    @Test
    public void modifyValueViewThroughLens() throws Exception {
        Lens<Person, Number> personAge = lens(Person.functions.age, Person.functions.setAge);

        Person oneYearOlderBarry = personAge.modify(originalBarry, increment());

        assertThat(oneYearOlderBarry.age.intValue(), is(41));

    }

    @Test
    public void lensCanBeComposed() throws Exception {
        Lens<Person, Postcode> personPostcode = Person.lenses.address.then(Address.lenses.postcode);

        Person movedBarry = personPostcode.set(originalBarry, new Postcode("SE1", "1AA"));

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
            public static final Lens<Person,Address> address = lens(functions.address, functions.setAddress);
        }

        public static class functions {
            public static final Function1<Person, Number> age = new Function1<Person, Number>() {
                public Number call(Person person) throws Exception {
                    return person.age;
                }
            };
            public static final Function2<Person, Number, Person> setAge = new Function2<Person, Number, Person>() {
                public Person call(Person person, Number newAge) throws Exception {
                    return new Person(person.name, newAge, person.address);
                }
            };
            public static final Function1<Person, Address> address = new Function1<Person, Address>() {
                public Address call(Person person) throws Exception {
                    return person.address;
                }
            };
            public static final Function2<Person, Address, Person> setAddress = new Function2<Person, Address, Person>() {
                public Person call(Person person, Address newAddress) throws Exception {
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
            public static final Lens<Address,Postcode> postcode = lens(functions.postcode, functions.setPostcode);
        }

        public static class functions {
            public static final Function1<Address, Postcode> postcode = new Function1<Address, Postcode>() {
                public Postcode call(Address address) throws Exception {
                    return address.postcode;
                }
            };
            public static final Function2<Address, Postcode, Address> setPostcode = new Function2<Address, Postcode, Address>() {
                public Address call(Address address, Postcode newPostcode) throws Exception {
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

