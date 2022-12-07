package com.sparta.task.controller;

import com.sparta.task.dto.ResponseDto;
import com.sparta.task.dto.SignupRequestDto;
import com.sparta.task.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    //
    @PostMapping("/signup")
    public ResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){ // valid는 컨트롤러에서만 동작한다.
        // jackson 이 json을 SignupRequestDto로 바꿔준다.
        return userService.signup(signupRequestDto);
    }

    // 로그인이 수행되면 쿠키안에 jwt토큰을 심어서 전달한다.
    @PostMapping("/login")
    public ResponseDto login(@RequestBody SignupRequestDto signupRequestDto, HttpServletResponse response){
        return userService.login(signupRequestDto, response);
    }
}
