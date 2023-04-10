package com.config.jpa;

import com.api.member.dto.Member;
import jakarta.persistence.*;
import org.apache.catalina.Lifecycle;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeEach
    public void init(){
        this.em = emf.createEntityManager();
    }
    @AfterAll
    public void close(){
        emf.close();
    }

    @Test
    public void JpaTest() {
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            logic(em);  //비즈니스 로직
            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

    }


    public void logic(EntityManager em) {

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
        em.remove(member);

        findMember = em.find(Member.class, id);
    }

    @Test
    @DisplayName("detach일경우 영속성 컨텍스트에서 빠지는지 확인")
    public void detachTest(){
            em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin(); //트랜잭션 시작

                String id = "id1";
                Member member = new Member();
                member.setId(id);
                member.setUsername("재하");
                member.setAge(2);

                em.persist( member );

                Assertions.assertThat( em.contains(member) ).isTrue();

                em.detach( member );

                Assertions.assertThat( em.contains(member) ).isFalse();

                tx.commit();//트랜잭션 커밋

                Assertions.assertThat( em.contains(member) ).isFalse();
            } catch (Exception e) {
                e.printStackTrace();
                tx.rollback(); //트랜잭션 롤백
            } finally {
                em.close(); //엔티티 매니저 종료
            }
        }


    @Test
    @DisplayName("clear일경우 영속성 컨텍스트에서 모두 빠지는지 확인")
    public void clearTest(){
        em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin(); //트랜잭션 시작

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

            tx.commit();//트랜잭션 커밋

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

    }
}
