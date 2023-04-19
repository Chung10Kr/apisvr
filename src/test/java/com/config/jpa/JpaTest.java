package com.config.jpa;

import com.api.member.dto.*;
import jakarta.persistence.*;
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
public class JpaTest {

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
    }
    @AfterEach
    public void close(){
        //this.tx.commit();//트랜잭션 커밋
        this.tx.rollback();
    }
    @AfterAll
    public void end(){
        this.em.close(); //엔티티 매니저 종료
        this.emf.close();
    }


    @Test
    
    public void EmbeddedTest(){
        Zipcode zipcode = Zipcode.builder().zip("22202").plusFour("엑슬뤁워 102동 2204호").build();
        Address address = Address.builder()
                .state("닛뽄바시")
                .city("오사카")
                .street("관동")
                .zipcode(zipcode)
                .build();
        Member member1 = Member.builder()
                .id("memberid1")
                .username("member1")
                .age(1)
                .address(address)
                .build();
        em.persist(member1);

        Member findMember = em.find(Member.class,"memberid1");
        Assertions.assertThat( findMember.getAddress().getStreet() ).isEqualTo( "관동" );
        Assertions.assertThat( findMember.getAddress().getCity() ).isEqualTo( "오사카" );
        Assertions.assertThat( findMember.getAddress().getZipcode().getZip() ).isEqualTo( "22202" );
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

    @Test
    
    public void cascadeTest(){
        Member member1 = Member.builder()
                .id("memberid1")
                .username("member1")
                .age(1)
                .build();
        Locker locker1 = Locker.builder().id(1L).name("사물함1").member(member1).build();
        em.persist(locker1);

        Assertions.assertThat( em.contains(member1) ).isTrue();
        Assertions.assertThat( em.contains(locker1) ).isTrue();
    }
    @Test
    
    public void proxyTest(){
        Team team1 = Team.builder()
                .id("team1")
                .name("팀1")
                .build();
        em.persist(team1);

        Member member1 = Member.builder()
                .id("memberid1")
                .username("member1")
                .age(1)
                .team(team1)
                .build();
        em.persist(member1);
        em.flush();
        em.clear();
        Member findMember = em.find(Member.class,"memberid1");

        Team findTeam = findMember.getTeam();
        Assertions.assertThat(findTeam.getName()).isEqualTo("팀1");
    }
    @Test
    public void testSave(){
        Team team1 = Team.builder()
                .id("team1")
                .name("팀1")
                .build();
        em.persist(team1);

        Member member1 = Member.builder()
                .id("memberid1")
                .username("member1")
                .age(1)
                .team(team1)
                .build();
        em.persist(member1);

        Member member2 = Member.builder()
                .id("memberid2")
                .username("member2")
                .age(2)
                .team(team1)
                .build();
        member2.setTeam(team1);
        member2.setAge(1);
        em.persist(member2);

        Assertions.assertThat(
                em.find(Member.class,"memberid1").getTeam().getName()
        ).isEqualTo("팀1");
        Assertions.assertThat(
                em.find(Member.class,"memberid2").getTeam().getName()
        ).isEqualTo("팀1");

        String jpgl = "select m from Member m join m.team t where t.name = :teamName";
        List<Member> resultList = em.createQuery(jpgl,Member.class)
                .setParameter("teamName" , "팀1")
                .getResultList();
        Assertions.assertThat( resultList.size() ).isEqualTo( 2 );

        em.flush();
        em.clear();

        Team findTeam = em.find(Team.class,"team1");
        Assertions.assertThat( findTeam.getMembers().size() ).isEqualTo( 2 );
    }

    @Test
    public void insertAndSelect() {

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        //등록
        em.persist(member);

        Member findMember = em.find(Member.class, id);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("지한");
        Assertions.assertThat(findMember.getAge()).isEqualTo(2);

        //수정
        member.setAge(20);
        /*
         * JPA는 어떤 엔티티가 변경되었는지 추적하는 기능을 갖추고 있다.
         * 따라서 member.setAge(20);처럼 엔티티의 값을 변경하면 update sql이 자동 실행된다.
         * */

        //한 건 조회
        findMember = em.find(Member.class, id);
        Assertions.assertThat(findMember.getUsername()).isEqualTo("지한");
        Assertions.assertThat(findMember.getAge()).isEqualTo(20);

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        //여기서 Member는 MEMBER테이블이 아니라 Member엔티티를 말한다.
        Assertions.assertThat(members.size()).isEqualTo(1);
        //삭제
        //em.remove(member);
    }

    @Test
    
    @DisplayName("detach일경우 영속성 컨텍스트에서 빠지는지 확인")
    public void detachTest(){

        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("재하");
        member.setAge(2);

        em.persist( member );

        Assertions.assertThat( em.contains(member) ).isTrue();

        em.detach( member );

        Assertions.assertThat( em.contains(member) ).isFalse();
        Assertions.assertThat( em.contains(member) ).isFalse();
    }


    @Test
    
    @DisplayName("clear일경우 영속성 컨텍스트에서 모두 빠지는지 확인")
    public void clearTest(){
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("재하");
        member.setAge(2);

        String id2 = "id2";
        Member member2 = new Member();
        member2.setId(id2);
        member2.setUsername("충렬");
        member2.setAge(30);

        em.persist( member );
        em.persist( member2 );

        em.contains(member);
        Assertions.assertThat( em.contains(member) ).isTrue();
        Assertions.assertThat( em.contains(member2) ).isTrue();

        em.clear();

        Assertions.assertThat( em.contains(member) ).isFalse();
        Assertions.assertThat( em.contains(member2) ).isFalse();
    }
}
