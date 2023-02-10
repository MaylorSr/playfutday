package com.salesianos.triana.playfutday.data.commentary.dto;

import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentaryRequest {
    @Length(max = 80, message = "{commentaryRequest.message.maxLength}")
    private String message;

    /**
     * Aprovechamos el mensaje del usuario para no tener que crear uno nuevo aunque sería "conveniente"
     */
    @NotBlank(message = "{createPostRequest.userId.notBlank}")
    private String userId;

    @NotBlank(message = "{createPostRequest.postId.notBlank}")
    private String postId;

}
