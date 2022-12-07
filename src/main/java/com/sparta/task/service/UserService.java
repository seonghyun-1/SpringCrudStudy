package com.sparta.task.service;

import com.sparta.task.dto.ResponseDto;
import com.sparta.task.dto.SignupRequestDto;
import com.sparta.task.entity.User;
import com.sparta.task.jwt.JwtUtil;
import com.sparta.task.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @Transactional
    public ResponseDto signup( @Valid SignupRequestDto signupRequestDto) {
        // private은 당연히 안됨 -> 메소드 안이기 때문..!
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if(found.isPresent()){
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);
        return new ResponseDto("사용자 등록 성공", HttpStatus.OK.value());
    }

    @Transactional
    public ResponseDto login(SignupRequestDto signupRequestDto, HttpServletResponse response) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if(!user.getPassword().equals(password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername()));
        return new ResponseDto("로그인에 성공했습니다.",HttpStatus.OK.value());
    }
}
