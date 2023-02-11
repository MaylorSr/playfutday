package com.salesianos.triana.playfutday.data.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse {

    @JsonView({viewUser.UserDetailsByAdmin.class})
    protected UUID id;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String username;
    @JsonView({viewUser.UserDetailsByAdmin.class})
    @Builder.Default
    protected LocalDateTime createdAt = LocalDateTime.now();
    @JsonView({viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String email;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String avatar;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected String biography;
    @JsonView({viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected int phone;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    protected LocalDate birthday;
    @JsonView({viewUser.UserDetailsByAdmin.class})
    protected boolean enabled;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected List<PostResponse> myPost;
    @JsonView({viewUser.UserResponse.class, viewUser.UserInfo.class, viewUser.UserDetailsByAdmin.class})
    protected List<String> roles;

    /**
     * ROLES DE USUARIO QUEDA
     */
    public static UserResponse fromUser(User user) {
        Set<UserRole> roles = user.getRoles();
        EnumSet<UserRole> userRolesEnumSet = EnumSet.noneOf(UserRole.class);

        if (!roles.isEmpty()) {
            userRolesEnumSet = roles.stream()
                    .map(r -> r.name().toUpperCase())
                    .filter(UserRole::contains)
                    .map(r -> UserRole.valueOf(r))
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(UserRole.class)));
        }

        return UserResponse
                .builder()
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
                .roles(userRolesEnumSet.stream().map(Enum::name).collect(Collectors.toList()))
                .build();
    }
}
