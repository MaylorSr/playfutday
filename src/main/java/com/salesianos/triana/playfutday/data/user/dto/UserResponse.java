package com.salesianos.triana.playfutday.data.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    @JsonView({viewUser.UserDetailsByAdmin.class, viewPost.PostAdmin.class})
    protected UUID id;
    @JsonView({viewUser.class, viewPost.class})
    protected String username;
    @JsonView({viewUser.UserDetailsByAdmin.class, viewPost.PostAdmin.class})
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();
    @JsonView({viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String email;
    @JsonView({viewUser.class, viewPost.class})
    protected String avatar;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewPost.PostResponse.class})
    protected String biography;
    @JsonView({viewUser.UserInfo.class})
    protected int phone;
    @JsonView({viewUser.UserInfo.class, viewUser.UserResponse.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDate birthday;
    @JsonView(viewUser.UserDetailsByAdmin.class)
    protected boolean enabled;
    @JsonView({viewUser.class, viewPost.class})
    protected List<PostResponse> myPost;


    /**
     * ROLES DE USUARIO QUEDA
     */
    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .biography(user.getBiography())
                .birthday(user.getBirthday())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .enabled(user.isEnabled())
                .myPost(user.getMyPost().stream().map(PostResponse::of).toList())
                .build();
    }
}
