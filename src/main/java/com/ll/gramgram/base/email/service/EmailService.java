package com.ll.gramgram.base.email.service;

import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(Member member) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(member.getEmail());

        simpleMailMessage.setSubject("회원가입을 축하드립니다.");

        simpleMailMessage.setText("%s님 환영합니다. 좋은 컨텐츠가 되도록 노력하겠습니다.".formatted(member.getUsername()));

        javaMailSender.send(simpleMailMessage);
    }

    public void sendUserId(Member member) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(member.getEmail());

        simpleMailMessage.setSubject("아이디 찾기");

        simpleMailMessage.setText("등록된 이메일에 연동된 아이디는 (%s)입니다.".formatted(member.getUsername()));

        javaMailSender.send(simpleMailMessage);
    }
}
