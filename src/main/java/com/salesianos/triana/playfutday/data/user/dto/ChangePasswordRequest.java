package com.salesianos.triana.playfutday.data.user.dto;

import com.salesianos.triana.playfutday.validation.annotation.FieldsPasswordChange;
import com.salesianos.triana.playfutday.validation.annotation.FieldsValueMatch;
import com.salesianos.triana.playfutday.validation.annotation.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldsValueMatch(field = "newPassword", fieldMatch = "verifyNewPassword",
        message = "{createUserRequest.password.match}")
@FieldsPasswordChange(field = "oldPassword", fieldMatch = "newPassword",
        message = "{createUserRequest.changePassword.too}")
public class ChangePasswordRequest {

    @NotBlank(message = "{createUserRequest.password.blank}")
    private String oldPassword;
    @StrongPassword
    @NotBlank(message = "{createUserRequest.password.blank}")
    private String newPassword;
    private String verifyNewPassword;

}