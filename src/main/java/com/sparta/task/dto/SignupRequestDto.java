package com.sparta.task.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//- username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
//- password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)`로 구성되어야 한다.
public class SignupRequestDto {
    @Pattern(regexp = "[a-z0-9]{4,11}")
    private String username;
    @Pattern(regexp = "[a-zA-Z0-9]{8,16}")
    private String password;
}
