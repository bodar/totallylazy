package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.collections.Keyword;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Maps.map;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.json.Json.json;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class JsonRecordTest {
    static class User extends JsonRecord {
        String name;
        Number age;
    }

    @Test
    public void canCreateARecordFromAMap() throws Exception {
        User user = JsonRecord.create(User.class, map("name", "Dan", "age", 1));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(1));
    }

    @Test
    public void canParseARecordFromJson() throws Exception {
        User user = JsonRecord.parse(User.class, json(map("name", "Dan", "age", 1)));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(new BigDecimal(1)));
    }

    @Test
    public void canConvertToJsonAndBack() throws Exception {
        User user = new User() {{ name = "Dan"; age = new BigDecimal(1); }};
        String json = user.toString();
        assertThat(json, is( "{\"name\":\"Dan\",\"age\":1}"));
        User parsed = JsonRecord.parse(User.class, json);
        assertThat(parsed.name, is(user.name));
        assertThat(parsed.age, is(user.age));
    }

    @Test
    public void jsonRecordsAreMaps() throws Exception {
        User user = new User() {{ name = "Dan"; age = new BigDecimal(1); }};
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
        assertThat(json, is( "{\"user\":{\"name\":\"Dan\",\"age\":1}}"));

        UserDocument parsed = JsonRecord.parse(UserDocument.class, json);
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
        assertThat(json, is( "{\"users\":[{\"name\":\"Dan\",\"age\":1}]}"));

        Users parsed = JsonRecord.parse(Users.class, json);
        assertThat(parsed.users.size(), is(doc.users.size()));
        assertThat(parsed.users.get(0).name, is(doc.users.get(0).name));
        assertThat(parsed.users.get(0).age, is(doc.users.get(0).age));
    }

    @Test
    public void preservesUnknownAttributes() throws Exception {
        User user = JsonRecord.create(User.class, map("name", "Dan", "age", new BigDecimal(1), "tel", "12345678890"));
        assertThat(user.name, is("Dan"));
        assertThat(user.age, is(new BigDecimal(1)));
        assertThat(user.get("tel"), is("12345678890"));
        String json = user.toString();
        assertThat(json, is( "{\"name\":\"Dan\",\"age\":1,\"tel\":\"12345678890\"}"));
        User parsed = JsonRecord.parse(User.class, json);
        assertThat(parsed.name, is(user.name));
        assertThat(parsed.age, is(user.age));
        assertThat(parsed.get("tel"), is("12345678890"));
    }

}