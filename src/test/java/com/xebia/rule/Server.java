package com.xebia.rule;

import org.junit.rules.ExternalResource;

public class Server extends ExternalResource {

    final int port = 9998;
    public final String uri = "http://localhost:" + port;

    public static Server create() {
        return new GrizzlyServer();
    }

    public static Server create(Class<? extends Server> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return create();
    }
}
