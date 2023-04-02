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

@JdbcTest
public class JdbcTemplateTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {

        String sql = """
                create sequence if not exists table_hello_id_seq;
                create table if not exists hello(
                    id integer not null default nextval('table_hello_id_seq') primary key,
                    name varchar(50) unique,
                    count int,
                    ins_timestamp date default now()
                );
                """;

        jdbcTemplate.execute(sql);
    }

    @Test
    void insertAndQuery() {
        jdbcTemplate.update("insert into hello(name,count) values(?, ?)", "Toby", 3);
        jdbcTemplate.update("insert into hello(name,count) values(?, ?)", "Spring", 1);

        Long count = jdbcTemplate.queryForObject("select count(*) from hello", Long.class);
        assertThat(count).isEqualTo(2);

    }
}
