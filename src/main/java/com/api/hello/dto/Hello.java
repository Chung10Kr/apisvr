package com.api.hello.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hello {
    @NonNull
    private String name;
    private int count;

}
