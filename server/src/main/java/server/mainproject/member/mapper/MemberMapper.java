package server.mainproject.member.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import server.mainproject.comment.dto.CommentDto;
import server.mainproject.comment.entity.Comment;
import server.mainproject.member.dto.AuthorResponseDto;
import server.mainproject.member.dto.MemberDto;
import server.mainproject.member.entity.Member;
import server.mainproject.post.dto.DevPostDto;
import server.mainproject.post.dto.Post_TagResponseDto;
import server.mainproject.post.dto.RecommendResponseDto;
import server.mainproject.post.entity.DevPost;
import server.mainproject.post.entity.Recommend;
import server.mainproject.tag.Post_Tag;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post requestBody);

    Member memberPatchDtoToMember(MemberDto.Patch requestBody);

    MemberDto.Response memberToMemberResponseDto(Member member);

//    default public MemberMyPageDto memberToMemberMyPageDto(Member member) {
//        if ( member == null ) {
//            return null;
//        }
//
//        MemberMyPageDto memberMyPageDto = new MemberMyPageDto();
//        memberMyPageDto.setMemberId(member.getMemberId());
//        memberMyPageDto.setEmail(member.getEmail());
//        memberMyPageDto.setUserName(member.getUserName());
//        memberMyPageDto.setCreatedAt(member.getCreatedAt());
//        memberMyPageDto.setModifiedAt(member.getModifiedAt());
//
//        List<DevPost> devPosts = member.getPosts();
//        List<PostMyPageDto> postMyPageDtos = devPosts.stream().map(post ->{
//            PostMyPageDto postMyPageDto = new PostMyPageDto();
//            postMyPageDto.setPostId(post.getPostId());
//            postMyPageDto.setTitle(post.getTitle());
//            postMyPageDto.setContent(post.getContent());
//            postMyPageDto.setLink(post.getLink());
//            postMyPageDto.setStar(post.getStar());
//            postMyPageDto.setStarAvg(post.getStarAvg());
//            postMyPageDto.setRecommend(post.getRecommend());
//            postMyPageDto.setPostTags(postTagDtoResponse(post.getPostTags()));
//
//            return postMyPageDto;
//        }).collect(Collectors.toList());
//
//        memberMyPageDto.setPosts(postMyPageDtos);
//
//        List<Comment> comments = member.getComments();
//        List<CommentMyPageDto> commentMyPageDtos = comments.stream().map(comment ->{
//            CommentMyPageDto commentMyPageDto = new CommentMyPageDto();
//            commentMyPageDto.setMemberId(commentMyPageDto.getMemberId());
//            commentMyPageDto.setUserName(commentMyPageDto.getUserName());
//            commentMyPageDto.setPostId(commentMyPageDto.getPostId());
//            commentMyPageDto.setCommentId(commentMyPageDto.getCommentId());
//            commentMyPageDto.setContent(commentMyPageDto.getContent());
//            commentMyPageDto.setStar(commentMyPageDto.getStar());
//
//            return commentMyPageDto;
//        }).collect(Collectors.toList());
//
//        memberMyPageDto.setComments(commentMyPageDtos);
//
//        return memberMyPageDto;
//    }

    List<Member> membersToMemberReponseDtos(List<Member> members);


    //Todo : DevPost 수동매핑
    default DevPostDto.Response devPostToResponse(DevPost devPost) {
        if ( devPost == null ) {
            return null;
        }

        double answersReview = devPost.getComments()
                .stream()
//                .filter(id -> id.getDevPost().getPostId() == postId)
                .map(review -> review.getStar())
                .mapToDouble(avr -> avr)
                .average()
                .orElse(0.0);

        DecimalFormat df = new DecimalFormat("#.#");
        String formattedReview = df.format(answersReview);
        double roundedReview = Double.parseDouble(formattedReview);

        Long postId = null;
        String title = null;
        String content = null;
        String link = null;
        int star = 0;
        Double starAvg = null;
        int recommend = 0;
        String source;
        List<Post_TagResponseDto> postTags = null;
        List<CommentDto.ResponseComment> comments = null;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;


        postId = devPost.getPostId();
        title = devPost.getTitle();
        content = devPost.getContent();
        link = devPost.getLink();
        star = devPost.getStar();
        starAvg = roundedReview;
        recommend = devPost.getRecommend();
        source = devPost.getSource();
        postTags = postTagDtoResponse( devPost.getPostTags() );
        comments = commentListToResponseCommentList( devPost.getComments() );
        createdAt = devPost.getCreatedAt();
        modifiedAt = devPost.getModifiedAt();

        String status = "success";
        List<AuthorResponseDto> authors = postMemberDtoResponse (devPost);

        DevPostDto.Response response = new DevPostDto.Response( status, postId, title, content, link, star, starAvg, recommend,source, authors, postTags, comments,createdAt, modifiedAt );

        return response;
    }


    //Todo : PostTag 수동 매핑
    default List<Post_TagResponseDto> postTagDtoResponse (Set<Post_Tag> postTags) {
        List<Post_TagResponseDto> result = new ArrayList<>();
        List<String> tagName = postTags.stream().map(tag -> tag.getTag().getName())
                .collect(Collectors.toList());

        result.add(Post_TagResponseDto
                .builder()
                .tags(tagName)
                .build());

        return result;

    }

    //Todo : Recommend 수동 매핑
    default List<RecommendResponseDto> getRecommendResponseDtos(List<Recommend> recommends) {

        return recommends.stream()
                .map(recommend -> new RecommendResponseDto(
                        recommend.getRecommendsId(),
                        recommend.getPost().getPostId(),
                        recommend.getMember().getMemberId(),
                        recommend.getPost().getTitle(),
                        recommend.getPost().getLink(),
                        recommend.getPost().getStar(),
                        recommend.getPost().getComments()
                                .stream()
                                .map(review -> review.getStar())
                                .mapToDouble(avr -> avr)
                                .average()
                                .orElse(0.0),
                        recommend.getPost().getRecommend(),
                        postMemberDtoResponse (recommend.getPost()),
                        postTagDtoResponse(recommend.getPost().getPostTags()),
                        commentListToResponseCommentList(recommend.getPost().getComments() )

                ))
                .collect(Collectors.toList());
    }
    //Todo : Comment 수동 매핑
    default CommentDto.ResponseComment commentToResponseComment(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto.ResponseComment responseComment = new CommentDto.ResponseComment();

        responseComment.setPostId( comment.getPostId() );
        responseComment.setCommentId( comment.getCommentId() );
        responseComment.setComment( comment.getComment() );
        responseComment.setStar( comment.getStar() );
        responseComment.setAuthor(commentMemberDtoResponse (comment));
        responseComment.setCreatedAt( comment.getCreatedAt() );
        responseComment.setModifiedAt( comment.getModifiedAt() );

        return responseComment;
    }

    //Todo : Comment 리스트로 수동 매핑
    default List<CommentDto.ResponseComment> commentListToResponseCommentList(List<Comment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommentDto.ResponseComment> list1 = new ArrayList<CommentDto.ResponseComment>( list.size() );
        for ( Comment comment : list ) {
            list1.add( commentToResponseComment( comment ) );
        }

        return list1;
    }

    //Todo: Author 수동매핑
    default List<AuthorResponseDto> postMemberDtoResponse (DevPost devPost) {
        List<AuthorResponseDto> author = new ArrayList<>();

        AuthorResponseDto ar = AuthorResponseDto
                .builder()
                .memberId(devPost.getMember().getMemberId())
                .userName(devPost.getMember().getUserName())
                .build();
        author.add(ar);

        return author;
    }

    //Todo: Author 수동매핑
    default List<AuthorResponseDto> commentMemberDtoResponse (Comment comment) {
        List<AuthorResponseDto> author = new ArrayList<>();

        AuthorResponseDto ar = AuthorResponseDto
                .builder()
                .memberId(comment.getMember().getMemberId())
                .userName(comment.getMember().getUserName())
                .build();
        author.add(ar);

        return author;
    }

}
