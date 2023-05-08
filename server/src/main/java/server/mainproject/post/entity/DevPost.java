package server.mainproject.post.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import server.mainproject.comment.entity.Comment;
import server.mainproject.audit.Auditable;
import server.mainproject.member.entity.Member;
import server.mainproject.tag.Post_Tag;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity @Getter @Setter
@NoArgsConstructor
public class DevPost extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String userName;

    @Column
    private String link;

    @Column
    private int recommend;

    @Column(nullable = false)
    private int star;

    @Column
    private Double starAvg;

//    @Transient
//    @Column
//    private List<String> tags;


    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Recommends> recommends = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "devPost", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private Set<Post_Tag> postTags = new HashSet<>();

}
