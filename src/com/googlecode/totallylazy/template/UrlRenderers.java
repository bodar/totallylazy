package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.functions.Function2;
import com.googlecode.totallylazy.io.Uri;

import java.io.InputStream;
import java.util.Map;

import static com.googlecode.totallylazy.io.Uri.packageUri;

public class UrlRenderers {

    public static Function2<String, Renderers, Renderer<Map<String, Object>>> renderers(Class<?> baseUrl) {
        return renderers(packageUri(baseUrl));
    }


    public static Function2<String, Renderers, Renderer<Map<String, Object>>> renderers(Uri baseUrl) {
        return (name, renderers) -> {
            try (InputStream inputStream = baseUrl.mergePath(name).toURL().openStream()) {
                return Template.template(inputStream, renderers);
            }
        };
    }
}
