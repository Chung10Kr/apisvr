package com.config.database;

import com.api.hello.dto.Hello;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class MybatisTest {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Test
    void Mybatis(){
        Hello hello = new Hello("Mybatis-TEST" , 0);
        sqlSession.insert("Hello.insert",hello);

        Hello ret = sqlSession.selectOne("Hello.select",hello);
        assertThat( hello.getName() ).isEqualTo(ret.getName());
        assertThat( hello.getCount() ).isEqualTo(ret.getCount());
    }
}
