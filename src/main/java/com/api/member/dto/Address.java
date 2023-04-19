package com.api.member.dto;

import jakarta.persistence.*;
import lombok.*;

/**
 * fileName       : Address
 * author         : crlee
 * date           : 2023/04/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/19        crlee       최초 생성
 */
@Getter
/*
@Setter 임베디드 타입 같은 값 타입은 여러 엔티티에서 공유되면 위험하다.
만약에 공유가 되어 member1 들어있는 Address가 setCity()로 수정되여 member2에 들어간다면
member1에 있는 Address도 수정됨...그러니 공유하지 말고 같읕 값(인스턴스)가 필요하다면 복사 .clone()을 사용해야함
그러나 객체를 불변하게 만들면 값을 수정할 수 없으므로 부작용을 원천 차단 할 수 있다.( 불변 객체 )
1. 생성자로만 값을 설정하고 수정자를 만들지 않으면 된다.

*/

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Address {

    String street;
    String city;
    String state;
    @Embedded
    Zipcode zipcode;
}
