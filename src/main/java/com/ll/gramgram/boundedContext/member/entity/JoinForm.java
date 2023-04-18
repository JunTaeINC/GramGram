package com.ll.gramgram.boundedContext.member.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor // @Setter 도 가능, 데이터를 저장할 방편을 마련하기 위해서
@Getter // joinForm.getUsername() 이런 코드 가능하게
public class JoinForm {
    @NotBlank // 비어있지 않아야 하고, 공백으로만 이루어 지지도 않아야 한다.
    @Size(min = 4, max = 30) // 4자 이상, 30자 이하
    private final String username;
    @NotBlank
    @Size(min = 4, max = 30)
    private final String password;

    @NotBlank
    @Email
    @Size(min = 4, max = 30)
    private final String email;
}
