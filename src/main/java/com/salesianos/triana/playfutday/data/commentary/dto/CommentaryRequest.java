package com.salesianos.triana.playfutday.data.commentary.dto;

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
    @NotBlank(message = "{commentaryRequest.message.notBlank}")
    @Length(max = 80, message = "{commentaryRequest.message.maxLength}")
    private String message;

}
