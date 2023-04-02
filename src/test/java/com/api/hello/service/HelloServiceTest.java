package com.api.hello.service;

import com.api.hello.service.Impl.HelloDecorator;
import com.api.hello.service.Impl.SimpleHelloService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import com.api.hello.dto.Hello;
import com.api.hello.repository.HelloRepository;

public class HelloServiceTest {
    @Test
    void simpleHelloService() {
        SimpleHelloService helloService = new SimpleHelloService(helloRepositoryStub);

        String ret = helloService.sayHello("Test");

        Assertions.assertThat(ret).isEqualTo("Hello Test");
    }

    private static HelloRepository helloRepositoryStub = new HelloRepository() {
        @Override
        public Hello findHello(String name) {
            return null;
        }

        @Override
        public void increaseCount(String name) {

        }
    };

    @Test
    void helloDecorator() {
        HelloDecorator decorator = new HelloDecorator(name -> name, alarmMsg);

        String ret = decorator.sayHello("Test");

        Assertions.assertThat(ret).isEqualTo("*Test*");
    }
}
