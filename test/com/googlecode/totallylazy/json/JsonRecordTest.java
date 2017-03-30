package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Assert;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.collections.Keyword;
import com.googlecode.totallylazy.time.Dates;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Assert.fail;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.json.Json.json;
import static com.googlecode.totallylazy.json.JsonRecord.create;
import static com.googlecode.totallylazy.json.JsonRecord.parse;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.nullValue;
import static com.googlecode.totallylazy.time.Dates.date;

public class JsonRecordTest {
    static class User extends JsonRecord {
        String name;
        BigDecimal age;
    }

    @Test
    public void canCreateARecordFromAMap() throws Exception {
        User user = create(User.class, map("name", "Dan", "age", new BigDecimal(1)));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(new BigDecimal(1)));
    }

    @Test
    public void canParseARecordFromJson() throws Exception {
        User user = parse(User.class, json(map("name", "Dan", "age", 1)));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(new BigDecimal(1)));
    }

    @Test
    public void canConvertToJsonAndBack() throws Exception {
        User user = new User() {{
            name = "Dan";
            age = new BigDecimal(1);
        }};
        String json = user.toString();
        assertThat(json, is("{\"name\":\"Dan\",\"age\":1}"));
        User parsed = parse(User.class, json);
        assertThat(parsed.name, is(user.name));
        assertThat(parsed.age, is(user.age));
    }

    @Test
    public void jsonRecordsAreMaps() throws Exception {
        User user = new User() {{
            name = "Dan";
            age = new BigDecimal(1);
        }};
        Keyword<String> name = Keyword.keyword();
        assertThat(name.call(user), is("Dan"));
    }

    static class UserDocument extends JsonRecord {
        User user;
    }

    @Test
    public void supportsNestedRecords() throws Exception {
        UserDocument doc = new UserDocument() {{
            user = new User() {{
                name = "Dan";
                age = new BigDecimal(1);
            }};
        }};

        String json = doc.toString();
        assertThat(json, is("{\"user\":{\"name\":\"Dan\",\"age\":1}}"));

        UserDocument parsed = parse(UserDocument.class, json);
        assertThat(parsed.user, is(doc.user));
    }

    static class Users extends JsonRecord {
        List<User> users;
    }

    @Test
    public void supportsListsOfRecords() throws Exception {
        Users doc = new Users() {{
            users = list(new User() {{
                name = "Dan";
                age = new BigDecimal(1);
            }});
        }};

        String json = doc.toString();
        assertThat(json, is("{\"users\":[{\"name\":\"Dan\",\"age\":1}]}"));

        Users parsed = parse(Users.class, json);
        assertThat(parsed.users.size(), is(doc.users.size()));
        assertThat(parsed.users.get(0).name, is(doc.users.get(0).name));
        assertThat(parsed.users.get(0).age, is(doc.users.get(0).age));
    }

    @Test
    public void preservesUnknownAttributes() throws Exception {
        User user = create(User.class, map("name", "Dan", "age", new BigDecimal(1), "tel", "12345678890"));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(new BigDecimal(1)));
        assertThat(user.get("tel"), is("12345678890"));
        String json = user.toString();
        assertThat(json, is("{\"name\":\"Dan\",\"age\":1,\"tel\":\"12345678890\"}"));
        User parsed = parse(User.class, json);
        assertThat(parsed.name, is(user.name));
        assertThat(parsed.age, is(user.age));
        assertThat(parsed.get("tel"), is("12345678890"));
    }

    static class IntUser extends JsonRecord {
        int age;
    }

    static class IntegerUser extends JsonRecord {
        Integer age;
    }

    @Test
    public void canCoerceIntegerTypes() throws Exception {
        assertThat(parse(IntUser.class, "{\"age\":1}").age, is(1));
        assertThat(parse(IntegerUser.class, "{\"age\":1}").age, is(1));
        assertThat(parse(IntegerUser.class, "{\"age\":null}").age, nullValue());
    }

    static class longUser extends JsonRecord {
        long age;
    }

    static class LongUser extends JsonRecord {
        Long age;
    }

    @Test
    public void canCoerceLongsTypes() throws Exception {
        assertThat(parse(longUser.class, "{\"age\":1}").age, is(1L));
        assertThat(parse(LongUser.class, "{\"age\":1}").age, is(1L));
        assertThat(parse(LongUser.class, "{\"age\":null}").age, nullValue());
    }

    enum Position {
        Long,
    }

    static class Trade extends JsonRecord {
        Position position;
    }

    @Test
    public void supportsEnums() throws Exception {
        assertThat(parse(Trade.class, "{\"position\":\"Long\"}").position, is(Position.Long));
    }

    enum CustomPosition {
        Short;

        static CustomPosition customPosition(String value) {
            if (value.equalsIgnoreCase("short")) return CustomPosition.Short;
            throw new UnsupportedOperationException();
        }
    }

    static class CustomTrade extends JsonRecord {
        CustomPosition position;
    }

    @Test
    public void supportsCustomEnumsFactoryMethods() throws Exception {
        assertThat(parse(CustomTrade.class, "{\"position\":\"short\"}").position, is(CustomPosition.Short));
    }

    interface Breed extends Value<String> {
        static Breed breed(String value) {
            return () -> value;
        }
    }

    static class Cat extends JsonRecord {
        Breed breed;
    }

    @Test
    public void supportsSimpleTypes() throws Exception {
        assertThat(parse(Cat.class, "{\"breed\":\"Tabby\"}").breed.value(), is("Tabby"));
    }

    @Test
    public void throwsUsefulErrorsWhenUnableToCoerceIntoType() throws Exception {
        try {
            parse(Cat.class, "{\"breed\":1}");
            fail("Expected exception");
        }
        catch (NoSuchElementException e) {
            Assert.assertThat(e.getMessage(), (s) -> s.matches(".*Breed.*BigDecimal"));
        }
    }

    interface Count extends Value<Integer> {
        static Count count(Number value) {
            return value::intValue;
        }
    }

    static class Crowd extends JsonRecord {
        Count count;
    }

    @Test
    public void supportsConstructorMethodsThatTakeSuperclasses() throws Exception {
        // 1 is a BigDecimal, but Count only has constructor method for Number
        String someJson = "{\"count\":1}";
        assertThat(parse(Crowd.class, someJson).count.value(), is(1));
    }

    static class Outer {
        private static class Inner extends JsonRecord {
        }
    }

    @Test
    public void canCreatePrivateJsonRecord() throws Exception {
        JsonRecord.create(Outer.Inner.class, map());
    }

    static class Account extends JsonRecord {
        public Date created_at;
    }

    @Test
    public void supportsDates() throws Exception {
        Date date = date(2001, 1, 1);
        assertThat(parse(Account.class, "{\"created_at\":\"" + Dates.RFC3339withMilliseconds().format(date) + "\"}").created_at, is(date));
    }

    static class Ledger extends JsonRecord {
        public List<Date> times;
    }

    @Test
    public void supportsListsOfCoerceables() throws Exception {
        Date date = date(2001, 1, 1);
        assertThat(parse(Ledger.class, "{\"times\":[\"" + Dates.RFC3339withMilliseconds().format(date) + "\"]}").times.get(0), is(date));

    }
}