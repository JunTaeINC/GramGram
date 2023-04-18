package com.ll.gramgram.boundedContext.member.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.member.entity.FindIdForm;
import com.ll.gramgram.boundedContext.member.entity.FindPasswordForm;
import com.ll.gramgram.boundedContext.member.entity.JoinForm;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member") // 액션 URL의 공통 접두어
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()") // 오직 로그인 안한 사람만 접근 가능하다.
    @GetMapping("/join") // 회원가입 폼
    public String showJoin() {
        return "usr/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) { // @Valid 가 없으면 @NotBlank 등이 작동하지 않음, 만약에 유효성 문제가 있다면 즉시 정지
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail());

        if (joinRs.isFail()) {
            // 뒤로가기 하고 거기서 메세지 보여줘
            return rq.historyBack(joinRs);
        }

        // 아래 링크로 리다이렉트(302, 이동) 하고 그 페이지에서 메세지 보여줘
        return rq.redirectWithMsg("/member/login", joinRs);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login") // 로그인 폼, 로그인 폼 처리는 스프링 시큐리티가 구현, 폼 처리시에 CustomUserDetailsService 가 사용됨
    public String showLogin() {
        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()") // 로그인 해야만 접속가능
    @GetMapping("/me") // 로그인 한 나의 정보 보여주는 페이지
    public String showMe() {
        return "usr/member/me";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findId")
    public String showFindId(FindIdForm findIdForm) {
        return "usr/member/findId";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findId")
    public String findId(@Valid FindIdForm findIdForm) {
        RsData findIdRs = memberService.findUserId(findIdForm.getEmail());

        if (findIdRs.isFail()) {
            return rq.historyBack(findIdRs);
        }

        return rq.redirectWithMsg("/member/login", findIdRs);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword(FindPasswordForm findPasswordForm) {
        return "usr/member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@Valid FindPasswordForm findPasswordForm) {
        RsData findPasswordRs = memberService.findUserPassword(findPasswordForm.getUsername(), findPasswordForm.getEmail());

        if (findPasswordRs.isFail()) {
            return rq.historyBack(findPasswordRs);
        }

        return rq.redirectWithMsg("/member/login", findPasswordRs);
    }

}
