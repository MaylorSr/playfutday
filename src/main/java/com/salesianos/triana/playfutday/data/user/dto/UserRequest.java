package com.salesianos.triana.playfutday.data.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "{createUserRequest.userName.blank}")
    private String username;

    @Email(message = "{createUserRequest.email.blank}")
    private String email;
    @NotBlank(message = "{createUserRequest.phone.blank}")
    private int phone;

    @NotBlank(message = "{createUserRequest.password.blank}")
    private String password;

    private String verifyPassword;


}
