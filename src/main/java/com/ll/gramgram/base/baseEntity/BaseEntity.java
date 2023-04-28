package com.ll.gramgram.base.baseEntity;


import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate // 아래 칼럼에는 값이 자동으로 들어간다.(INSERT 할 때)
    private LocalDateTime createDate;
    @LastModifiedDate // 아래 칼럼에는 값이 자동으로 들어간다.(UPDATE 할 때 마다)
    private LocalDateTime modifyDate;
}
