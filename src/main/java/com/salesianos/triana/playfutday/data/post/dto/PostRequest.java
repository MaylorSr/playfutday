package com.salesianos.triana.playfutday.data.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.triana.playfutday.data.post.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PostRequest {

    @Length(max = 50, message = "{createPostRequest.tag.maxLength}")
    private String tag;

    @Length(max = 200, message = "{createPostRequest.description.maxLength}")
    private String description;

    @NotBlank(message = "{createPostRequest.image.notBlank}")
    private String image;


}
