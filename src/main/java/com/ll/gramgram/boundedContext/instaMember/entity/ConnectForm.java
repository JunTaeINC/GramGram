package com.ll.gramgram.boundedContext.instaMember.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConnectForm {
    @NotBlank
    @Size(min = 3, max = 30)
    private final String username;
    @NotBlank
    @Size(min = 1, max = 1)
    private final String gender;
}