package com.salesianos.triana.playfutday.data.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryResponse;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.user.dto.UserResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostResponse {

    @JsonView({viewPost.PostAdmin.class, viewUser.UserDetailsByAdmin.class})
    protected Long id;
    @JsonView({viewPost.class, viewUser.class})
    protected String tag;
    @JsonView({viewPost.class, viewUser.class})
    protected String description;
    @JsonView({viewPost.class, viewUser.class})
    protected String image;
    @JsonView({viewPost.class, viewUser.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    protected LocalDateTime uploadDate;
    @JsonView({viewPost.class, viewUser.class})
    protected String author;
    @JsonView({viewPost.class, viewUser.class})
    protected List<UserResponse> likesByAuthor;
    @JsonView({viewPost.class, viewUser.class})
    protected List<CommentaryResponse> commentaries;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .tag(post.getTag())
                .description(post.getDescription())
                .image(post.getImage())
                .uploadDate(post.getUploadDate())
                .author(post.getAuthor().getUsername())
                .likesByAuthor(post.getLikes().stream().map(UserResponse::fromUser).toList())
                .commentaries(post.getCommentaries().stream().map(CommentaryResponse::of).toList())
                .build();
    }

}
