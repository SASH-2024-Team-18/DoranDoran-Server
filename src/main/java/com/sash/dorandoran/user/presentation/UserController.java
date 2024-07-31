package com.sash.dorandoran.user.presentation;

import com.sash.dorandoran.common.annotation.AuthUser;
import com.sash.dorandoran.common.response.ResponseDto;
import com.sash.dorandoran.jwt.JwtProvider;
import com.sash.dorandoran.jwt.JwtResponse;
import com.sash.dorandoran.scrap.implement.ScrapService;
import com.sash.dorandoran.scrap.presentation.dto.ScrapSummaryResponse;
import com.sash.dorandoran.user.business.UserMapper;
import com.sash.dorandoran.user.domain.User;
import com.sash.dorandoran.user.implement.KakaoLoginService;
import com.sash.dorandoran.user.implement.NaverLoginService;
import com.sash.dorandoran.user.implement.UserService;
import com.sash.dorandoran.user.presentation.dto.DiarySummaryListResponse;
import com.sash.dorandoran.user.presentation.dto.JwtRequest;
import com.sash.dorandoran.user.presentation.dto.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "👤 User API")
@RestController
public class UserController {

    private final UserService userService;
    private final ScrapService scrapService;
    private final NaverLoginService naverLoginService;
    private final KakaoLoginService kakaoLoginService;
    private final JwtProvider jwtProvider;

    @GetMapping("/info")
    public ResponseDto<UserResponse> getUserInfo(@AuthUser User user) {
        List<Boolean> attendanceStatus = userService.getAttendanceStatus(user);
        return ResponseDto.onSuccess(UserMapper.toUserResponse(user, attendanceStatus));
    }

    @PostMapping("/attendance")
    public ResponseDto<Boolean> checkAttendance(@AuthUser User user) {
        userService.checkAttendance(user);
        return ResponseDto.onSuccess(true);
    }

    @PostMapping("/login/naver")
    public ResponseDto<JwtResponse> naverLogin(@RequestBody JwtRequest request) {
        return ResponseDto.onSuccess(naverLoginService.naverLogin(request));
    }

    @PostMapping("/login/kakao")
    public ResponseDto<JwtResponse> kakaoLogin(@RequestBody JwtRequest request) {
        return ResponseDto.onSuccess(kakaoLoginService.kakaoLogin(request));
    }

    @GetMapping("/test")
    public ResponseDto<JwtResponse> generateTestToken(@RequestParam Long userId) {
        return ResponseDto.onSuccess(jwtProvider.generateToken(userService.getUser(userId)));
    }

    @GetMapping("/diaries")
    public ResponseDto<DiarySummaryListResponse> getDiaries(@AuthUser User user) {
        return ResponseDto.onSuccess(userService.getDiaries(user));
    }

    @GetMapping("/scraps")
    public ResponseDto<List<ScrapSummaryResponse>> getScraps(@AuthUser User user) {
        return ResponseDto.onSuccess(scrapService.getScraps(user));
    }

}
