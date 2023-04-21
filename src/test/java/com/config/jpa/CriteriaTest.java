package com.config.jpa;

import com.api.member.dto.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

/**
 * fileName       : CriteriaTest
 * author         : crlee
 * date           : 2023/04/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/19        crlee       최초 생성
 */
@DataJpaTest
@TestInstance (TestInstance. Lifecycle.PER_CLASS)
public class CriteriaTest {


    @PersistenceUnit
    EntityManagerFactory emf; //엔티티 매니저 팩토리 생성

    EntityManager em;
    EntityTransaction tx;
    @BeforeAll
    public void init(){
        this.em = emf.createEntityManager();
    }
    @BeforeEach
    public void open(){
        this.tx = em.getTransaction();
        this.tx.begin(); //트랜잭션 시작
        this.setData();
    }
    @AfterEach
    public void close(){
        //this.tx.commit();//트랜잭션 커밋
        this.tx.rollback();
    }
    @AfterAll
    public void end(){
        this.em.close(); //엔티티 매니저 종료
        //this.emf.close();
    }

    void setData(){
        Member member1 = Member.builder()
                .id("memberid1")
                .username("kim1")
                .age(1)
                .build();
        em.persist(member1);

        Member member2 = Member.builder()
                .id("memberid2")
                .username("kim2")
                .age(1)
                .build();
        em.persist(member2);
    }

    @Test
    @DisplayName("Criteria Test ")
    void CriteriaTest(){
        // 사용 준비
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);

        // 루트 클래스( 조회를 시작할 클래스 )
        Root<Member> memberRoot = query.from(Member.class);

        // 쿼리 생성
        CriteriaQuery<Member> criteriaQuery = query.select(memberRoot).where( criteriaBuilder.equal( memberRoot.get("age"),1 ) );
        List<Member> resultList = em.createQuery(criteriaQuery).getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo( 2 );
    }

}
