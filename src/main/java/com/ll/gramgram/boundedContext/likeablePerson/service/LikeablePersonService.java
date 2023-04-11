package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if (!member.hasConnectedInstaMember()) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        if (member.getInstaMember().getFromLikeablePeople().size() > 9) {
            return RsData.of("F-4", "호감상대는 최대 10명까지 등록이 가능합니다.");
        }

        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();
        InstaMember fromInstaMember = member.getInstaMember();

        Optional<LikeablePerson> opToInstaMember = likeablePersonRepository.findByToInstaMember(toInstaMember);

        if (opToInstaMember.isPresent() && opToInstaMember.get().getAttractiveTypeCode() == attractiveTypeCode) {
            return RsData.of("F-3", "이미 '%s'님은 호감상대로 등록되어있습니다.".formatted(username));
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장
        fromInstaMember.addFromLikeablePerson(likeablePerson);
        toInstaMember.addToLikeablePerson(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    @Transactional
    public RsData delete(Long id, InstaMember instaMember) {
        LikeablePerson likeablePerson = getLikeablePerson(id);
        // 객체 비교시 != -> !Objects.equals (A, B)
        if (!Objects.equals(instaMember.getId(), likeablePerson.getFromInstaMember().getId())) {
            return RsData.of("F-1", "호감상대를 삭제할 권한이 없습니다.");
        }

        likeablePersonRepository.delete(likeablePerson);

        return RsData.of("S-1", "호감상대(%s)가 삭제되었습니다."
                .formatted(likeablePerson.getToInstaMember().getUsername()));
    }

    public LikeablePerson getLikeablePerson(Long id) {
        Optional<LikeablePerson> likeablePerson = likeablePersonRepository.findById(id);

        if (likeablePerson.isEmpty()) throw new RuntimeException("LikeablePerson not found");

        return likeablePerson.get();
    }

    public Optional<LikeablePerson> findById(Long id) {
        return likeablePersonRepository.findById(id);
    }
}
