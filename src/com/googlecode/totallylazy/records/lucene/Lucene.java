package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.predicates.*;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Keyword.keyword;

public class Lucene {
    public static final Keyword RECORD_KEY = keyword("type");

    public static TermQuery record(Keyword recordName) {
        return new TermQuery(new Term(RECORD_KEY.toString(), recordName.toString()));
    }

    public static Query and(Query... queries) {
        return and(sequence(queries));
    }

    public static Query and(Iterable<Query> queries) {
        return sequence(queries).fold(new BooleanQuery(), add(BooleanClause.Occur.MUST));
    }

    public static Query or(Query... queries) {
        return or(sequence(queries));
    }

    public static Query or(Iterable<Query> queries) {
        return sequence(queries).fold(new BooleanQuery(), add(BooleanClause.Occur.SHOULD));
    }

    public static Query not(Query... queries) {
        return not(sequence(queries));
    }

    public static Query not(Iterable<Query> queries) {
        return sequence(queries).fold(new BooleanQuery(), add(BooleanClause.Occur.MUST_NOT));
    }

    private static Callable2<? super BooleanQuery, ? super Query, BooleanQuery> add(final BooleanClause.Occur occur) {
        return new Callable2<BooleanQuery, Query, BooleanQuery>() {
            public BooleanQuery call(BooleanQuery booleanQuery, Query query) throws Exception {
                // FIX Lucene issue where it does not understand nested boolean negatives
                if (query instanceof BooleanQuery && occur.equals(BooleanClause.Occur.MUST)) {
                    BooleanClause[] clauses = ((BooleanQuery) query).getClauses();
                    if (clauses.length == 1 && clauses[0].getOccur().equals(BooleanClause.Occur.MUST_NOT)) {
                        booleanQuery.add(clauses[0]);
                        return booleanQuery;
                    }
                }
                booleanQuery.add(query, occur);
                return booleanQuery;
            }
        };
    }

    private final Mappings mappings;

    public Lucene(Mappings mappings) {
        this.mappings = mappings;
    }

    public Query query(Predicate<? super Record> predicate) {
        if (predicate instanceof WherePredicate) {
            return where((WherePredicate) predicate);
        }
        if (predicate instanceof AndPredicate) {
            return and(sequence(((AndPredicate) predicate).predicates()).map(asQuery()));
        }
        if (predicate instanceof OrPredicate) {
            return or(sequence(((OrPredicate) predicate).predicates()).map(asQuery()));
        }
        throw new UnsupportedOperationException();
    }


    private Callable1<? super Predicate, Query> asQuery() {
        return new Callable1<Predicate, Query>() {
            public Query call(Predicate predicate) throws Exception {
                return query(predicate);
            }
        };
    }

    public Query where(WherePredicate where) {
        Keyword keyword = (Keyword) where.callable();
        Predicate predicate = where.predicate();
        return query(keyword, predicate);
    }

    private Query query(Keyword keyword, Predicate predicate) {
        Class aClass = keyword.forClass();

        if (predicate instanceof EqualsPredicate) {
            return equalTo(keyword, ((EqualsPredicate) predicate).value());
        }

        if (predicate instanceof GreaterThan) {
            return greaterThan(keyword, ((GreaterThan) predicate).value());
        }

        if (predicate instanceof GreaterThanOrEqualTo) {
            return greaterThanOrEqual(keyword, ((GreaterThanOrEqualTo) predicate).value());
        }

        if (predicate instanceof LessThan) {
            return lessThan(keyword, ((LessThan) predicate).value());
        }

        if (predicate instanceof LessThanOrEqualTo) {
            return lessThanOrEqual(keyword, ((LessThanOrEqualTo) predicate).value());
        }
        if (predicate instanceof Between) {
            Object lower = ((Between) predicate).lower();
            Object upper = ((Between) predicate).upper();
            return between(keyword, lower, upper);
        }
        if (predicate instanceof Not) {
            Predicate p = ((Not) predicate).predicate();
            return not(query(keyword, p));
        }
        if (predicate instanceof InPredicate) {
            Sequence values = ((InPredicate) predicate).values();
            return or(values.map(asQuery(keyword)));
        }
        if (predicate instanceof StartsWithPredicate) {
            String value = ((StartsWithPredicate) predicate).value();
            return new PrefixQuery(new Term(keyword.toString(), value));
        }
        if (predicate instanceof ContainsPredicate) {
            String value = ((ContainsPredicate) predicate).value();
            return new WildcardQuery(new Term(keyword.toString(), "*" + value + "*"));
        }
        if (predicate instanceof EndsWithPredicate) {
            String value = ((EndsWithPredicate) predicate).value();
            return new WildcardQuery(new Term(keyword.toString(), "*" + value));
        }
        if (predicate instanceof NotNullPredicate) {
            return notNull(keyword);
        }
        if (predicate instanceof NullPredicate) {
            return not(notNull(keyword));
        }
        throw new UnsupportedOperationException();
    }

    private Query equalTo(Keyword keyword, Object value) {
        return mappings.get(keyword.forClass()).equalTo(keyword.toString(), value);
    }

    private Query greaterThan(Keyword keyword, Object value) {
        return mappings.get(keyword.forClass()).greaterThan(keyword.toString(), value);
    }

    private Query greaterThanOrEqual(Keyword keyword, Object value) {
        return mappings.get(keyword.forClass()).greaterThanOrEqual(keyword.toString(), value);
    }

    private Query lessThan(Keyword keyword, Object value) {
        return mappings.get(keyword.forClass()).lessThan(keyword.toString(), value);
    }

    private Query lessThanOrEqual(Keyword keyword, Object value) {
        return mappings.get(keyword.forClass()).lessThanOrEqual(keyword.toString(), value);
    }

    private Query between(Keyword keyword, Object lower, Object upper) {
        return mappings.get(keyword.forClass()).between(keyword.toString(), lower, upper);
    }

    private Query notNull(Keyword keyword) {
        return mappings.get(keyword.forClass()).notNull(keyword.toString());
    }

    private Callable1<Object, Query> asQuery(final Keyword keyword) {
        return new Callable1<Object, Query>() {
            public Query call(Object o) throws Exception {
                return equalTo(keyword, o);
            }
        };
    }


}
