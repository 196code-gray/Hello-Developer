package server.mainproject.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import server.mainproject.post.dto.PostDto;
import server.mainproject.post.entity.Likes;
import server.mainproject.post.entity.Post;
import server.mainproject.post.mapper.PostMapper;
import server.mainproject.post.service.PostService;
import server.mainproject.response.MultiResponse;
import server.mainproject.tag.Tag;
import server.mainproject.tag.TagRepository;
import server.mainproject.utils.URICreator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

// todo : 회원이 탈퇴되도 글은 유지.
@RestController
@RequestMapping("/post")
@Validated
@RequiredArgsConstructor
public class PostController {
    private final PostService service;
    private final PostMapper mapper;
    private final TagRepository tagRepository;

    @PostMapping
    public ResponseEntity postPost (@RequestBody @Valid PostDto.Post post) {

        Post mappPost = mapper.postToEntity(post);
        List<Tag> tags = new ArrayList<>();

        for (String tagName : post.getTag()) {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }

        Post create = service.createdPost(mappPost, tags);
        URI uri = URICreator.createUri("/post", create.getPostId());

        return ResponseEntity.created(uri).build();
    }
    @PatchMapping("/{post-id}/edit/{member-id}")    // todo : security 적용 후 memberId 는 제거
    public ResponseEntity updatePost (@PathVariable("post-id") @Positive long postId,
                                      @PathVariable("member-id") @Positive long memberId,
                                      @RequestBody @Valid PostDto.Patch patch) {
        patch.setPostId(postId);
        patch.setMemberId(memberId);

        Post update = service.updatePost(mapper.patchToEntity(patch));

        return new ResponseEntity(mapper.EntityToResponse(update), HttpStatus.OK);
    }
    // 최신 글 순
    @GetMapping("/new_post") // page : 1 size : 9-16 1페이지에 9-16 개 정도의 게시물. 일단 16으로
    public ResponseEntity getAllNewPost(@RequestParam(defaultValue = "1") @Positive int page,
                                        @RequestParam(defaultValue = "16") @Positive int size) {

        Page<Post> posts = service.findAllPost (page -1, size);
        List<Post> list = posts.getContent();

        return new ResponseEntity(new MultiResponse<>(mapper.ListResponse(list), posts), HttpStatus.OK);
    }
    // 평점이 높은 순
    @GetMapping("/top_review")
    public ResponseEntity getAllTopPost (@RequestParam(defaultValue = "1") @Positive int page,
                                         @RequestParam(defaultValue = "16") @Positive int size) {
        Page<Post> posts = service.findAllTopPost (page -1, size);
        List<Post> list = posts.getContent();

        return new ResponseEntity(new MultiResponse<>(mapper.ListResponse(list), posts), HttpStatus.OK);
    }
    @GetMapping("/{post-id}")
    public ResponseEntity getPost (@PathVariable("post-id") @Positive long postId) {

        Post find = service.findPost(postId);

        return new ResponseEntity(mapper.EntityToResponse(find), HttpStatus.OK);
    }
    @DeleteMapping("/{post-id}/{member-id}") // todo : security 적용 후 member-id 는 제거
    public ResponseEntity deletePost (@PathVariable("post-id") @Positive long postId,
                                      @PathVariable("member-id") @Positive long memberId) {

        service.deletePost(postId, memberId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/{post-id}/likes/{member-id}")    // todo : security 적용 후 member 제거
    public ResponseEntity likesPost (@PathVariable("post-id") @Positive long postId,
                                     @PathVariable("member-id") @Positive long memberId) {

        Likes create = service.createLikes(postId, memberId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{post-id}/likes/{member-id}")      // todo : member 제거
    public ResponseEntity unLikesPost (@PathVariable("post-id") @Positive long postId,
                                       @PathVariable("member-id") @Positive long memberId) {

        service.unLikesPost(postId, memberId);
        return ResponseEntity.ok().build();
    }

}
