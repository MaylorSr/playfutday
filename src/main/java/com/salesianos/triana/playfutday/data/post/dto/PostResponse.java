package com.salesianos.triana.playfutday.data.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryResponse;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.user.dto.UserResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostResponse {

    private static PostRepository postRepository;

    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class})
    protected Long id;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected String tag;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected String description;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected String image;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDateTime uploadDate;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected String author;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected String authorFile;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class})
    protected List<String> likesByAuthor;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewPost.PostLikeMe.class})
    protected int countLikes;
    @JsonView({viewPost.PostAdmin.class, viewPost.PostResponse.class, viewUser.UserInfo.class})
    protected List<CommentaryResponse> commentaries;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .tag(post.getTag())
                .description(post.getDescription())
                .image(post.getImage())
                .uploadDate(post.getUploadDate())
                .author(post.getAuthor().getUsername())
                .authorFile(post.getAuthor().getAvatar())
                .likesByAuthor(post.getLikes() == null ? null : post.getLikes().stream().map(User::getUsername).toList())
                .countLikes(post.getLikes() == null ? 0 : post.getLikes().size())
                .commentaries(post.getCommentaries() == null ? null : post.getCommentaries().stream().map(CommentaryResponse::of).toList())
                .build();
    }

}
