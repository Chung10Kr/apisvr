package com.api.hello.repository;

import com.api.hello.dto.Hello;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class HelloRepositoryJdbc implements HelloRepository {


    private final SqlSessionTemplate sqlSession;
    private final String MAPPER_ID = "Hello.";
    public HelloRepositoryJdbc(SqlSessionTemplate sqlSession) {
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
            hello = new Hello(name,0);
            this.sqlSession.insert(this.MAPPER_ID + "insert", new Hello(name,0) );
        }
        hello.setCount( hello.getCount() + 1 );
        this.sqlSession.update(this.MAPPER_ID + "update", hello );
    }
}
