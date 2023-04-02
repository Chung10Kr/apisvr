package com.api.hello.repository.impl;

import com.api.hello.dto.Hello;
import com.api.hello.repository.HelloRepository;
import com.api.hello.repository.impl.jpa.HelloJPA;
import jakarta.persistence.EntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class HelloRepositoryJPA implements HelloRepository {

    private final HelloJPA repository;

    private final EntityManager entityManager;

    public HelloRepositoryJPA(HelloJPA repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }


    @Override
    public Hello findHello(String name) {
        try {
            return repository.findByName(name);
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void increaseCount(String name) {
        Hello hello = findHello(name);
        if (hello == null) {
            hello = Hello.builder()
                    .name(name)
                    .count(0)
                    .build();
        }
        hello.setCount( hello.getCount() + 1 );
        Hello result = repository.save(hello);
        System.out.println( result.getName() );
    }

}

