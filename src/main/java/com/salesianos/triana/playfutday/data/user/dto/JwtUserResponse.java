package com.salesianos.triana.playfutday.data.user.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtUserResponse extends UserResponse {

    @JsonView(viewUser.UserInfo.class)
    private String token;

    @JsonView(viewUser.UserInfo.class)
    private String refreshToken;


    public JwtUserResponse(UserResponse userResponse) {
        id = userResponse.getId();
        username = userResponse.getUsername();
        createdAt = userResponse.getCreatedAt();
        username = (userResponse.getUsername());
        email = (userResponse.getEmail());
        avatar = (userResponse.getAvatar());
        biography = (userResponse.getBiography());
        birthday = (userResponse.getBirthday());
        phone = (userResponse.getPhone());
        createdAt = (userResponse.getCreatedAt());
        enabled = (userResponse.isEnabled());
        myPost = (userResponse.getMyPost() == null ? null : userResponse.getMyPost());
        roles = (userResponse.getRoles());
    }

    public static JwtUserResponse of(User user, String token) {

        JwtUserResponse result = new JwtUserResponse(UserResponse.fromUser(user));
        result.setToken(token);

        return result;
    }

}
