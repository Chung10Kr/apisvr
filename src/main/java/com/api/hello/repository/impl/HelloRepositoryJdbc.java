package com.api.hello.repository.impl;

import com.api.hello.dto.Hello;
import com.api.hello.repository.HelloRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;


public class HelloRepositoryJdbc implements HelloRepository {
    private final JdbcTemplate jdbcTemplate;

    public HelloRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Hello findHello(String name) {
        try {
            return jdbcTemplate.queryForObject("select * from hello where name = '" + name + "'",
                    (rs, rowNum) -> new Hello(
                            rs.getLong("idx"),
                            rs.getString("name"),
                            rs.getInt("count"),
                            rs.getObject("ins_timestamp", LocalDate.class),
                            rs.getObject("upd_timestamp", LocalDate.class)
                    ));
        }
        catch(EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void increaseCount(String name) {
        Hello hello = findHello(name);
        if (hello == null) jdbcTemplate.update("insert into hello(name,count) values(?,?)", name, 1);
        else jdbcTemplate.update("update hello set count = ? , upd_timestamp = now() where name = ?", hello.getCount() + 1,
                name);
    }
}
