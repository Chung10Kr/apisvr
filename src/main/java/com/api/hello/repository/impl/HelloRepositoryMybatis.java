package com.api.hello.repository.impl;

import com.api.hello.dto.Hello;
import com.api.hello.repository.HelloRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

//@Repository
public class HelloRepositoryMybatis implements HelloRepository {
    private final String MAPPER_ID = "Hello.";
    private final SqlSessionTemplate sqlSession;

    public HelloRepositoryMybatis(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Hello findHello(String name) {
        try {
            return this.sqlSession.selectOne(
                    this.MAPPER_ID + "select",name
            );
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

            this.sqlSession.insert(this.MAPPER_ID + "insert", hello );
        }
        hello.setCount( hello.getCount() + 1 );
        this.sqlSession.update(this.MAPPER_ID + "update", hello );
    }
}
