package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.reduce;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.isPalindrome;
import static com.googlecode.totallylazy.matchers.NumberMatcher.is;
import static com.googlecode.totallylazy.numbers.Numbers.even;
import static com.googlecode.totallylazy.numbers.Numbers.fibonacci;
import static com.googlecode.totallylazy.numbers.Numbers.lcm;
import static com.googlecode.totallylazy.numbers.Numbers.lessThanOrEqualTo;
import static com.googlecode.totallylazy.numbers.Numbers.maximum;
import static com.googlecode.totallylazy.numbers.Numbers.mod;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;
import static com.googlecode.totallylazy.numbers.Numbers.primeFactors;
import static com.googlecode.totallylazy.numbers.Numbers.primes;
import static com.googlecode.totallylazy.numbers.Numbers.product;
import static com.googlecode.totallylazy.numbers.Numbers.range;
import static com.googlecode.totallylazy.numbers.Numbers.squared;
import static com.googlecode.totallylazy.numbers.Numbers.subtract;
import static com.googlecode.totallylazy.numbers.Numbers.valueOf;
import static com.googlecode.totallylazy.numbers.Numbers.zero;
import static com.googlecode.totallylazy.numbers.Numbers.Σ;
import static com.googlecode.totallylazy.predicates.WherePredicate.asWhere;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProjectEuler {
    @Test
    public void problem1() throws Exception {
        assertThat(range(1, 999).filter(or(sequence(3, 5).map(asWhere(mod, zero)))).reduce(Σ), is(233168));
    }

    @Test
    public void problem2() throws Exception {
        assertThat(fibonacci().takeWhile(lessThanOrEqualTo(4000000)).filter(even).reduce(Σ), is(4613732));
    }

    @Test
    public void problem3() throws Exception {
        assertThat(primeFactors(600851475143L).last(), is(6857));
    }

    @Test
    public void problem4() throws Exception {
        assertThat(range(999, 100).cartesianProduct().map(multiply.pair()).
                filter(where(toString, isPalindrome())).reduce(maximum), is(906609));
    }

    @Test
    public void problem5() throws Exception {
        assertThat(range(1, 20).reduce(lcm), is(232792560));
    }

    @Test
    public void problem6() throws Exception {
        assertThat(subtract(squared(range(1, 100).reduce(Σ)), range(1, 100).map(squared).reduce(Σ)), is(25164150));
    }

    @Test
    public void problem7() throws Exception {
        assertThat(primes.drop(10000).head(), is(104743));
    }

    @Test
    public void problem8() throws Exception {
        Sequence<Number> number = characters("7316717653133062491922511967442657474235534919493496983520312774506326239578318016984801869478851843858615607891129494954595017379583319528532088055111254069874715852386305071569329096329522744304355766896648950445244523161731856403098711121722383113622298934233803081353362766142828064444866452387493035890729629049156044077239071381051585930796086670172427121883998797908792274921901699720888093776657273330010533678812202354218097512545405947522435258490771167055601360483958644670632441572215539753697817977846174064955149290862569321978468622482839722413756570560574902614079729686524145351004748216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450").map(valueOf);
        assertThat(number.windowed(5).map(reduce(product)).reduce(maximum), is(40824));
    }
}
