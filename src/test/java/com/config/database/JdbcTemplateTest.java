package com.config.database;

import com.api.hello.dto.Hello;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class JdbcTemplateTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SqlSessionTemplate sqlSession;
    @BeforeEach
    void init() {
        jdbcTemplate.execute("create table if not exists hello(name varchar(50) primary key, count int)"
        );
    }

    @Test
    void insertAndQuery() {
        jdbcTemplate.update("insert into hello values(?, ?)", "Toby", 3);
        jdbcTemplate.update("insert into hello values(?, ?)", "Spring", 1);

        Long count = jdbcTemplate.queryForObject("select count(*) from hello", Long.class);
        assertThat(count).isEqualTo(2);

    }

    @Test
    void Mybatis(){
        Hello hello = new Hello("Mybatis-TEST" , 0);
        sqlSession.insert("Hello.insert",hello);

        Hello ret = sqlSession.selectOne("Hello.select",hello);
        assertThat( hello.getName() ).isEqualTo(ret.getName());
        assertThat( hello.getCount() ).isEqualTo(ret.getCount());
    }
}
