package com.sparta.task.controller;

import com.sparta.task.dto.PostListResponseDto;
import com.sparta.task.dto.PostRequestDto;
import com.sparta.task.dto.PostResponseDto;
import com.sparta.task.dto.ResponseDto;
import com.sparta.task.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/post")
    public ResponseDto savePost(@RequestBody PostRequestDto requestDto, HttpServletRequest request){
        return taskService.savePost(requestDto,request);
    }

    @GetMapping("/posts")
    public PostListResponseDto getPosts(){
        return taskService.getPosts();
    }

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id){
        return taskService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        return taskService.updatePost(id, postRequestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public ResponseDto deletePost(@PathVariable Long id, HttpServletRequest request){
        return taskService.deletePost(id,request);
    }

}
