package com.salesianos.triana.playfutday.data.user.dto;

import com.salesianos.triana.playfutday.validation.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
/*@FieldsValueMatch(field = "password", fieldMatch = "verifyPassword",
        message = "{newUserDto.password.nomatch}")*/
public class UserRequest {

    @NotBlank(message = "{createUserRequest.userName.blank}")
    private String username;
    @Email(message = "{createUserRequest.email.pattern}")
    @NotBlank(message = "{createUserRequest.email.blank}")
    @UniqueEmail(message = "{newUserDto.unique.email}")
    private String email;
    @NotBlank(message = "{createUserRequest.phone.blank}")
    @PhoneStructure()
    @UniquePhone(message = "{createUserRequest.phone.exists}")
    private String phone;
/*    @StrongPassword()
    @NotBlank(message = "{createUserRequest.password.blank}")*/
    private String password;
/*    @StrongPassword()
    @NotEmpty(message = "{newUserDto.unique.password.blank}")*/
    private String verifyPassword;


}
