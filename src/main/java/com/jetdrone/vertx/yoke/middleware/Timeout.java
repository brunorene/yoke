package com.jetdrone.vertx.yoke.middleware;

import com.jetdrone.vertx.yoke.Middleware;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class Timeout extends Middleware {

    private final long timeout;

    public Timeout(long timeout) {
        this.timeout = timeout;
    }

    public Timeout() {
        this(5000);
    }
    @Override
    public void handle(final HttpServerRequest request, final Handler<Object> next) {
        final YokeHttpServerResponse res = (YokeHttpServerResponse) request.response();

        final long timerId = vertx.setTimer(timeout, new Handler<Long>() {
            @Override
            public void handle(Long event) {
                next.handle(408);
            }
        });

        res.endHandler(new Handler<Void>() {
            @Override
            public void handle(Void event) {
                vertx.cancelTimer(timerId);
            }
        });

        next.handle(null);
    }
}