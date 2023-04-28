package com.ll.gramgram.boundedContext.likeablePerson.entity;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LikeForm {
    @NotBlank
    @Size(min = 3, max = 30)
    private final String username;
    @NotNull
    @Min(1)
    @Max(3)
    private final int attractiveTypeCode;
}