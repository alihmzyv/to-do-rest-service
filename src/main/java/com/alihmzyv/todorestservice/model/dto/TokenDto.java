package com.alihmzyv.todorestservice.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenDto {
    String name;
    String body;

    public static TokenDto accessToken(String body) {
        return TokenDto.builder()
                .name("Access-Token")
                .body(body)
                .build();
    }

    public static TokenDto refreshToken(String body) {
        return TokenDto.builder()
                .name("Refresh-Token")
                .body(body)
                .build();
    }
}
