package com.sparta.task.service;

import com.sparta.task.dto.PostListResponseDto;
import com.sparta.task.dto.PostRequestDto;
import com.sparta.task.dto.PostResponseDto;
import com.sparta.task.dto.ResponseDto;
import com.sparta.task.entity.Post;
import com.sparta.task.entity.User;
import com.sparta.task.jwt.JwtUtil;
import com.sparta.task.repository.PostRespository;
import com.sparta.task.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final PostRespository postRespository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

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
    public ResponseDto savePost(PostRequestDto requestDto, HttpServletRequest request) {

        // requestDto 안에는
        // title, username, content가 있고
        // request속 토큰안에는
        // {
        //      "sub": "kimkim",
        //      "auth": "USER",
        //      "exp": 1670419252,
        //      "iat": 1670415652
        //  }
        // 가 들어있다.

        // Request에서 Token가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 토큰이 있는 경우에만 게시글 작성가능!
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            //User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
            //        () -> new IllegalArgumentException("사용자가 존재하지 않습니다")
            //);

            Post post = new Post(requestDto, claims.getSubject());
            postRespository.save(post);
            return new ResponseDto("게시글 등록 성공", HttpStatus.OK.value());
        }
        return new ResponseDto("토큰이 null값 입니다.",HttpStatus.NO_CONTENT.value());
    }

    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRespository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        ); // 이렇게 안하면 Optional 선언때문에
        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {

        // Request에서 Token가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 체크1 - 토큰이 있는 경우에만 게시글 수정가능
        if(token == null) {
            throw new IllegalArgumentException("토큰값이 존재하지 않습니다.");
        }

        // 체크2 - 유효성 검증
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        claims = jwtUtil.getUserInfoFromToken(token);
        Post post = postRespository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 작성자가 같지 않으면 에러발생
        System.out.println("claims.getSubject() = " + claims.getSubject());
        System.out.println("post.getUsername() = " + post.getUsername());
        if(!claims.getSubject().equals(post.getUsername())){
            throw new IllegalArgumentException("아이디가 일치하지 않습니다!");
        }

        post.update(postRequestDto);
        return new PostResponseDto(post);
    }

    @Transactional
    public ResponseDto deletePost(Long id, HttpServletRequest request) {

        // Request에서 Token가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // 체크1 - 토큰이 있는 경우에만 게시글 수정가능
        if(token == null) {
            throw new IllegalArgumentException("토큰값이 존재하지 않습니다.");
        }

        // 체크2 - 유효성 검증
        if(!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        claims = jwtUtil.getUserInfoFromToken(token);
        Post post = postRespository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        // 체크3 - 해당 사용자가 만든 글인지 확인
        if(!claims.getSubject().equals(post.getUsername())){
            throw new IllegalArgumentException("지우려는 사용자가 쓴 글이 아닙니다.");
        }

        postRespository.deleteById(id);
        return new ResponseDto("안전하게 삭제했습니다.", HttpStatus.OK.value());
    }
}
