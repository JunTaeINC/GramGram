package com.ll.gramgram.boundedContext.likeablePerson.entity;

import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class LikeablePerson extends BaseEntity {

    @ManyToOne
    @ToString.Exclude
    private InstaMember fromInstaMember; // 호감을 표시한 사람(인스타 멤버)
    private String fromInstaMemberUsername; // 혹시 몰라서 기록

    @ManyToOne
    @ToString.Exclude
    private InstaMember toInstaMember; // 호감을 받은 사람(인스타 멤버)
    private String toInstaMemberUsername; // 혹시 몰라서 기록
    private int attractiveTypeCode; // 매력포인트(1=외모, 2=성격, 3=능력)

    public String getAttractiveTypeDisplayName() {
        return switch (attractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public void updateAttractiveTypeCode(int attractiveTypeCode) {
        if (this.attractiveTypeCode != attractiveTypeCode) {
            toInstaMember.decreaseLikesCount(fromInstaMember.getGender(), this.attractiveTypeCode);
            toInstaMember.increaseLikesCount(fromInstaMember.getGender(), attractiveTypeCode);
            this.attractiveTypeCode = attractiveTypeCode;
        }
    }


    public String getAttractiveTypeDisplayNameWithIcon() {
        return switch (attractiveTypeCode) {
            case 1 -> "<i class=\"fa-solid fa-person-rays\"></i>";
            case 2 -> "<i class=\"fa-regular fa-face-smile\"></i>";
            default -> "<i class=\"fa-solid fa-people-roof\"></i>";
        } + "&nbsp;" + getAttractiveTypeDisplayName();
    }
}
