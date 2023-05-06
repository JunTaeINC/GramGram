package com.ll.gramgram.boundedContext.notification.controller;

import com.ll.gramgram.boundedContext.notification.entity.Notification;
import com.ll.gramgram.boundedContext.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private NotificationService notificationService;

    @Test
    @DisplayName("알림 목록에 접속시 'readDate : null -> now()'로 읽음처리 한다")
    @WithUserDetails("user4")
    void test001() throws Exception {

        List<Notification> notifications = notificationService.findByToInstaMember_username("insta_user4");

        long readCount = notifications.stream()
                .filter(notification -> !notification.isRead()) // 읽지 않은 것
                .count();

        assertThat(readCount).isEqualTo(1);

        ResultActions resultActions = mvc
                .perform(get("/usr/notification/list"))
                .andDo(print());

        readCount = notifications.stream()
                .filter(notification -> !notification.isRead())
                .count();



        assertThat(readCount).isEqualTo(0);
    }
}