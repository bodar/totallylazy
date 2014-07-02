package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Block;
import com.googlecode.totallylazy.Function;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.totallylazy.reactive.Observable.EMPTY_CLOSEABLE;

public interface ObservableSocket {
    static Observable<AsynchronousSocketChannel> bind(SocketAddress socketAddress) throws IOException {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open().bind(socketAddress);
        System.out.println("socketAddress = " + server.getLocalAddress());
        return observer -> {
            server.accept(server, completionHandler(observer, handler -> result -> attachment -> observer.next(result)));
            return server::close;
        };
    }

    static Observable<ByteBuffer> read(AsynchronousSocketChannel channel) {
        return observer -> {
            AtomicBoolean close = new AtomicBoolean(false);
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            channel.read(buffer, channel, completionHandler(observer, handler -> result -> attachment -> {
                if (result == -1) {
                    observer.complete();
                    return;
                }
                System.out.println("read = " + result);
                if(result > 0) {
                    observer.next(buffer);
                }
                if(!close.get()) attachment.read(buffer, attachment, handler);
            }));
            return () -> close.set(true);
        };
    }

    static <A, B, C> CompletionHandler<A, B> completionHandler(Observer<C> observer, Function<CompletionHandler<A, B>, Function<A, Block<B>>> next) {
        return new CompletionHandler<A, B>() {
            @Override
            public void completed(A result, B attachment) {
                next.apply(this).apply(result).apply(attachment);
            }

            @Override
            public void failed(Throwable exc, B attachment) {
                observer.error(exc);
            }
        };
    }
}
