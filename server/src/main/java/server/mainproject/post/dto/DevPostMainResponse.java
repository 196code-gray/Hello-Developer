package server.mainproject.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import server.mainproject.member.dto.AuthorResponseDto;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class DevPostMainResponse {
    private Long postId;
    private String title;
    private String sourceURL;
    private int star;
    private Double starAvg;
    private int recommend;
    private String sourceMedia;
    private List<AuthorResponseDto> author;
    private List<Post_TagResponseDto> tags;
    private String thumbnailImage;
    private String sorta;
}
