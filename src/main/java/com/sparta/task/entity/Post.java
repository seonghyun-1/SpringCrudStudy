package com.sparta.task.entity;


import com.sparta.task.dto.PostRequestDto;
import com.sparta.task.dto.PostResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String username;

    //@Column(nullable = false)
    //private String password;
    @Column(nullable = false)
    private String contents;

    public Post(PostRequestDto requestDto, String username) {
        this.title = requestDto.getTitle();
        //this.username = requestDto.getUsername();
        this.username = username;
        //this.password = requestDto.getPassword();
        this.contents = requestDto.getContents();
    }


    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
