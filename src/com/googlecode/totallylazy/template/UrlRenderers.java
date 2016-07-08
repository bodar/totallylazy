package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.Uri;

import java.io.InputStream;
import java.util.Map;

import static com.googlecode.totallylazy.Uri.packageUri;

public class UrlRenderers {

    public static Function2<String, Renderers, Renderer<Map<String, Object>>> renderers(Class<?> baseUrl) {
        return renderers(packageUri(baseUrl));
    }

    public static Function2<String, Renderers, Renderer<Map<String, Object>>> renderers(final Uri baseUrl) {
        return new Function2<String, Renderers, Renderer<Map<String, Object>>>() {
            @Override
            public Renderer<Map<String, Object>> call(String name, Renderers renderers) throws Exception {
                try (InputStream inputStream = baseUrl.mergePath(name).toURL().openStream()) {
                    return Template.template(inputStream, renderers);
                }
            }
        };
    }
}
