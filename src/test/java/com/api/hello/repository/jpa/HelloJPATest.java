package com.api.hello.repository.jpa;

import com.api.hello.dto.Hello;
import com.api.hello.repository.impl.jpa.HelloJPA;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@DataJpaTest
public class HelloJPATest {
    @Autowired
    HelloJPA repositoryJPA;

    @Autowired
    TestEntityManager entityManager;
    @Test
    void insertSelect(){
        Hello hello = Hello.builder()
                .name("JPA")
                .build();

        repositoryJPA.save(hello);
        Hello ret = repositoryJPA.findByName("JPA");
        Assertions.assertThat( ret.getName() ).isEqualTo("JPA" );
    }

    @Test
    void insertUpdate(){
        Hello hello = Hello.builder()
                .name("JPA")
                .build();

        entityManager.persist(hello);
        entityManager.flush();

        hello.setCount( 1 );
        repositoryJPA.save(hello);

        Hello ret2 = repositoryJPA.findByName("JPA");
        Assertions.assertThat( ret2.getName() ).isEqualTo("JPA" );
        Assertions.assertThat( ret2.getCount() ).isEqualTo(1 );
    }

}
