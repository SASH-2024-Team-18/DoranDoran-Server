package com.sash.dorandoran.user.implement;

import com.sash.dorandoran.feign.client.NaverUserInfoClient;
import com.sash.dorandoran.feign.dto.NaverUserResponse;
import com.sash.dorandoran.jwt.JwtProvider;
import com.sash.dorandoran.jwt.JwtResponse;
import com.sash.dorandoran.user.dao.UserRepository;
import com.sash.dorandoran.user.domain.AuthProvider;
import com.sash.dorandoran.user.domain.Role;
import com.sash.dorandoran.user.domain.User;
import com.sash.dorandoran.user.domain.UserLevel;
import com.sash.dorandoran.user.presentation.dto.JwtRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class NaverLoginService {

    private final NaverUserInfoClient naverUserInfoClient;
    private final UserRepository userRepository;
    private final NicknameGenerator nicknameGenerator;
    private final JwtProvider jwtProvider;

    @Transactional
    public JwtResponse naverLogin(JwtRequest request) {
        NaverUserResponse.NaverUserDetail profile = requestProfile(request.getAccessToken());
        Optional<User> optionalUser = userRepository.findByAuthProviderAndEmail(AuthProvider.NAVER, profile.getEmail());
        User user = optionalUser.orElseGet(() -> userRepository.save(buildUser(profile)));
        return jwtProvider.generateToken(user);
    }

    private NaverUserResponse.NaverUserDetail requestProfile(String accessToken) {
        NaverUserResponse response = naverUserInfoClient.getUserInfo("Bearer " + accessToken);
        return response.getNaverUserDetail();
    }

    private User buildUser(NaverUserResponse.NaverUserDetail profile) {
        String nickname = profile.getNickname();
        if (nickname == null || nickname.trim().isEmpty()) {
            nickname = nicknameGenerator.generateNickname();
        }

        return User.builder()
                .email(profile.getEmail())
                .nickname(nickname)
                .role(Role.MEMBER)
                .level(UserLevel.NONE)
                .authProvider(AuthProvider.NAVER)
                .build();
    }

}
