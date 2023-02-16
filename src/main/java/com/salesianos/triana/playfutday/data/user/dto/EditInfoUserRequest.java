package com.salesianos.triana.playfutday.data.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.validation.annotation.FieldsValueMatch;
import com.salesianos.triana.playfutday.validation.annotation.PhoneStructure;
import com.salesianos.triana.playfutday.validation.annotation.UniquePhone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EditInfoUserRequest {
    @JsonView(viewUser.editProfile.class)
    private String avatar;
    @JsonView(viewUser.editProfile.class)
    @Length(max = 200, message = "{editRequest.biography.limit}")
    private String biography;

    @NotBlank(message = "{createUserRequest.phone.blank}")
    @PhoneStructure(message = "{createUserRequest.phone.digits}")
    @UniquePhone(message = "{createUserRequest.phone.exists}")
    @JsonView(viewUser.editProfile.class)
    private String phone;

    @JsonView(viewUser.editProfile.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;


}
