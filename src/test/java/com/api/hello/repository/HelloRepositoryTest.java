package com.api.hello.repository;

import com.api.hello.dto.Hello;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class HelloRepositoryTest {
    @Autowired
    HelloRepository helloRepository;

    @Test
    void findHello() {
        String name = "TestId";
        helloRepository.increaseCount(name);
        Hello ret  = helloRepository.findHello(name);
        assertThat( ret.getName() ).isEqualTo(name);
    }

    @Test
    void findHelloFailed() {
        assertThat(helloRepository.findHello("Toby")).isNull();
    }

    @Test
    void incraseCount() {
        assertThat(helloRepository.countOf("Toby")).isEqualTo(0);

        helloRepository.increaseCount("Toby");
        assertThat(helloRepository.countOf("Toby")).isEqualTo(1);

        helloRepository.increaseCount("Toby");
        assertThat(helloRepository.countOf("Toby")).isEqualTo(2);
    }

}
