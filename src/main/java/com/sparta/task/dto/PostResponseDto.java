package com.sparta.task.dto;

import com.sparta.task.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private String title;
    private String username;
    private String contents;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.createdAt = post.getCreatedAt();
    }
}
