package com.salesianos.triana.playfutday.data.user.dto;

import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EditInfoUserRequest {

    private String avatar;

    private String biography;

    private String phone;

    private LocalDate birthday;

    static EditInfoUserRequest of(User user) {
        return EditInfoUserRequest.builder()
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .biography(user.getBiography())
                .birthday(user.getBirthday())
                .build();
    }

}
