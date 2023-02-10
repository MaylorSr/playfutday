package com.salesianos.triana.playfutday.data.commentary.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.interfaces.commentary.viewCommentary;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentaryResponse {
    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.class})
    protected String message;

    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.class})
    protected String authorName;

    @JsonView({viewCommentary.CommentaryResponse.class, viewPost.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDate uploadCommentary;

    public static CommentaryResponse of(Commentary commentary) {
        return CommentaryResponse.builder()
                .message(commentary.getMessage())
                .authorName(commentary.getUser().getUsername())
                .uploadCommentary(commentary.getUpdateCommentary())
                .build();
    }


}
