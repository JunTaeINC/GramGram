package com.ll.gramgram.boundedContext.likeablePerson.controller;

import com.ll.gramgram.base.rq.Rq;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeForm;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.entity.ModifyForm;
import com.ll.gramgram.boundedContext.likeablePerson.service.LikeablePersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/usr/likeablePerson")
@RequiredArgsConstructor
public class LikeablePersonController {
    private final Rq rq;
    private final LikeablePersonService likeablePersonService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/like")
    public String showAdd() {
        return "usr/likeablePerson/like";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/like")
    public String like(@Valid LikeForm likeForm) {
        RsData<LikeablePerson> createRsData = likeablePersonService.like(rq.getMember(), likeForm.getUsername(), likeForm.getAttractiveTypeCode());

        if (createRsData.isFail()) {
            return rq.historyBack(createRsData);
        }

        return rq.redirectWithMsg("/usr/likeablePerson/list", createRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String showList(Model model) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        // 인스타인증을 했는지 체크
        if (instaMember != null) {
            List<LikeablePerson> likeablePeople = instaMember.getFromLikeablePeople();
            model.addAttribute("likeablePeople", likeablePeople);
        }

        return "usr/likeablePerson/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable("id") Long id) {
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElse(null);

        RsData canDeleteRsData = likeablePersonService.canCancel(rq.getMember(), likeablePerson);

        if (canDeleteRsData.isFail()) return rq.historyBack(canDeleteRsData);

        RsData deleteRsData = likeablePersonService.cancel(likeablePerson);

        if (deleteRsData.isFail()) return rq.historyBack(deleteRsData);

        return rq.redirectWithMsg("/usr/likeablePerson/list", deleteRsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String showModify(@PathVariable Long id, Model model) {
        LikeablePerson likeablePerson = likeablePersonService.findById(id).orElseThrow();

        RsData canModifyRsData = likeablePersonService.canModifyLike(rq.getMember(), likeablePerson);

        if (canModifyRsData.isFail()) return rq.historyBack(canModifyRsData);

        model.addAttribute("likeablePerson", likeablePerson);

        return "usr/likeablePerson/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable Long id, @Valid ModifyForm modifyForm) {
        RsData<LikeablePerson> rsData = likeablePersonService.modifyAttractive(rq.getMember(), id, modifyForm.getAttractiveTypeCode());

        if (rsData.isFail()) {
            return rq.historyBack(rsData);
        }

        return rq.redirectWithMsg("/usr/likeablePerson/list", rsData);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/toList")
    public String showToList(Model model, @RequestParam(required = false) String gender, @RequestParam(required = false) String attractiveTypeCode,
                             @RequestParam(required = false) String sortCode) {
        InstaMember instaMember = rq.getMember().getInstaMember();

        if (instaMember == null) return "usr/likeablePerson/toList";


        List<LikeablePerson> likeablePeople = instaMember.getToLikeablePeople();

        if (gender == null || attractiveTypeCode == null || sortCode == null) {
            model.addAttribute("likeablePeople", likeablePeople);
            return "usr/likeablePerson/toList";
        }

        switch (gender) {
            case "M" -> likeablePeople = likeablePeople.stream()
                    .filter(likeablePerson -> likeablePerson.getFromInstaMember().getGender().equals("M"))
                    .collect(Collectors.toList());
            case "W" -> likeablePeople = likeablePeople.stream()
                    .filter(likeablePerson -> likeablePerson.getFromInstaMember().getGender().equals("W"))
                    .collect(Collectors.toList());
        }

        switch (attractiveTypeCode) {
            case "1" -> likeablePeople = likeablePeople.stream()
                    .filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 1)
                    .collect(Collectors.toList());
            case "2" -> likeablePeople = likeablePeople.stream()
                    .filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 2)
                    .collect(Collectors.toList());
            case "3" -> likeablePeople = likeablePeople.stream()
                    .filter(likeablePerson -> likeablePerson.getAttractiveTypeCode() == 3)
                    .collect(Collectors.toList());
        }
        // 최신순 = 1(기본) / 날짜순 = 2 / 인기 많은순 = 3 / 인기 적은순 = 4 / 성별순 = 5 / 호감사유순 = 6
        likeablePeople = likeablePeople.stream()
                .sorted(Comparator.comparing(LikeablePerson::getCreateDate).reversed())
                .collect(Collectors.toList());
        switch (sortCode) {
            case "2" -> likeablePeople = likeablePeople.stream()
                    .sorted(Comparator.comparing(LikeablePerson::getCreateDate))
                    .collect(Collectors.toList());
            case "3" -> likeablePeople = likeablePeople.stream()
                    .sorted((e1, e2) -> e2.getFromInstaMember().getToLikeablePeople().size() - e1.getFromInstaMember().getToLikeablePeople().size())
                    .collect(Collectors.toList());
            case "4" -> likeablePeople = likeablePeople.stream()
                    .sorted(Comparator.comparingInt(e -> e.getFromInstaMember().getToLikeablePeople().size()))
                    .collect(Collectors.toList());
            case "5" -> likeablePeople = likeablePeople.stream()
                    .sorted(Comparator.comparing((LikeablePerson e) -> e.getFromInstaMember().getGender().equals("W")).reversed())
                    .collect(Collectors.toList());
            case "6" -> likeablePeople = likeablePeople.stream()
                    .sorted(Comparator.comparingInt(LikeablePerson::getAttractiveTypeCode))
                    .collect(Collectors.toList());
        }
        model.addAttribute("likeablePeople", likeablePeople);

        return "usr/likeablePerson/toList";
    }
}