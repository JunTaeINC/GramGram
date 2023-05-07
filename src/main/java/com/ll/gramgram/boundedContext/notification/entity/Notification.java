package com.ll.gramgram.boundedContext.notification.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.standard.util.Ut;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Notification extends BaseEntity {

    private LocalDateTime readDate;
    @ManyToOne
    @ToString.Exclude
    private InstaMember toInstaMember;
    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember;
    private String typeCode;
    private String oldGender;
    private String newGender;
    private int oldAttractiveTypeCode;
    private int newAttractiveTypeCode;

    public void markAsRead() {
        readDate = LocalDateTime.now();
    }

    public boolean isRead() {
        return readDate != null;
    }

    public String getCreateDateAfterStrHuman() {
        return Ut.time.diffFormat1Human(LocalDateTime.now(), getCreateDate());
    }

    public String getNewGenderDisplayName() {
        return switch (newGender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public String getNewAttractiveTypeDisplayName() {
        return switch (newAttractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public String getOldAttractiveTypeDisplayName() {
        return switch (oldAttractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public boolean isHot() {
        return getCreateDate().isAfter(LocalDateTime.now().minusMinutes(30));
    }
}
