package com.sparta.task.service;

import com.sparta.task.dto.PostListResponseDto;
import com.sparta.task.dto.PostRequestDto;
import com.sparta.task.dto.PostResponseDto;
import com.sparta.task.dto.ResponseDto;
import com.sparta.task.entity.Post;
import com.sparta.task.repository.PostRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final PostRespository postRespository;
    @Transactional(readOnly = true)
    public PostListResponseDto getPosts() {
        PostListResponseDto postListResponseDto = new PostListResponseDto();
        List<Post> posts = postRespository.findAllByOrderByModifiedAtDesc();
        for (Post post : posts) {
            postListResponseDto.addPost(new PostResponseDto(post));
        }
        return postListResponseDto;
    }

    @Transactional
    public ResponseDto savePost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRespository.save(post);
        return new ResponseDto("게시글 등록 성공", HttpStatus.OK.value());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRespository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        ); // 이렇게 안하면 Optional 선언때문에
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post post = postRespository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 여기서 에러가 났던 이유는 ==으로 비교했기 때문이다. 객체의 값은 equals로 비교한다.
        // !(Not 연산자) 잊어버렸음.....
        System.out.println("----------------------------------------------------");
        System.out.println("post.getPassword() = " + post.getPassword());
        System.out.println("postRequestDto.getPassword() = " + postRequestDto.getPassword());
        System.out.println("----------------------------------------------------");
        if(!post.getPassword().equals(postRequestDto.getPassword())){
            System.out.println("여기서 에러가 났습니다!");
            throw new IllegalStateException("비밀번호가 일치하지 않습니다");
        }

        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    public ResponseDto deletePost(Long id) {
        postRespository.deleteById(id);
        return new ResponseDto("안전하게 삭제했습니다.", HttpStatus.OK.value());
    }
}
