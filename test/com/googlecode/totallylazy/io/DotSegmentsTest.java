package com.googlecode.totallylazy.io;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/* RFC 3986 URI Generic Syntax

5.2.4.  Remove Dot Segments

The pseudocode also refers to a "remove_dot_segments" routine for
interpreting and removing the special "." and ".." complete path
segments from a referenced path.  This is done after the path is
extracted from a reference, whether or not the path was relative, in
order to remove any invalid or extraneous dot-segments prior to
forming the target URI.  Although there are many ways to accomplish
this removal process, we describe a simple method using two string
buffers.

1.  The input buffer is initialized with the now-appended path
   components and the output buffer is initialized to the empty
   string.

2.  While the input buffer is not empty, loop as follows:

   A.  If the input buffer begins with a prefix of "../" or "./",
       then remove that prefix from the input buffer; otherwise,

   B.  if the input buffer begins with a prefix of "/./" or "/.",
       where "." is a complete path segment, then replace that
       prefix with "/" in the input buffer; otherwise,

   C.  if the input buffer begins with a prefix of "/../" or "/..",
       where ".." is a complete path segment, then replace that
       prefix with "/" in the input buffer and remove the last
       segment and its preceding "/" (if any) from the output
       buffer; otherwise,

   D.  if the input buffer consists only of "." or "..", then remove
       that from the input buffer; otherwise,

   E.  move the first path segment in the input buffer to the end of
       the output buffer, including the initial "/" character (if
       any) and any subsequent characters up to, but not including,
       the next "/" character or the end of the input buffer.

3.  Finally, the output buffer is returned as the result of
   remove_dot_segments.
 */
public class DotSegmentsTest {
    @Test
    public void removeDotSegments() throws Exception {
        assertThat(DotSegments.remove("."), is(""));
        assertThat(DotSegments.remove("./"), is("/"));
        assertThat(DotSegments.remove("./foo"), is("/foo"));
        assertThat(DotSegments.remove("./foo/"), is("/foo/"));
        assertThat(DotSegments.remove("/./foo/"), is("/foo/"));
        assertThat(DotSegments.remove("bar/./foo/"), is("bar/foo/"));
        assertThat(DotSegments.remove(".."), is(""));
        assertThat(DotSegments.remove("../"), is("/"));
        assertThat(DotSegments.remove("../foo"), is("/foo"));
        assertThat(DotSegments.remove("../foo/"), is("/foo/"));
        assertThat(DotSegments.remove("/../foo"), is("/foo"));
        assertThat(DotSegments.remove("bar/../foo/"), is("/foo/"));
        assertThat(DotSegments.remove("baz/bar/../foo/"), is("baz/foo/"));
        assertThat(DotSegments.remove(""), is(""));
        assertThat(DotSegments.remove("/"), is("/"));
    }
}
