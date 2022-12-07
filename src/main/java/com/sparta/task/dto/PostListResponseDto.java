package com.sparta.task.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PostListResponseDto extends ResponseDto{
    List<PostResponseDto> postList = new ArrayList<>();
    public PostListResponseDto() {
        super("게시물 목록 조회 성공", HttpStatus.OK.value());
    }
    public void addPost(PostResponseDto postResponseDto){
        postList.add(postResponseDto);
    }
}
