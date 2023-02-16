package com.salesianos.triana.playfutday.data.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.validation.annotation.PhoneStructure;
import com.salesianos.triana.playfutday.validation.annotation.UniquePhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EditPhoneUserRequest {
    @NotBlank(message = "{createUserRequest.phone.blank}")
    @PhoneStructure(message = "{createUserRequest.phone.digits}")
    @UniquePhone(message = "{createUserRequest.phone.exists}")
    @JsonView(viewUser.editProfile.class)
    private String phone;
}
