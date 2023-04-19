package com.api.member.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * fileName       : Member
 * author         : crlee
 * date           : 2023/04/10
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/10        crlee       최초 생성
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "team" , "locker"})
@Entity
@Table(name="MEMBER", uniqueConstraints = {
        @UniqueConstraint(
            name = "NAME_AGE_UNIQUE",
            columnNames = {"NAME", "AGE"}
        )
})
public class Member {
    @Id
    //@Column(name="ID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    /*
    * 엔티티가 영속 상태가 되려면 식별자가 반드시 필요하다.
    * 그런다 IDENTITY전략은 식별자 생성을 데이터베이스에 저장해야 식별자를 구할 수 있다( 데이터베이스 의존 )
    * 그렇기 떄문에 em.persist()를 호출하는 즉시 insert query가 데이터베이스에 전달된다.
    * 따라서 이 전략은 트랜잭션을 지원하는 쓰기 지연이 동작하지 않는다.
    *
    * SEQUENCE전략은 persist()를 호출할때 식별자를 조회하여 엔티티에 할당한 후
    * 영속성 컨텍스트에 저장한다. 이후 트랜잭션을 커밋해서 플러시하면
    * 엔티티를 데이터베이스에 저장한다.
    * */

    @Column(name="NAME", nullable = false, length = 10)
    private String username;

    private Integer age;

    //@Enumerated(EnumType.STRING)
    @Enumerated(EnumType.ORDINAL)
    private RoleType roleType;
    /*
    * ORDINAL : enum에 정의된 순서대로 ADMIN:0, USER:1로 저장된다. -> 저장된 데이터 크기는 작지만 이미 저장던 enum의 순서를 변경 할 수 없다.
    * String : 'ADMIN','USER' 문자 그대로 저장된다. -> 저장된 데이터 크기는 크지만 저장된 enum순서가 바뀌어도 안전
    * */

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    @Transient
    private String temp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TEAM_ID")
    private Team team;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="LOCKER_ID")
    private Locker locker;

    @Embedded Address address;

}
