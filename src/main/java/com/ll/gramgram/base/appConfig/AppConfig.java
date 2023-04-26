package com.ll.gramgram.base.appConfig;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

public class AppConfig {

    @Getter
    private static long likeablePersonFromMaxPeople;

    @Value("${custom.likeablePerson.fromMaxPeople}")
    public void setLikeablePersonFromMaxPeople(long likeablePersonFromMaxPeople) {
        AppConfig.likeablePersonFromMaxPeople = likeablePersonFromMaxPeople;
    }
}
