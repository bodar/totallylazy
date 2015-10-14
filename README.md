totallylazy
===========

Another functional library for Java

Friendly support is available at our [Google Group](https://groups.google.com/forum/?fromgroups#!forum/totallylazy) or post a question on StackOverflow with [tag totallylazy](http://stackoverflow.com/questions/tagged/totallylazy)

A functional library for Java that has the following features

 * Tries to be as lazy as possible just like [Clojure's](http://clojure.org/) collection library
 * Works with Iterable, Iterator, Arrays, Char Sequences, Dates and Numbers (i.e virtually everything)
 * Follows the [ML family](http://hyperpolyglot.org/ml) of function / method names (Standard ML, oCaml, F#, Scala, Haskell)
 * Uses and extends Callable interface for maximum interop (i.e Can use with Clojure, [Hazelcast](http://www.hazelcast.com/))
 * Optionally supports using [Hamcrest](http://code.google.com/p/hamcrest/) matchers as predicates
 * Supports chaining of all methods (Recommended) or the use of static imports for all methods.
 * Contains PersistentSet, PersistentMap, PersistentSortedMap, PersistentList
 * Support Functors and Applicative Functors
 * Supports runtime multi-method dispatch and pattern matching

Tail call optimisation is available in conjunction with [JCompilo](https://code.google.com/p/jcompilo/)

Now also available in [Objective-C](https://github.com/stuartervine/OCTotallyLazy/) 



Examples
========

The following are some simple examples of actual Java code (minus any imports). They are using numbers just to make them simple but these could just as well be any types; though you would either need to create some strongly types predicates / callables for your types or use the provided dynamic proxy support.

```java
sequence(1, 2, 3, 4).filter(even); // lazily returns 2,4
sequence(1, 2).map(toString); // lazily returns "1", "2"
sequence(1, 2).mapConcurrently(toString); // lazily distributes the work to background threads
sequence(1, 2, 3).take(2); // lazily returns 1,2
sequence(1, 2, 3).drop(2); // lazily returns 3
sequence(1, 2, 3).tail(); // lazily returns 2,3
sequence(1, 2, 3).head(); // eagerly returns 1
sequence(1, 2, 3).reduce(sum); // eagerly return 6
sequence(1, 3, 5).find(even); // eagerly returns none()
sequence(1, 2, 3).contains(2); // eagerly returns true
sequence(1, 2, 3).exists(even); // eagerly return true
sequence(1, 2, 3).forAll(odd); // eagerly returns false;
sequence(1, 2, 3).foldLeft(0, add); // eagerly returns 6
sequence(1, 2, 3).toString(); // eagerly returns "1,2,3"
sequence(1, 2, 3).toString(":"); // eagerly returns "1:2:3"
```

Generators
==========

```java
range(1, 4); // lazily returns 1,2,3,4
repeat("car"); // lazily returns an infinite sequence of "car"s
iterate(increment, 1); // lazily returns 1,2,3 ... to infinity
range(1, 4).cycle(); // lazily returns 1,2,3,4,1,2,3,4,1,2,3,4 infinitely 
primes(); // lazily returns every prime number
fibonacci(); // lazily returns the fibonacci sequence
powersOf(3); // lazily returns the powers of 3 (i.e 1,3,9,27 ...)
```

Naturally you can combine these operations together ... 

```java
iterate(increment, 1).filter(even).take(10).reduce(average); // returns 11
```

And because all the operations except reduce are lazy the sequence of numbers is only processed once.

----

Releases
========

 * Stable releases are version 1.x (Require Java 7+) Branch:java7
 * Development releases are version 2.x (Require Java 8+) HEAD

All releases are created automically and released to

http://repo.bodar.com/com/googlecode/totallylazy/totallylazy/

This is a maven repository so you can just add the following in you repo section

```xml
<repositories>
    <repository>
        <id>repo.bodar.com</id>
        <url>http://repo.bodar.com</url>
    </repository>
</repositories>
```
and then 

```xml
<dependencies>
    <dependency>
        <groupId>com.googlecode.totallylazy</groupId>
        <artifactId>totallylazy</artifactId>
        <version>SOME_VERSION</version>
    </dependency>
</dependencies>
```

License
=======

[Apache 2](http://www.apache.org/licenses/LICENSE-2.0)

Sponsors
========

YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling 
Java and .NET applications.Take a look at !YourKit's leading software products:
[YourKit Java Profiler](http://www.yourkit.com/java/profiler/index.jsp)
[YourKit .NET Profiler](http://www.yourkit.com/.net/profiler/index.jsp)
