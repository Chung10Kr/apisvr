package com.api.hello;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class HelloApiTest {
    @Value("${server.port}")
    String PORT;

    @Value("${server.servlet.context-path}")
    String CONTEXT_PATH;

    String URL = "http://localhost";

    String SERVER_URL;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init(){
        this.SERVER_URL = String.format("%s:%s%s", URL,PORT,CONTEXT_PATH);
    }
    @Test
    void helloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> res = rest.getForEntity(this.SERVER_URL + "/hello?name={name}", String.class, "Spring");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);

        assertThat(res.getBody()).isEqualTo("*Hello Spring*");
    }

    @Test
    void countApi(){
        String name = "ApiTest";
        Long count = jdbcTemplate.queryForObject("select count from hello where name = '"+name+"'", Long.class);

        TestRestTemplate rest = new TestRestTemplate();
        int i=0;
        while(i<3){
            rest.getForEntity(this.SERVER_URL + "/hello?name={name}", String.class, name);
            i++;
        }
        ResponseEntity<String> res = rest.getForEntity(this.SERVER_URL + "/count?name={name}", String.class, name);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);

        count = count+i;
        assertThat(res.getBody()).isEqualTo(name+": "+count);
    }

    @Test
    void failsHelloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        ResponseEntity<String> res =
                rest.getForEntity(this.SERVER_URL + "/hello?name=", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
