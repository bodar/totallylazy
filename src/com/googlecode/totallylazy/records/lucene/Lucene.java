package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.predicates.AllPredicate;
import com.googlecode.totallylazy.predicates.AndPredicate;
import com.googlecode.totallylazy.predicates.Between;
import com.googlecode.totallylazy.predicates.ContainsPredicate;
import com.googlecode.totallylazy.predicates.EndsWithPredicate;
import com.googlecode.totallylazy.predicates.EqualsPredicate;
import com.googlecode.totallylazy.predicates.GreaterThan;
import com.googlecode.totallylazy.predicates.GreaterThanOrEqualTo;
import com.googlecode.totallylazy.predicates.InPredicate;
import com.googlecode.totallylazy.predicates.LessThan;
import com.googlecode.totallylazy.predicates.LessThanOrEqualTo;
import com.googlecode.totallylazy.predicates.Not;
import com.googlecode.totallylazy.predicates.NotEqualsPredicate;
import com.googlecode.totallylazy.predicates.NotNullPredicate;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.lucene.mappings.Mappings;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Keywords.keyword;

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

    private static Function2<? super BooleanQuery, ? super Query, BooleanQuery> add(final BooleanClause.Occur occur) {
        return new Function2<BooleanQuery, Query, BooleanQuery>() {
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

    @SuppressWarnings("unchecked")
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
        if (predicate instanceof Not) {
            return not(query(((Not) predicate).predicate()));
        }
        if (predicate instanceof AllPredicate) {
            return new MatchAllDocsQuery();
        }
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    private Function1<? super Predicate, Query> asQuery() {
        return new Function1<Predicate, Query>() {
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

    @SuppressWarnings("unchecked")
    private Query query(Keyword keyword, Predicate predicate) {
        if (predicate instanceof EqualsPredicate) {
            return equalTo(keyword, ((Value) predicate).value());
        }

        if (predicate instanceof GreaterThan) {
            return greaterThan(keyword, ((Value) predicate).value());
        }

        if (predicate instanceof GreaterThanOrEqualTo) {
            return greaterThanOrEqual(keyword, ((Value) predicate).value());
        }

        if (predicate instanceof LessThan) {
            return lessThan(keyword, ((Value) predicate).value());
        }

        if (predicate instanceof LessThanOrEqualTo) {
            return lessThanOrEqual(keyword, ((Value) predicate).value());
        }
        if (predicate instanceof Between) {
            Object lower = ((Between) predicate).lower();
            Object upper = ((Between) predicate).upper();
            return between(keyword, lower, upper);
        }
        if (predicate instanceof NotEqualsPredicate) {
            return not(equalTo(keyword, ((Value) predicate).value()));
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

    private Function1<Object, Query> asQuery(final Keyword keyword) {
        return new Function1<Object, Query>() {
            public Query call(Object o) throws Exception {
                return equalTo(keyword, o);
            }
        };
    }


}
