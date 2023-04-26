package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Getter
    private static long likeablePersonFromMaxPeople;

    @Value("${custom.likeablePerson.fromMaxPeople}")
    public void setLikeablePersonFromMaxPeople(long likeablePersonFromMaxPeople) {
        AppConfig.likeablePersonFromMaxPeople = likeablePersonFromMaxPeople;
    }
}
