package com.ll.gramgram.boundedContext.member.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FindIdForm {

    @NotBlank
    @Size(min = 4, max = 30)
    private final String email;
}