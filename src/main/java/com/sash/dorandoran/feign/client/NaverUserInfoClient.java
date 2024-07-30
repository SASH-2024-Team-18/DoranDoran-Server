package com.sash.dorandoran.feign.client;

import com.sash.dorandoran.feign.dto.NaverUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverUserInfo", url = "${naver.user-info-uri}")
public interface NaverUserInfoClient {

    @GetMapping
    NaverUserResponse getUserInfo(@RequestHeader("Authorization") String accessToken);

}