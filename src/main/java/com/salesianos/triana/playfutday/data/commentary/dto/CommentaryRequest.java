package com.salesianos.triana.playfutday.data.commentary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CommentaryRequest {
    @Length(max = 80, message = "{commentaryRequest.message.maxLength}")
    private String message;

}
