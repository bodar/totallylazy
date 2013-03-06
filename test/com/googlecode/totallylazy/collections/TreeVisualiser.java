package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Files;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import static com.googlecode.totallylazy.Strings.bytes;

@Ignore
public class TreeVisualiser {
    @Test
    public void canVisualiseTree() throws Exception {
        render((TreeMap<?, ?>) MapPerformanceTest.createPersistent(MapPerformanceTest.range));
    }

    private void render(TreeMap<?, ?> map) {
        final File file = new File(Files.temporaryDirectory(), getClass().getSimpleName() + ".html");
        Files.write(bytes("<html><head><style>" +
                ".tree { border: 1px solid grey; padding: 0 1px; } " +
                ".key { text-align: center; } " +
                ".tree, .left, .right { display: table-cell; }" +
                "</style></head><body>" + new TreeMapRenderer().render(map) + "</body></html>"), file);
        System.out.println("tree = " + file);
    }

    private class TreeMapRenderer {
        public String render(TreeMap<?, ?> map) {
            if (map.isEmpty()) return "";
            return "<div class='tree'>" +
                    "<div class='key'>" + map.key() + "</div>" +
                    "<div class='left'>" + render(map.left()) + "</div>" +
                    "<div class='right'>" + render(map.right()) + "</div>" +
                    "</div>";
        }
    }

}
