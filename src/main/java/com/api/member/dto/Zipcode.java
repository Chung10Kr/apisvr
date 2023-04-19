package com.api.member.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

/**
 * fileName       : Zipcode
 * author         : crlee
 * date           : 2023/04/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/04/19        crlee       최초 생성
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Zipcode {
    String zip;
    String plusFour;
}
