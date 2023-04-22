package com.config.jpa;

import com.api.member.dto.Member;
import com.api.member.dto.QMember;
import com.api.member.dto.Team;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryModifiers;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.naming.directory.SearchResult;
import java.util.List;

/**
 * fileName       : JpglTest
 * author         : QueryDSLTest
 * date           : 2023/04/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/10        crlee       최초 생성
 */
@DataJpaTest
@TestInstance (TestInstance. Lifecycle.PER_CLASS)
public class QueryDSLTest {

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
    void selectOneTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = new QMember("m"); //별칭 m
        Member findMember = queryFactory
                .selectFrom(qMember)
                .where( qMember.id.eq("memberid1") )
                .fetchOne();
        Assertions.assertThat( findMember.getUsername() ).isEqualTo( "kim1" );
    }
    @Test
    void selectListTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = new QMember("m"); //별칭 m
        List<Member> findMember = queryFactory
                .selectFrom(qMember)
                .where(
                        qMember.username.contains("kim"),
                        qMember.age.between(1,3)
                )
                .fetch();
        Assertions.assertThat( findMember.size() ).isEqualTo( 2 );
    }
    @Test
    void pageTest(){

        for (int i=5 ; i<30 ; i++ ){
            em.persist(Member.builder()
                    .id("memberid"+i)
                    .username("kim"+i)
                    .age(10)
                    .build());
        };

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = new QMember("m"); //별칭 m
        List<Member> findMember = queryFactory
                .selectFrom(qMember)
                .where(
                        qMember.username.contains("kim"),
                        qMember.age.eq(10)
                ).offset(10).limit(10)
                .orderBy( qMember.id.asc() , qMember.username.desc() )
                .fetch();
        Assertions.assertThat( findMember.size() ).isEqualTo( 10 );
        Assertions.assertThat( findMember.get(0).getUsername() ).isEqualTo( "kim20" );

        QueryModifiers queryModifiers = new QueryModifiers(10L,10L); //limit, offset
        findMember = queryFactory
                .selectFrom(qMember)
                .where(
                        qMember.username.contains("kim"),
                        qMember.age.eq(10)
                )
                .restrict( queryModifiers )
                .orderBy( qMember.id.asc() , qMember.username.desc() )
                .fetch();
        Assertions.assertThat( findMember.size() ).isEqualTo( 10 );
        Assertions.assertThat( findMember.get(0).getUsername() ).isEqualTo( "kim20" );
    }

    @Test
    void updateTest(){
        QMember member = QMember.member;
        JPAUpdateClause updateClause = new JPAUpdateClause(em,member);
        Long count = updateClause.where(member.username.eq("kim1"))
                .set(member.age,11)
                .execute();
        Member findMember = em.find(Member.class,"memberid1");
        Assertions.assertThat( findMember.getAge() ).isEqualTo( 11 );

        JPADeleteClause deleteClause = new JPADeleteClause(em,member);
        Long count2 = deleteClause.where(member.id.eq("memberid1"))
                .execute();
        em.flush();
        em.clear();

        Member findMember2 = em.find(Member.class,"memberid1");
        Assertions.assertThat( findMember2 ).isNull();
    }

    @Test
    void dynamicQuery(){
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();
        if( true ){
            builder.and( member.age.eq( 1) );
        }
        if( false ){
            builder.and( member.age.eq( 2 ));
        };

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        List<Member> findMember = queryFactory
                .selectFrom(member)
                .where(
                        builder
                )
                .fetch();
        Assertions.assertThat( findMember.get(0).getAge() ).isEqualTo( 1 );
    }
}
