package com.ll.gramgram.boundedContext.likeablePerson.entity;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.base.baseEntity.BaseEntity;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.standard.util.Ut;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

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

    private LocalDateTime modifyUnlockDate;

    public String getAttractiveTypeDisplayName() {
        return switch (attractiveTypeCode) {
            case 1 -> "외모";
            case 2 -> "성격";
            default -> "능력";
        };
    }

    public RsData updateAttractionTypeCode(int attractiveTypeCode) {
        if (this.attractiveTypeCode == attractiveTypeCode) {
            return RsData.of("F-1", "이미 설정되었습니다.");
        }

        this.attractiveTypeCode = attractiveTypeCode;
        this.modifyUnlockDate = AppConfig.genLikeablePersonModifyUnlockDate();

        return RsData.of("S-1", "성공");
    }

    public boolean isModifyUnlocked() {
        return modifyUnlockDate.isBefore(LocalDateTime.now());
    }

    public String getModifyUnlockDateRemainStrHuman() {
        //return Ut.time.diffFormat1Human(LocalDateTime.now(), modifyUnlockDate);

        // 수정가능한 시간
        LocalDateTime likeablePersonModifyReleasedDateTime = modifyUnlockDate;

        // 현재시간
        LocalDateTime now = LocalDateTime.now();
        // 수정가능한 시간 - 현재시간
        Duration duration = Duration.between(now, likeablePersonModifyReleasedDateTime);

        long hours = duration.toHours();
        long minutes = (long) Math.ceil((duration.getSeconds() / 60.0) % 60.0);

        if (minutes == 60) {
            hours += 1;
            minutes = 0;
        }

        return "%s시간 %s분".formatted(hours, minutes);

    }


    public String getAttractiveTypeDisplayNameWithIcon() {
        return switch (attractiveTypeCode) {
            case 1 -> "<i class=\"fa-solid fa-person-rays\"></i>";
            case 2 -> "<i class=\"fa-regular fa-face-smile\"></i>";
            default -> "<i class=\"fa-solid fa-people-roof\"></i>";
        } + "&nbsp;" + getAttractiveTypeDisplayName();
    }

    public String getJdenticon() {
        return Ut.hash.sha256(fromInstaMember.getId() + "_likes_" + toInstaMember.getId());
    }
}
