package com.salesianos.triana.playfutday.data.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.files.exception.StorageException;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.user.dto.*;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.service.UserService;
import com.salesianos.triana.playfutday.exception.NotPermission;
import com.salesianos.triana.playfutday.search.page.PageResponse;
import com.salesianos.triana.playfutday.security.jwt.access.JwtProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;


    @PostMapping("/auth/register")
    @JsonView(viewUser.UserResponse.class)
    public ResponseEntity<UserResponse> createUserWithUserRole(@Valid @RequestBody UserRequest createUserRequest) {
        User user = userService.createUserWithUserRole(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }

    /**
     * OBTENER TODOS LOS USUARIOS PARA ALVISTA DEL ADMIN
     */

    @GetMapping("/user")
    @JsonView(viewUser.UserDetailsByAdmin.class)
    public PageResponse<UserResponse> findAllUsers(@RequestParam(value = "s", defaultValue = "") String s, @PageableDefault(size = 3, page = 0) Pageable pageable) {
        return userService.findAll(s, pageable);
    }


    /**
     * put mi perfil
     */


    @GetMapping("/fav")
    @JsonView(viewPost.PostLikeMe.class)
    public PageResponse<PostResponse> findAll(@PageableDefault(size = 5, page = 0) Pageable pageable, @AuthenticationPrincipal User user) {
        return userService.findMyFavPost(user, pageable);
    }


    /**
     * Banear a un usuario desde el punto de vista del administrador
     */

    @PostMapping("/banUserByAdmin/{id}")
    @JsonView(viewUser.UserChangeDate.class)
    public ResponseEntity<UserResponse> banUserById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.banUser(id));
    }

    /**
     * ELIMINAR UN USUARIO
     */

    @DeleteMapping("/user/{idU}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID idU, @AuthenticationPrincipal User user) {
        return userService.deleteUser(idU, user);
    }


    @PostMapping("/changeRole/{id}")
    @JsonView(viewUser.UserChangeDate.class)
    public ResponseEntity<UserResponse> addRoleAdminToUser(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAdminRoleToUser(id));
    }

    @PostMapping("/auth/login")
    @JsonView(viewUser.UserInfo.class)
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();
        UserResponse userP = UserResponse.fromUser(user);
        userP.setToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(userP);
    }


    @PutMapping("/user/changePassword")
    @JsonView(viewUser.UserChangeDate.class)
    public UserResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal User user) {
        return userService.editPassword(user, changePasswordRequest);
    }

    @PostMapping("/edit/avatar")
    @JsonView(viewUser.editProfile.class)
    public ResponseEntity<EditInfoUserRequest> editProfile(@RequestPart("image") MultipartFile image, @AuthenticationPrincipal User user) throws StorageException {

        EditInfoUserRequest newPost = userService.editProfileAvatar(user, image);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(newPost);
    }

    @PutMapping("/edit/bio")
    @JsonView(viewUser.editProfile.class)
    public EditInfoUserRequest editProfileBio(@AuthenticationPrincipal User user, @RequestBody EditInfoUserRequest request) {
        return userService.editProfileBio(user, request);
    }

    @PutMapping("/edit/phone")
    @JsonView(viewUser.editProfile.class)
    public EditPhoneUserRequest editPhone(@AuthenticationPrincipal User user, @Valid @RequestBody EditPhoneUserRequest request) {
        return userService.editProfilePhone(user, request);
    }

    @PutMapping("/edit/birthday")
    @JsonView(viewUser.editProfile.class)
    public EditInfoUserRequest editBirthday(@AuthenticationPrincipal User user, @Valid @RequestBody EditInfoUserRequest request) {
        return userService.editProfileBirthday(user, request);
    }

    @GetMapping("/me")

    public UserResponse getMyProfile(@AuthenticationPrincipal User user) {
        String token = jwtProvider.generateToken(user);
        Optional<User> u = userService.addPostToUser(user.getUsername());
        UserResponse userResponse = UserResponse.fromUser(u.get());
        userResponse.setToken(token);
        return userResponse;
    }
}