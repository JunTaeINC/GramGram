package com.ll.gramgram.boundedContext.instaMember.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class InstaMemberSnapshot extends InstaMemberBase {

    private String username;
    private String eventTypeCode;

    @ToString.Exclude // ToString() 에서 제외
    @ManyToOne
    private InstaMember instaMember;
}
