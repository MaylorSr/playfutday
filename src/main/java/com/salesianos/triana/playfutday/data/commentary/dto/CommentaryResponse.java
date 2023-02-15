package com.salesianos.triana.playfutday.data.commentary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.interfaces.commentary.viewCommentary;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentaryResponse {

    @JsonView({viewCommentary.CommentaryResponse.class, viewUser.UserDetailsByAdmin.class, viewUser.UserDetailsByAdmin.class})
    private Long id;

    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String message;

    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String authorName;

    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.PostResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDate uploadCommentary;

    public static CommentaryResponse of(Commentary commentary) {
        return CommentaryResponse.builder()
                .id(commentary.getId())
                .message(commentary.getMessage())
                .authorName(commentary.getAuthor())
                .uploadCommentary(commentary.getUpdateCommentary())
                .build();
    }


}
