package com.config.jpa;

import com.api.member.dto.*;
import jakarta.persistence.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

/**
 * fileName       : JpglTest
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
        Team team1 = Team.builder()
                .id("team1")
                .name("팀1")
                .build();
        em.persist(team1);

        Member member1 = Member.builder()
                .id("memberid1")
                .username("kim1")
                .age(1)
                .team(team1)
                .build();
        em.persist(member1);

        Member member2 = Member.builder()
                .id("memberid2")
                .username("kim2")
                .age(2)
                .team(team1)
                .build();
        em.persist(member2);

        em.flush();
        em.clear();

    }
    @Test
    void TypedQueryTest(){
        TypedQuery query = em.createQuery( "select m from Member m" , Member.class );
        List<Member> resultList = query.getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo(2);

        Query query2 = em.createQuery( "select m from Member m" , Member.class );
        List<Member> resultList2 = query2.getResultList();
        Assertions.assertThat( resultList2.size() ).isEqualTo(2);
    }

    @Test
    void ParameterTest(){
        TypedQuery query = em.createQuery( "select m from Member m where m.username = :username" , Member.class );
        query.setParameter("username" , "kim1" );
        List<Member> resultList = query.getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo(1);
        Assertions.assertThat( resultList.get(0).getAge() ).isEqualTo(1);
    }

    @Test
    void projectionTest(){
        Query query2 = em.createQuery( "select m.username from Member m where m.username = :username");
        query2.setParameter("username" , "kim1" );
        List<String> resultList2 = query2.getResultList();
        Assertions.assertThat( resultList2.get(0) ).isEqualTo("kim1");
    }

    @Test
    void pageTest(){

        for (int i=5 ; i<30 ; i++ ){
            em.persist(Member.builder()
                    .id("memberid"+i)
                    .username("kim"+i)
                    .age(2)
                    .build());
        };
        Query query = em.createQuery( "select m from Member m where m.age = :age");
        query.setParameter("age" , "2" );
        query.setFirstResult(10); //시작 row
        query.setMaxResults(5); // max Data Count
        List<String> resultList = query.getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo(5);
    }

    @Test
    void joinTest(){
        String jpgl = "select m from Member m join m.team t where t.name = :teamName";
        List<Member> resultList = em.createQuery(jpgl,Member.class)
                .setParameter("teamName" , "팀1")
                .getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo( 2 );
    }
    @Test
    void fetchJoinTest(){
        String jpgl = "select m from Member m join fetch m.team t where t.name = :teamName";
        List<Member> resultList = em.createQuery(jpgl,Member.class)
                .setParameter("teamName" , "팀1")
                .getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo( 2 );
        Assertions.assertThat( resultList.get(0).getTeam().getName() ).isEqualTo( "팀1" );
        //패치 조인으로 회원과 팀을 함께 조회해서 지연로딩 발생 안함
    }

    @Test
    void collectionFecthTest(){

        String jpgl = "select t from Team t join fetch t.members where t.name = :teamName";
        List<Team> resultList = em.createQuery(jpgl,Team.class)
                .setParameter("teamName" , "팀1")
                .getResultList();

        Assertions.assertThat( resultList.get(0).getName() ).isEqualTo("팀1");
        Assertions.assertThat( resultList.get(0).getMembers().size() ).isEqualTo(2);

    }
}
