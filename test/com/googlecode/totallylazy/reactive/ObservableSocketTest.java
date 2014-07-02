package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Characters;
import com.googlecode.totallylazy.Strings;
import org.junit.Ignore;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.nullValue;

public class ObservableSocketTest {
    @Test
    @Ignore
    public void canObserverASocket() throws Exception {
        Observable<AsynchronousSocketChannel> server = ObservableSocket.bind(new InetSocketAddress(38523));
        Observable<Observable<ByteBuffer>> connections = server.map(ObservableSocket::read);
        Observable<String> listObservable = connections.flatMap(c -> c.map(buffer -> {
            buffer.flip();
            CharBuffer decode = Characters.UTF8.decode(buffer);
            return decode.toString();
        }));

        CapturingObserver<String> observer = new CapturingObserver<>();
        listObservable.subscribe(observer);

        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://localhost:38523").openConnection();
            return urlConnection.getResponseCode();
        });

        Thread.sleep(1000);
        assertThat(observer.items().first(), Strings.startsWith("GET /"));
        assertThat(observer.error(), nullValue());
        assertThat(observer.completed(), is(false));
    }
}