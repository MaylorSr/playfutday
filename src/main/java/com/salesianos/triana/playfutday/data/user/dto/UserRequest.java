package com.salesianos.triana.playfutday.data.user.dto;

import com.salesianos.triana.playfutday.validation.annotation.*;
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
@FieldsValueMatch(field = "password", fieldMatch = "verifyPassword",
        message = "{createUserRequest.password.match}")
public class UserRequest {

    @NotBlank(message = "{createUserRequest.userName.blank}")
    private String username;
    @Email(message = "{createUserRequest.email.pattern}")
    @NotBlank(message = "{createUserRequest.email.blank}")
    @UniqueEmail(message = "{createUserRequest.unique.email}")
    private String email;
    @NotBlank(message = "{createUserRequest.phone.blank}")
    @PhoneStructure(message = "{createUserRequest.phone.digits}")
    @UniquePhone(message = "{createUserRequest.phone.exists}")
    private String phone;
    @StrongPassword
    @NotBlank(message = "{createUserRequest.password.blank}")
    private String password;
    private String verifyPassword;


}
