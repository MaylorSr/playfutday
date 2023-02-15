package com.salesianos.triana.playfutday.data.user.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.interfaces.user.viewUser;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.user.dto.*;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.service.UserService;
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
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.PreRemove;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;


    @JsonView(viewUser.UserResponse.class)
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> createUserWithUserRole(@RequestBody UserRequest createUserRequest) {
        User user = userService.createUserWithUserRole(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }

    /**
     * OBTENER TODOS LOS USUARIOS PARA ALVISTA DEL ADMIN
     */
/*
    @JsonView(viewUser.UserDetailsByAdmin.class)
*/
 /*   @GetMapping("/user")
    public List<UserResponse> findallUsers() {
        return userService.findAllUsers();
    }*/
    @GetMapping("/user")
    @JsonView(viewUser.UserDetailsByAdmin.class)
    public PageResponse<UserResponse> findAllUsers(
            @RequestParam(value = "s", defaultValue = "") String s,
            @PageableDefault(size = 3, page = 0) Pageable pageable) {
        return userService.findAll(s, pageable);
    }


    /**
     * put mi perfil
     */


    @GetMapping("/fav")
    @JsonView(viewPost.PostLikeMe.class)
    public PageResponse<PostResponse> findAll(
            @PageableDefault(size = 5, page = 0) Pageable pageable, @AuthenticationPrincipal User user) {
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

    /**
     * Añadir / Quitar rol de administrador a un usuario que lo tenga
     */


    @PostMapping("/auth/register/admin")
    public ResponseEntity<UserResponse> createUserWithAdminRole(@RequestBody UserRequest createUserRequest) {
        User user = userService.createUserWithAdminRole(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }


    @PostMapping("/auth/login")
    @JsonView(viewUser.UserInfo.class)
    public ResponseEntity<JwtUserResponse> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()
                        )
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(JwtUserResponse.of(user, token));
    }


    @PutMapping("/user/changePassword")
    public ResponseEntity<UserResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
                                                       @AuthenticationPrincipal User loggedUser) {

        // Este código es mejorable.
        // La validación de la contraseña nueva se puede hacer con un validador.
        // La gestión de errores se puede hacer con excepciones propias
        try {
            if (userService.passwordMatch(loggedUser, changePasswordRequest.getOldPassword())) {
                Optional<User> modified = userService.editPassword(loggedUser.getId(), changePasswordRequest.getNewPassword());
                if (modified.isPresent())
                    return ResponseEntity.ok(UserResponse.fromUser(modified.get()));
            } else {
                // Lo ideal es que esto se gestionara de forma centralizada
                // Se puede ver cómo hacerlo en la formación sobre Validación con Spring Boot
                // y la formación sobre Gestión de Errores con Spring Boot
                throw new RuntimeException();
            }
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password Data Error");
        }

        return null;
    }

}