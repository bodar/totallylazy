package com.googlecode.totallylazy.template;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.http.Uri;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.http.Uri.packageUri;

public class UrlRenderers implements Renderers {
    private final Uri baseUrl;
    private final String extension;
    private final ConcurrentHashMap<String, Template> cache = new ConcurrentHashMap<>();
    private final Renderers parent;

    UrlRenderers(Uri baseUrl, Renderers parent, String extension) {
        this.baseUrl = baseUrl;
        this.parent = parent;
        this.extension = extension;
    }

    static Renderers renderers(Class<?> baseUrl){
        return renderers(packageUri(baseUrl));
    }

    static Renderers renderers(Uri baseUrl){
        return renderers(baseUrl, Empty.Instance, "st");
    }

    static Renderers renderers(Uri baseUrl, Renderers parent, final String extension){
        return new UrlRenderers(baseUrl, parent, extension);
    }

    @Override
    public Renderer<Object> get(String name) {
        return cast(cache.computeIfAbsent(name, (n) -> {
            try {
                return Template.template(baseUrl.mergePath(name + "." + extension).toURL().openStream(), this);
            } catch (IOException e) {
                throw LazyException.lazyException(e);
            }
        }));
    }

    @Override
    public Appendable render(Object instance, Appendable appendable) throws IOException {
        return parent.render(instance, appendable);
    }
}
