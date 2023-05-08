package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    int countByToInstaMember_username(String toInstaMemberUserName);

    LikeablePerson findByFromInstaMemberAndToInstaMember_username(InstaMember instaMember, String username);

    List<LikeablePerson> findByToInstaMember_username(String username);

    LikeablePerson findByFromInstaMemberIdAndToInstaMember_username(long idNumber, String username);

    Optional<LikeablePerson> findByFromInstaMember_usernameAndToInstaMember_username(String fromInstaMemberUsername, String toInstaMemberUsername);

    Optional<LikeablePerson> findQslByFromInstaMemberIdAndToInstaMember_username(long id, String username);
}
