package com.ll.gramgram.boundedContext.notification.service;

import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import com.ll.gramgram.boundedContext.notification.entity.Notification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class NotificationServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("호감 표시시 알림 생성")
    void test001() throws Exception {
        Member member3 = memberService.findByUsername("user3").orElseThrow();
        Member member5 = memberService.findByUsername("user5").orElseThrow();

        likeablePersonService.like(member3, member5.getInstaMember().getUsername(), 1);

        List<Notification> notifications = notificationService.findByToInstaMember(member5.getInstaMember());

        Notification lastNotification = notifications.get(0);

        assertThat(lastNotification.getTypeCode()).isEqualTo("LIKE");
        assertThat(lastNotification.getFromInstaMember().getUsername()).isEqualTo("insta_user3");
        assertThat(lastNotification.getNewAttractiveTypeCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("호감사유 수정시 알림 생성")
    void test002() throws Exception {
        Member member3 = memberService.findByUsername("user3").orElseThrow();
        Member member4 = memberService.findByUsername("user4").orElseThrow();

        likeablePersonService.modifyAttractive(member3, member4.getInstaMember().getUsername(), 3);

        List<Notification> notifications = notificationService.findByToInstaMember(member4.getInstaMember());

        Notification lastNotification = notifications.get(0);

        assertThat(lastNotification.getTypeCode()).isEqualTo("MODIFY_ATTRACTIVE_TYPE");
        assertThat(lastNotification.getOldAttractiveTypeCode()).isEqualTo(1);
        assertThat(lastNotification.getNewAttractiveTypeCode()).isEqualTo(3);
    }
}