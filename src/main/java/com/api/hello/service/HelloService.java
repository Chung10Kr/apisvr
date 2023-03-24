package com.api.hello.service;

public interface HelloService {
    String sayHello(String name);

    default int countOf(String name) {
        return 0;
    }
}
