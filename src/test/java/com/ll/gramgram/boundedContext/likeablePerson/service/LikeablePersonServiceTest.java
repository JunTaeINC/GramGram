package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.controller.LikeablePersonController;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LikeablePersonServiceTest {

    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private LikeablePersonRepository likeablePersonRepository;

    @Autowired
    private LikeablePersonController likeablePersonController;

    @Test
    void test001() throws Exception {
        // LikeablePerson ID = 1
        LikeablePerson likeablePerson = likeablePersonRepository.findById(1L).get();

        // LikeablePerson ID = 1 -> toInstaMember
        InstaMember toInstaMember = likeablePerson.getToInstaMember();

        // LikeablePerson ID = 1 -> toInstaMember -> toInstaMember name = insta_user4
        String toInstaMemberName = toInstaMember.getUsername();

        assertEquals(toInstaMemberName, "insta_user4");
    }

    @Test
    void test002() throws Exception {

        List<LikeablePerson> likeablePersonList = likeablePersonRepository.findByToInstaMember_username("insta_test4");

        assertThat(likeablePersonList.get(0).getId()).isEqualTo(7L);

        LikeablePerson likeablePerson = likeablePersonRepository.findByFromInstaMemberIdAndToInstaMember_username(2L, "insta_user100");

        assertThat(likeablePerson.getId()).isEqualTo(2);
    }

}