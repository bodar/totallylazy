package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.time.Dates;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Assert.fail;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.json.Json.json;
import static com.googlecode.totallylazy.json.PersistentJsonRecord.create;
import static com.googlecode.totallylazy.json.PersistentJsonRecord.map;
import static com.googlecode.totallylazy.json.PersistentJsonRecord.modify;
import static com.googlecode.totallylazy.json.PersistentJsonRecord.parse;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.nullValue;
import static com.googlecode.totallylazy.time.Dates.date;

public class PersistentJsonRecordTest {
    @Test
    public void doesNotSerialiseNullValues() throws Exception {
        assertThat(json(map(new User() {
            public String name() { return null; }
            public int age() { return 0; }
        })), is("{\"age\":0}"));
    }

    interface User {
        String name();
        int age();
    }

    @Test
    public void canCreateARecordFromAMap() throws Exception {
        User user = create(User.class, map("name", "Dan"));
        assertThat(user.name(), is("Dan"));
    }

    @Test
    public void canParseARecordFromJson() throws Exception {
        User user = parse(User.class, json(map("name", "Dan")));
        assertThat(user.name(), is("Dan"));
    }

    @Test
    public void canConvertToJsonAndBack() throws Exception {
        String json = "{\"name\":\"Dan\"}";
        User parsed = parse(User.class, json);
        assertThat(parsed.name(), is("Dan"));
        assertThat(parsed.toString(), is(json));
    }

    @Test
    public void canBeConvertedToAMap() throws Exception {
        User user = new User() {
            public String name() { return "Dan"; }
            public int age() { return 12; }
        };
        PersistentMap<String, Object> map = map(user);
        assertThat(map.get("name"), is("Dan"));
    }

    @Test
    public void canPersistNewValues() throws Exception {
        User dan = create(User.class, map("name", "Dan"));
        User matt = modify(dan, User::name, "Matt");
        assertThat(dan.name(), is("Dan"));
        assertThat(matt.name(), is("Matt"));
    }

    interface UserDocument {
        User user();
    }

    @Test
    public void supportsNestedRecords() throws Exception {
        UserDocument parsed = parse(UserDocument.class, "{\"user\":{\"name\":\"Dan\"}}");
        assertThat(parsed.user().name(), is("Dan"));
    }

    interface Users {
        List<User> users();
    }

    @Test
    public void supportsListsOfRecords() throws Exception {
        Users parsed = parse(Users.class, "{\"users\":[{\"name\":\"Dan\"}]}");
        assertThat(parsed.users().size(), is(1));
        assertThat(parsed.users().get(0).name(), is("Dan"));
    }

    @Test
    public void preservesUnknownAttributes() throws Exception {
        User user = create(User.class, map("name", "Dan","tel", "12345678890"));
        assertThat(user.name(), is("Dan"));
        assertThat(map(user).get("tel"), is("12345678890"));
        String json = user.toString();
        assertThat(json, is("{\"name\":\"Dan\",\"tel\":\"12345678890\"}"));
        User parsed = parse(User.class, json);
        assertThat(parsed.name(), is(user.name()));
        assertThat(map(parsed).get("tel"), is("12345678890"));
    }

    interface IntUser {
        int age();
    }

    interface IntegerUser {
        Integer age();
    }

    @Test
    public void canCoerceIntegerTypes() throws Exception {
        assertThat(parse(IntUser.class, "{\"age\":1}").age(), is(1));
        assertThat(parse(IntegerUser.class, "{\"age\":1}").age(), is(1));
        assertThat(parse(IntegerUser.class, "{\"age\":null}").age(), nullValue());
    }

    interface longUser {
        long age();
    }

    interface LongUser {
        Long age();
    }

    @Test
    public void canCoerceLongsTypes() throws Exception {
        assertThat(parse(longUser.class, "{\"age\":1}").age(), is(1L));
        assertThat(parse(LongUser.class, "{\"age\":1}").age(), is(1L));
        assertThat(parse(LongUser.class, "{\"age\":null}").age(), nullValue());
    }

    enum Position {
        Long,
    }

    interface Trade {
        Position position();
    }

    @Test
    public void supportsEnums() throws Exception {
        assertThat(parse(Trade.class, "{\"position\":\"Long\"}").position(), is(Position.Long));
    }

    enum CustomPosition {
        Short;

        static CustomPosition customPosition(String value) {
            if (value.equalsIgnoreCase("short")) return CustomPosition.Short;
            throw new UnsupportedOperationException();
        }
    }

    interface CustomTrade {
        CustomPosition position();
    }

    @Test
    public void supportsCustomEnumsFactoryMethods() throws Exception {
        assertThat(parse(CustomTrade.class, "{\"position\":\"short\"}").position(), is(CustomPosition.Short));
    }

    interface Breed extends Value<String> {
        static Breed breed(String value) {
            return () -> value;
        }
    }

    interface Cat {
        Breed breed();
    }

    @Test
    public void supportsSimpleTypes() throws Exception {
        assertThat(parse(Cat.class, "{\"breed\":\"Tabby\"}").breed().value(), is("Tabby"));
    }

    @Test
    public void throwsUsefulErrorsWhenUnableToCoerceIntoType() throws Exception {
        try {
            parse(Cat.class, "{\"breed\":1}");
            fail("Expected exception");
        }
        catch (NoSuchElementException e) {
            assertThat(e.getMessage(), (s) -> s.matches(".*Breed.*BigDecimal"));
        }
    }

    interface Count extends Value<Integer> {
        static Count count(Number value) {
            return value::intValue;
        }
    }

    interface Crowd {
        Count count();
    }

    @Test
    public void supportsConstructorMethodsThatTakeSuperclasses() throws Exception {
        // 1 is a BigDecimal, but Count only has constructor method for Number
        String someJson = "{\"count\":1}";
        assertThat(parse(Crowd.class, someJson).count().value(), is(1));
    }

    interface Account {
        Date created_at();
    }

    @Test
    public void supportsDates() throws Exception {
        Date date = date(2001, 1, 1);
        assertThat(parse(Account.class, "{\"created_at\":\"" + Dates.RFC3339withMilliseconds().format(date) + "\"}").created_at(), is(date));
    }

    interface Ledger {
        List<Date> times();
    }

    @Test
    public void supportsListsOfCoerceables() throws Exception {
        Date date = date(2001, 1, 1);
        assertThat(parse(Ledger.class, "{\"times\":[\"" + Dates.RFC3339withMilliseconds().format(date) + "\"]}").times().get(0), is(date));
    }

}
