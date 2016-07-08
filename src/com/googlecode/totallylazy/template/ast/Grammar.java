package com.googlecode.totallylazy.template.ast;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Lists;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.parser.CharactersParser;
import com.googlecode.totallylazy.parser.Parser;
import com.googlecode.totallylazy.parser.Parsers;
import com.googlecode.totallylazy.parser.Result;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Characters.alphaNumeric;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.parser.Parsers.between;
import static com.googlecode.totallylazy.parser.Parsers.isChar;
import static com.googlecode.totallylazy.parser.Parsers.or;
import static com.googlecode.totallylazy.parser.Parsers.ws;
import static com.googlecode.totallylazy.parser.Parsers.wsChar;
import static com.googlecode.totallylazy.predicates.EqualsPredicate.is;

public class Grammar {
    static Grammar Default = new Grammar();

    public static Parser<List<Expression>> parser() {
        return Default.TEMPLATE;
    }

    public static Parser<List<Expression>> parser(final char delimiter) {
        return new Grammar() {
            @Override
            char DELIMITER() { return delimiter; }
        }.TEMPLATE;
    }

    char DELIMITER() { return '$'; }

    Parser<Void> SEPARATOR = wsChar(',').ignore();
    Parser<Void> SINGLE_QUOTE = isChar('\'').ignore();
    Parser<Void> DOUBLE_QUOTE = isChar('"').ignore();

    Parser<String> IDENTIFIER = CharactersParser.characters(alphaNumeric).map(new Callable1<CharSequence, String>() {
        @Override
        public String call(CharSequence charSequence) throws Exception {
            return charSequence.toString();
        }
    });

    Parser<Name> NAME = IDENTIFIER.map(new Callable1<String, Name>() {
        @Override
        public Name call(String s) throws Exception {
            return Name.name(s);
        }
    });
    Parser<Text> TEXT = textExcept(is(DELIMITER()));
    Parser<Text> SINGLE_QUOTED = between(SINGLE_QUOTE, textExcept('\''), SINGLE_QUOTE);
    Parser<Text> DOUBLE_QUOTED = between(DOUBLE_QUOTE, textExcept('"'), DOUBLE_QUOTE);
    Parser<Text> LITERAL = SINGLE_QUOTED.or(DOUBLE_QUOTED);

    static Parser<Text> textExcept(char c) {
        return textExcept(is(c));
    }

    static Parser<Text> textExcept(Predicate<Character> predicate) {
        return CharactersParser.characters(not(predicate)).map(new Callable1<CharSequence, Text>() {
            @Override
            public Text call(CharSequence charSequence) throws Exception {
                return Text.text(charSequence);
            }
        });
    }

    Parser<Expression> VALUE = Parsers.lazy(new Callable<Parser<Expression>>() {
        @Override
        public Parser<Expression> call() throws Exception {
            return ws(or(LITERAL, FUNCTION_CALL, ATTRIBUTE));
        }
    });

    Parser<Expression> EXPRESSION = Parsers.lazy(new Callable<Parser<Expression>>() {
        @Override
        public Parser<Expression> call() throws Exception {
            return Parsers.or(FUNCTION_CALL, MAPPING, ATTRIBUTE).surroundedBy(isChar(DELIMITER()));
        }
    });

    Parser<Expression> NAMES = Parsers.lazy(new Callable<Parser<Expression>>() {
        @Override
        public Parser<Expression> call() throws Exception {
            return cast(or(NAME, INDIRECTION));
        }
    });

    Parser<Attribute> ATTRIBUTE = NAMES.sepBy1(isChar('.')).map(new Callable1<List<Expression>, Attribute>() {
        @Override
        public Attribute call(List<Expression> expressions) throws Exception {
            return Attribute.attribute(expressions);
        }
    });

    Parser<Pair<String, Expression>> NAMED_ARGUMENT = IDENTIFIER.followedBy(isChar('=')).then(VALUE);

    Parser<NamedArguments> NAMED_ARGUMENTS = NAMED_ARGUMENT.sepBy1(SEPARATOR).
            map(new Callable1<List<Pair<String, Expression>>, Map<String, Expression>>() {
                @Override
                public Map<String, Expression> call(List<Pair<String, Expression>> pairs) throws Exception {
                    return Maps.map(pairs);
                }
            }).
            map(new Callable1<Map<String, Expression>, NamedArguments>() {
                @Override
                public NamedArguments call(Map<String, Expression> map) throws Exception {
                    return NamedArguments.namedArguments(map);
                }
            });

    Parser<ImplicitArguments> IMPLICIT_ARGUMENTS = VALUE.sepBy(SEPARATOR).map(new Callable1<List<Expression>, ImplicitArguments>() {
        @Override
        public ImplicitArguments call(List<Expression> expressions) throws Exception {
            return ImplicitArguments.implicitArguments(expressions);
        }
    });

    Parser<FunctionCall> FUNCTION_CALL =
            NAMES.then(between(isChar('('), or((Parser<? extends Arguments>) NAMED_ARGUMENTS, IMPLICIT_ARGUMENTS), isChar(')'))).
                    map(new Callable1<Pair<Expression, ? extends Arguments>, FunctionCall>() {
                        @Override
                        public FunctionCall call(Pair<Expression, ? extends Arguments> pair) throws Exception {
                            return FunctionCall.functionCall(pair.first(), pair.second());
                        }
                    });

    Parser<List<Expression>> TEMPLATE = or(EXPRESSION, TEXT).many1();

    Parser<List<String>> PARAMETERS_NAMES = IDENTIFIER.sepBy1(SEPARATOR);
    Parser<Anonymous> ANONYMOUS_TEMPLATE = between(wsChar('{'),
            PARAMETERS_NAMES.followedBy(wsChar('|')).optional().
                    map(new Callable1<Option<List<String>>, List<String>>() {
                        @Override
                        public List<String> call(Option<List<String>> lists) throws Exception {
                            return lists.getOrElse(Lists.<String>list());
                        }
                    }).
                    then(CharactersParser.characters(not('}')).flatMap(new Function1<CharSequence, Result<List<Expression>>>() {
                        @Override
                        public Result<List<Expression>> call(CharSequence charSequence) throws Exception {
                            return TEMPLATE.parse(charSequence);
                        }
                    })), wsChar('}')).
            map(new Callable1<Pair<List<String>, List<Expression>>, Anonymous>() {
                @Override
                public Anonymous call(Pair<List<String>, List<Expression>> pair) throws Exception {
                    return Anonymous.anonymous(pair.first(), pair.second());
                }
            });

    Parser<Mapping> MAPPING = ATTRIBUTE.followedBy(isChar(':')).then(ANONYMOUS_TEMPLATE).
            map(new Callable1<Pair<Attribute, Anonymous>, Mapping>() {
                @Override
                public Mapping call(Pair<Attribute, Anonymous> pair) throws Exception {
                    return Mapping.mapping(pair.first(), pair.second());
                }
            });

    Parser<Indirection> INDIRECTION = Parsers.or(ATTRIBUTE, ANONYMOUS_TEMPLATE, LITERAL).between(isChar('('), isChar(')')).
            map(new Callable1<Expression, Indirection>() {
                @Override
                public Indirection call(Expression expression) throws Exception {
                    return Indirection.indirection(expression);
                }
            });
}