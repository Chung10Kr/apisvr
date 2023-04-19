package com.config.jpa;

import com.api.member.dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

/**
 * fileName       : JpaTest
 * author         : crlee
 * date           : 2023/04/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/10        crlee       최초 생성
 */
@DataJpaTest
@TestInstance (TestInstance. Lifecycle.PER_CLASS)
public class JpqlTest {

    @PersistenceUnit
    EntityManagerFactory emf; //엔티티 매니저 팩토리 생성

    EntityManager em;
    EntityTransaction tx;
    @BeforeAll
    public void init(){
        //this.em = emf.createEntityManager();
    }
    @BeforeEach
    public void open(){
        this.em = emf.createEntityManager();
        this.tx = em.getTransaction();
        this.tx.begin(); //트랜잭션 시작
    }
    @AfterEach
    public void close(){
        this.tx.commit();//트랜잭션 커밋
        this.em.close(); //엔티티 매니저 종료
    }
    @AfterAll
    public void end(){
        this.emf.close();
    }


    @Test
    public void EmbeddedNullTest(){
        Member member1 = Member.builder()
                .id("memberid1")
                .username("member1")
                .age(1)
                .address(null)
                .build();
        em.persist(member1);

        Member findMember = em.find(Member.class,"memberid1");
        Assertions.assertThat( findMember.getAddress() ).isNull();
    }

}
