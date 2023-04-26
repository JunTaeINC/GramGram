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
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getEmail());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs);
        }

        return rq.redirectWithMsg("/usr/member/login", joinRs);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
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

        return rq.redirectWithMsg("/usr/member/login", findIdRs);
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

        return rq.redirectWithMsg("/usr/member/login", findPasswordRs);
    }

}
