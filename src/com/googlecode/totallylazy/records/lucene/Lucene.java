package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
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
import com.googlecode.totallylazy.predicates.NotNullPredicate;
import com.googlecode.totallylazy.predicates.NullPredicate;
import com.googlecode.totallylazy.predicates.OrPredicate;
import com.googlecode.totallylazy.predicates.StartsWithPredicate;
import com.googlecode.totallylazy.predicates.WherePredicate;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;

import java.util.Date;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static org.apache.lucene.document.DateTools.dateToString;

public class Lucene {
    public static final Keyword RECORD_KEY = keyword("type");


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
                booleanQuery.add(query, occur);
                return booleanQuery;
            }
        };
    }

    public static Query query(Predicate<? super Record> predicate) {
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


    private static Callable1<? super Predicate, Query> asQuery() {
        return new Callable1<Predicate, Query>() {
            public Query call(Predicate predicate) throws Exception {
                return query(predicate);
            }
        };
    }

    public static Query where(WherePredicate where) {
        Keyword keyword = (Keyword) where.callable();
        Predicate predicate = where.predicate();
        return query(keyword, predicate);
    }

    private static Query query(Keyword keyword, Predicate predicate) {
        Class aClass = keyword.forClass();

        if (predicate instanceof EqualsPredicate) {
            return equalTo(keyword, ((EqualsPredicate) predicate).value());
        }

        if (predicate instanceof GreaterThan) {
            Object value = ((GreaterThan) predicate).value();
            if (aClass.equals(Integer.class)) {
                return NumericRangeQuery.newIntRange(keyword.toString(), (Integer) value, null, false, true);
            }
            if (aClass.equals(Long.class)) {
                return NumericRangeQuery.newLongRange(keyword.toString(), (Long) value, null, false, true);
            }
            if (aClass.equals(Date.class)) {
                return new TermRangeQuery(keyword.toString(), dateToString(((Date) value), DateTools.Resolution.MILLISECOND), null, false, true);
            }
            if (aClass.equals(String.class)) {
                return new TermRangeQuery(keyword.toString(), (String) value, null, false, true);
            }
            throw new UnsupportedOperationException();
        }
        if (predicate instanceof GreaterThanOrEqualTo) {
            Object value = ((GreaterThanOrEqualTo) predicate).value();
            if (aClass.equals(Integer.class)) {
                return NumericRangeQuery.newIntRange(keyword.toString(), (Integer) value, null, true, true);
            }
            if (aClass.equals(Long.class)) {
                return NumericRangeQuery.newLongRange(keyword.toString(), (Long) value, null, true, true);
            }
            if (aClass.equals(Date.class)) {
                return new TermRangeQuery(keyword.toString(), dateToString(((Date) value), DateTools.Resolution.MILLISECOND), null, true, true);
            }
            if (aClass.equals(String.class)) {
                return new TermRangeQuery(keyword.toString(), (String) value, null, true, true);
            }
            throw new UnsupportedOperationException();
        }
        if (predicate instanceof LessThan) {
            Object value = ((LessThan) predicate).value();
            if (aClass.equals(Integer.class)) {
                return NumericRangeQuery.newIntRange(keyword.toString(), null, (Integer) value, true, false);
            }
            if (aClass.equals(Long.class)) {
                return NumericRangeQuery.newLongRange(keyword.toString(), null, (Long) value, true, false);
            }
            if (aClass.equals(Date.class)) {
                return new TermRangeQuery(keyword.toString(), null, dateToString(((Date) value), DateTools.Resolution.MILLISECOND), true, false);
            }
            if (aClass.equals(String.class)) {
                return new TermRangeQuery(keyword.toString(), null, (String) value, true, false);
            }
            throw new UnsupportedOperationException();
        }
        if (predicate instanceof LessThanOrEqualTo) {
            Object value = ((LessThanOrEqualTo) predicate).value();
            if (aClass.equals(Integer.class)) {
                return NumericRangeQuery.newIntRange(keyword.toString(), null, (Integer) value, true, true);
            }
            if (aClass.equals(Long.class)) {
                return NumericRangeQuery.newLongRange(keyword.toString(), null, (Long) value, true, true);
            }
            if (aClass.equals(Date.class)) {
                return new TermRangeQuery(keyword.toString(), null, dateToString(((Date) value), DateTools.Resolution.MILLISECOND), true, true);
            }
            if (aClass.equals(String.class)) {
                return new TermRangeQuery(keyword.toString(), null, (String) value, true, true);
            }
            throw new UnsupportedOperationException();
        }
        if (predicate instanceof Between) {
            Object lower = ((Between) predicate).lower();
            Object upper = ((Between) predicate).upper();
            if (aClass.equals(Integer.class)) {
                return NumericRangeQuery.newIntRange(keyword.toString(), (Integer) lower, (Integer) upper, true, true);
            }
            if (aClass.equals(Long.class)) {
                return NumericRangeQuery.newLongRange(keyword.toString(), (Long) lower, (Long) upper, true, true);
            }
            if (aClass.equals(Date.class)) {
                return new TermRangeQuery(keyword.toString(), dateToString(((Date) lower), DateTools.Resolution.MILLISECOND), dateToString(((Date) upper), DateTools.Resolution.MILLISECOND), true, true);
            }
            if (aClass.equals(String.class)) {
                return new TermRangeQuery(keyword.toString(), (String) lower, (String) upper, true, true);
            }
            throw new UnsupportedOperationException();
        }
        if (predicate instanceof Not) {
            Predicate p = ((Not) predicate).predicate();
            return not(query(keyword, p));
        }
        if(predicate instanceof InPredicate){
            Sequence values = ((InPredicate) predicate).values();
            return or(values.map(asQuery(keyword)));
        }
        if(predicate instanceof StartsWithPredicate){
            String value = ((StartsWithPredicate) predicate).value();
            return new PrefixQuery(new Term(keyword.toString(), value));
        }
        if(predicate instanceof ContainsPredicate){
            String value = ((ContainsPredicate) predicate).value();
            return new WildcardQuery(new Term(keyword.toString(), "*" + value + "*"));
        }
        if(predicate instanceof EndsWithPredicate){
            String value = ((EndsWithPredicate) predicate).value();
            return new WildcardQuery(new Term(keyword.toString(), "*" + value));
        }
        if(predicate instanceof NotNullPredicate){
            return notNull(keyword, aClass);
        }
        if(predicate instanceof NullPredicate){
            return not(notNull(keyword, aClass));
        }
        throw new UnsupportedOperationException();
    }

    private static Query notNull(Keyword keyword, Class aClass) {
        if (aClass.equals(Integer.class)) {
            return NumericRangeQuery.newIntRange(keyword.toString(), null, null, true, true);
        }
        if (aClass.equals(Long.class)) {
            return NumericRangeQuery.newLongRange(keyword.toString(), null, null, true, true);
        }
        if (aClass.equals(Date.class)) {
            return new TermRangeQuery(keyword.toString(), null, null, true, true);
        }
        if (aClass.equals(String.class)) {
            return new TermRangeQuery(keyword.toString(), null, null, true, true);
        }
        throw new UnsupportedOperationException();
    }

    private static Query equalTo(Keyword keyword, Object value) {
        Class aClass = keyword.forClass();
        if (aClass.equals(Integer.class)) {
            return NumericRangeQuery.newIntRange(keyword.toString(), (Integer) value, (Integer) value, true, true);
        }
        if (aClass.equals(Long.class)) {
            return NumericRangeQuery.newLongRange(keyword.toString(), (Long) value, (Long) value, true, true);
        }
        if (aClass.equals(Date.class)) {
            return new TermQuery(new Term(keyword.toString(), dateToString(((Date) value), DateTools.Resolution.MILLISECOND)));
        }
        if (aClass.equals(String.class)) {
            return new TermQuery(new Term(keyword.toString(), (String) value));
        }
        throw new UnsupportedOperationException();
    }

    private static Callable1<Object, Query> asQuery(final Keyword keyword) {
        return new Callable1<Object, Query>() {
            public Query call(Object o) throws Exception {
                return equalTo(keyword, o);
            }
        };
    }

    public static TermQuery record(Keyword recordName) {
        return new TermQuery(new Term(RECORD_KEY.toString(), recordName.toString()));
    }


}
