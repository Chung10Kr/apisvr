package com;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ApiSvrApplication {
    private final JdbcTemplate jdbcTemplate;

    public ApiSvrApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //@PostConstruct
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

    @Bean
    ApplicationRunner run( ConditionEvaluationReport report ){
        return args -> {
            System.out.println(
                report.getConditionAndOutcomesBySource().entrySet().stream()
                        .filter( co -> co.getValue().isFullMatch() )
                        .filter( co -> co.getKey().indexOf("Jax") < 0 )
                        .map( co -> {
                            System.out.println(co.getKey());
                            co.getValue().forEach( c -> {
                                System.out.println("\t"+c.getOutcome());
                            });
                            System.out.println();
                            return co;
                        }).count()
            );
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ApiSvrApplication.class, args);
    }

}
