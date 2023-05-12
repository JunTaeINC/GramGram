package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.appConfig.AppConfig;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LikeablePersonControllerTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private LikeablePersonRepository likeablePersonRepository;

    @Test
    @DisplayName("등록 폼(인스타 인증을 안해서 폼 대신 메세지)")
    @WithUserDetails("user1")
    void t001() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/like"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showAdd"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        먼저 본인의 인스타 아이디를 입력해주세요.
                        """.stripIndent().trim())))
        ;
    }

    @Test
    @DisplayName("등록 폼")
    @WithUserDetails("user2")
    void t002() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/like"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showAdd"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="text" name="username"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="1"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="2"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="3"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        id="btn-like-1"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("등록 폼 처리(user2가 user3에게 호감표시(외모))")
    @WithUserDetails("user2")
    void t003() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user3")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("등록 폼 처리(user2가 abcd에게 호감표시(외모), abcd는 아직 우리 서비스에 가입하지 않은상태)")
    @WithUserDetails("user2")
    void t004() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "abcd")
                        .param("attractiveTypeCode", "2")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("수정 폼")
    @WithUserDetails("user3")
    void t005() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/modify/2"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="1"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="2"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        <input type="radio" name="attractiveTypeCode" value="3"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        inputValue__attractiveTypeCode = 2;
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        id="btn-modify-like-1"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("수정 폼 처리")
    @WithUserDetails("user3")
    void t006() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/modify/2")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "abcd")
                        .param("attractiveTypeCode", "3")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("호감목록")
    @WithUserDetails("user3")
    void t007() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/list"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_username=insta_user4"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_attractiveTypeDisplayName=외모"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_username=insta_user100"
                        """.stripIndent().trim())))
                .andExpect(content().string(containsString("""
                        data-test="toInstaMember_attractiveTypeDisplayName=성격"
                        """.stripIndent().trim())));
    }

    @Test
    @DisplayName("호감취소")
    @WithUserDetails("user3")
    void t008() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/usr/likeablePerson/cancel/1")
                                .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/usr/likeablePerson/list**"))
        ;

        assertThat(likeablePersonService.findById(1L).isPresent()).isEqualTo(false);
    }

    @Test
    @DisplayName("호감취소(없는거 취소, 취소가 안되어야 함)")
    @WithUserDetails("user3")
    void t009() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/usr/likeablePerson/cancel/100")
                                .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    @DisplayName("호감취소(권한이 없는 경우, 취소가 안됨)")
    @WithUserDetails("user2")
    void t010() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(
                        post("/usr/likeablePerson/cancel/1")
                                .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().is4xxClientError())
        ;

        assertThat(likeablePersonService.findById(1L).isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("인스타아이디가 없는 회원은 대해서 호감표시를 할 수 없다.")
    @WithUserDetails("user1")
    void t011() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user4")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("본인이 본인에게 호감표시하면 안된다.")
    @WithUserDetails("user3")
    void t012() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user3")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("특정인에 대해서 호감표시를 중복으로 시도하면 안된다.")
    @WithUserDetails("user3")
    void t013() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user4")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("한 회원은 호감표시 할 수 있는 최대 인원이 정해져 있다.")
    @WithUserDetails("user5")
    void t014() throws Exception {
        Member memberUser5 = memberService.findByUsername("user5").get();

        IntStream.range(0, (int) AppConfig.getLikeablePersonFromMaxPeople())
                .forEach(index -> {
                    likeablePersonService.like(memberUser5, "insta_user%30d".formatted(index), 1);
                });

        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user111")
                        .param("attractiveTypeCode", "1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("기존에 호감을 표시한 유저에게 새로운 사유로 호감을 표시하면 추가가 아니라 수정이 된다.")
    @WithUserDetails("user3")
    void t015() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/like")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user4")
                        .param("attractiveTypeCode", "2")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(status().is3xxRedirection());

        Optional<LikeablePerson> opLikeablePerson = likeablePersonService.findByFromInstaMember_usernameAndToInstaMember_username("insta_user3", "insta_user4");

        int newAttractiveTypeCode = opLikeablePerson
                .map(LikeablePerson::getAttractiveTypeCode)
                .orElse(-1);

        assertThat(newAttractiveTypeCode).isEqualTo(2);
    }

    @Test
    @DisplayName("호감사유 변경시 쿨타임 적용")
    @WithUserDetails("user5")
    void t016() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/modify/3")
                        .with(csrf()) // CSRF 키 생성
                        .param("username", "insta_user119")
                        .param("attractiveTypeCode", "3")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(status().is4xxClientError());

        assertThat(likeablePersonService.findById(3L).get().getAttractiveTypeCode()).isEqualTo(2);
    }

    @Test
    @DisplayName("호감표시 삭제시 쿨타임 적용")
    @WithUserDetails("user5")
    void t017() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/usr/likeablePerson/cancel/3")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().is4xxClientError());

        assertThat(likeablePersonService.findById(3L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("user2 호감표시자의 전체 항목의 수 = 3")
    @WithUserDetails("user2")
    void t018() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(3)));
    }

    @Test
    @DisplayName("user2 호감표시자의 남성 항목의 수 = 1")
    @WithUserDetails("user2")
    void t019() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .param("gender", "M")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(1)));
    }

    @Test
    @DisplayName("user2 호감표시자의 여성 항목의 수 = 2")
    @WithUserDetails("user2")
    void t020() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .param("gender", "W")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(2)));
    }

    @Test
    @DisplayName("user2 호감표시자의 호감사유(외모 : 1)인 항목의 수 = 2")
    @WithUserDetails("user2")
    void t021() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .param("attractiveTypeCode", "1")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(2)));
    }

    @Test
    @DisplayName("user2 호감표시자의 성별이 여성이고 호감사유(외모 : 1)인 항목의 수 = 2")
    @WithUserDetails("user2")
    void t022() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .param("gender", "W")
                        .param("attractiveTypeCode", "1")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(2)));
        ;
    }

    @Test
    @DisplayName("user2 호감표시자의 성별이 여성이고 호감사유(능력 : 3)인 항목의 수 = 0")
    @WithUserDetails("user2")
    void t023() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/usr/likeablePerson/toList")
                        .param("gender", "W")
                        .param("attractiveTypeCode", "3")
                        .with(csrf())
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(handler().handlerType(LikeablePersonController.class))
                .andExpect(handler().methodName("showToList"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("likeablePeople", hasSize(0)));
        ;
    }

    @Test
    @DisplayName("user2 최신순으로 정렬")
    @WithUserDetails("user2")
    void t024() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "1"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
    }

    @Test
    @DisplayName("user2 오래된순으로 정렬")
    @WithUserDetails("user2")
    void t025() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "2"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
    }

    @Test
    @DisplayName("user2 인기 많은 순 정렬")
    @WithUserDetails("user2")
    void t026() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "3"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
    }

    @Test
    @DisplayName("user2 인기 적은 순 정렬 / 2순위 최신순")
    @WithUserDetails("user2")
    void t027() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "4"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
    }

    @Test
    @DisplayName("user2 성별 순 / 2순위 최신순")
    @WithUserDetails("user2")
    void t028() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "5"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
    }

    @Test
    @DisplayName("user2 호감사유순 / 2순위 최신순")
    @WithUserDetails("user2")
    void t029() throws Exception {
        // WHEN
        MvcResult result = mvc.perform(get("/usr/likeablePerson/toList")
                        .param("sortCode", "6"))
                .andExpect(status().isOk())
                .andReturn();

        // THEN
        List<LikeablePerson> modelLikeablePeople = (List<LikeablePerson>) result.getModelAndView().getModel().get("likeablePeople");
        LikeablePerson firstLikeablePerson = modelLikeablePeople.get(0);
        LikeablePerson twiceLikeablePerson = modelLikeablePeople.get(1);
        LikeablePerson thridLikeablePerson = modelLikeablePeople.get(2);

        assertThat(firstLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user5");
        assertThat(twiceLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user3");
        assertThat(thridLikeablePerson.getFromInstaMemberUsername()).isEqualTo("insta_user4");
    }
}