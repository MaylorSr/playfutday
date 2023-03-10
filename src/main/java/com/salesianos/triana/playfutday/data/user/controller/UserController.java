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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtProvider jwtProvider;


    @Operation(summary = "Este m??todo crea un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado un nuevo usuario",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "username": "maylor",
                                        "email": "maylor@gmail.com",
                                        "phone": "609835692",
                                        "password":"Maylor15",
                                        "verifyPassword":"Maylor15"
                                    }
                                    """))}),
            @ApiResponse(responseCode = "400",
                    description = "No se han introducidos los datos correctamente. Observa la lista de errores",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Haz intentado realizar la petici??n borrando un atributo del post",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de POST a otra distinta, ejemplo GET",
                    content = @Content),
    })
    @PostMapping("/auth/register")
    @JsonView(viewUser.UserResponse.class)
    public ResponseEntity<UserResponse> createUserWithUserRole(@Valid @RequestBody @Parameter(name = "UserRequest", description = "La informaci??n del usuario que se va a crear") UserRequest createUserRequest) {
        User user = userService.createUserWithUserRole(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromUser(user));
    }


    @Operation(summary = "Este m??todo obtiene todos los datos de los usuarios creados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los datos correctamente de todos los usuarios s??lo para el administrador",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                 "content": [
                                                     {
                                                         "id": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                         "username": "ccliss6",
                                                         "createdAt": "22/02/2023 21:43:35",
                                                         "avatar": "avatar.png",
                                                         "birthday": "06/12/1997",
                                                         "enabled": true,
                                                         "roles": [
                                                             "USER"
                                                         ]
                                                     },
                                                     {
                                                         "id": "6e245baa-4865-45be-99f0-33d548b16887",
                                                         "username": "mdeverell7",
                                                         "createdAt": "22/02/2023 21:43:35",
                                                         "avatar": "avatar.png",
                                                         "birthday": "15/12/1991",
                                                         "enabled": true,
                                                         "roles": [
                                                             "USER"
                                                         ]
                                                     },
                                                     {
                                                         "id": "563d2700-0c3c-4276-a4c4-55f861ebe90a",
                                                         "username": "nchoppen8",
                                                         "createdAt": "22/02/2023 21:43:35",
                                                         "avatar": "avatar.png",
                                                         "birthday": "12/10/1988",
                                                         "enabled": true,
                                                         "myPost": [
                                                             {},
                                                             {},
                                                             {},
                                                             {},
                                                             {},
                                                             {}
                                                         ],
                                                         "roles": [
                                                             "USER"
                                                         ]
                                                     }
                                                 ],
                                                 "totalPages": 4
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para acceder a esta petici??n",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petici??n que no lo requiere", content = @Content),
            @ApiResponse(responseCode = "404", description = "La lista est?? vac??a", content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content)
    })
    @GetMapping("/user")
    @JsonView(viewUser.UserDetailsByAdmin.class)
    public PageResponse<UserResponse> findAllUsers(@RequestParam(value = "s", defaultValue = "") String s,
                                                   @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return userService.findAll(s, pageable);
    }

    @Operation(summary = "Este m??todo obtiene los posts favoritos del usuario logeado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los posts que el usuario le ha dado a favoritos",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                  "content": [
                                                      {
                                                          "tag": "#MESSI",
                                                          "image": "messi.jpg",
                                                          "author": "hpitt2",
                                                          "idAuthor": "eabbe12c-363e-49ff-8b0f-cad8daca9bf9",
                                                          "authorFile": "avatar.png",
                                                          "countLikes": 1
                                                      }
                                                  ],
                                                  "totalPages": 1
                                              }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content),
            @ApiResponse(responseCode = "404", description = "La lista se encuentra vac??a", content = @Content)
    })
    @GetMapping("/fav")
    @JsonView(viewPost.PostLikeMe.class)
    public PageResponse<PostResponse> findAll(@PageableDefault(size = 5, page = 0) Pageable pageable,
                                              @Parameter(name = "Usuario", description = "Se pasa el token del usuario logeado",
                                                      content = @Content, allowEmptyValue = true)
                                              @AuthenticationPrincipal User user) {
        return userService.findMyFavPost(user, pageable);
    }

    @Operation(summary = "Este m??todo banea o quita el ban a un usuario de la aplicaci??n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "El usuario se ha baneado/desbaneado correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "username": "ccliss6",
                                        "avatar": "avatar.png",
                                        "enabled": false,
                                        "roles": [
                                            "USER"
                                        ]
                                    }
                                    """))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de POST a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para realizar esta petici??n",
                    content = @Content)
    })
    @PostMapping("/banUserByAdmin/{id}")
    @JsonView(viewUser.UserChangeDate.class)
    public ResponseEntity<UserResponse> banUserById(@Parameter(name = "ID", description = "Se debe proporcionar el id del usuario a banear", allowEmptyValue = true)
                                                    @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.banUser(id));
    }

    @Operation(summary = "Este m??todo elmina o da de baja a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "El usuario se ha eliminado correctamente"),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de DELETE a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para realizar esta petici??n",
                    content = @Content)
    })
    @DeleteMapping("/user/{idU}")
    public ResponseEntity<?> deleteUser(@Parameter(name = "idU", description = "Id del usuario a eliminar")
                                        @PathVariable UUID idU, @Parameter(name = "Usuario",
            description = "Se debe proporcionar el token del usuario administrador", allowEmptyValue = true, content = @Content) @AuthenticationPrincipal User user) {
        return userService.deleteUser(idU, user);
    }

    @Operation(summary = "Este m??todo a??ade o elmina el rol de ADMIN a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Al usuario se le ha a??adido o quitado el rol de ADMIN",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {
                                                "username": "bmacalester1",
                                                "avatar": "avatar.png",
                                                "enabled": true,
                                                "roles": [
                                                    "ADMIN",
                                                    "USER"
                                                ]
                                            }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de Post a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para realizar esta petici??n",
                    content = @Content)
    })
    @PostMapping("/changeRole/{id}")
    @JsonView(viewUser.UserChangeDate.class)
    public ResponseEntity<UserResponse> addRoleAdminToUser(@Parameter(name = "id",
            description = "Se debe ingresar el ID del usuario a cambiar el rol")
                                                           @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addAdminRoleToUser(id));
    }


    @Operation(summary = "Este sirve para logear a un usuario ya creado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "El usuario se ha logeado correctamente y devuelto sus datos",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                            {
                                                 "id": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                 "username": "bmacalester1",
                                                 "email": "bmacalester1@hotmail.com",
                                                 "avatar": "avatar.png",
                                                 "phone": "3011096944",
                                                 "birthday": "02/04/2002",
                                                 "myPost": [
                                                     {
                                                         "id": 3,
                                                         "tag": "#CR7",
                                                         "image": "cr7.jpg",
                                                         "uploadDate": "22/02/2023",
                                                         "author": "bmacalester1",
                                                         "idAuthor": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                         "authorFile": "avatar.png",
                                                         "countLikes": 0,
                                                         "commentaries": [
                                                             {
                                                                 "message": "engineer rich schemas",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "22/02/2023"
                                                             },
                                                             {
                                                                 "message": "monetize best-of-breed eyeballs",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "22/02/2023"
                                                             },
                                                             {
                                                                 "message": "strategize cross-media deliverables",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "22/02/2023"
                                                             }
                                                         ]
                                                     }
                                                 ],
                                                 "roles": [
                                                     "ADMIN",
                                                     "USER"
                                                 ],
                                                 "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkODgyNTc1OC1kMDJhLTRiY2MtODE0Ni05NWZiNmZhM2RlZDciLCJpYXQiOjE2NzcxMDQ2MDYsImV4cCI6MTY3NzcwOTQwNn0.rp4d6AUuMwAZC8gomYwxrEufKr_-_liNuciUo1foFlqcPwBwlHRTp3CNO7ilZtXAEpgK8UuXuqHvFpRCN7Y8_A"
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado u autenticado",
                    content = @Content)
    })

    @PostMapping("/auth/login")
    @JsonView(viewUser.UserInfo.class)
    public ResponseEntity<UserResponse> login(@Parameter(name = "Usuario",
            description = "Se debe proporcionar el usuario y contrase??a respectivamente para poder logearse", allowEmptyValue = true, content = @Content)
                                              @RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();
        UserResponse userP = UserResponse.fromUser(user);
        userP.setToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(userP);
    }


    @Operation(summary = "Este m??todo lo que hace es cambiar tu contrase??a")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "{Se ha cambiado la contrase??a con ??xito}",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ChangePasswordRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "username": "wbeetham0",
                                                  "avatar": "avatar.png",
                                                  "enabled": true,
                                                  "roles": [
                                                      "ADMIN",
                                                      "USER"
                                                  ]
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Estas otorgando datos err??neos",
                    content = @Content)
    })
    @PutMapping("/user/changePassword")
    @JsonView(viewUser.UserChangeDate.class)
    public UserResponse changePassword(@Parameter(name = "ChangePasswordRequest",
            description = "Se debe proporcionar la contrase??a antigua y las dos nuevas respectivamente", content = @Content, allowEmptyValue = true)
                                       @Valid @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal User user) {
        return userService.editPassword(user, changePasswordRequest);
    }

    @Operation(summary = "Este m??todo lo que hace es cambiar tu avatar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha cambiado el avatar con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditInfoUserRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "avatar": "5842fe0ea6515b1e0ad75b3c.png"
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "No has proporcionado la imagen nueva",
                    content = @Content)
    })
    @PostMapping("/edit/avatar")
    @JsonView(viewUser.editProfile.class)
    public ResponseEntity<EditInfoUserRequest> editProfile(@Parameter(name = "Imagen del nuevo avatar", description = "Se debe proporcionar una im??gen para el nuevo avatar")
                                                           @RequestPart("image") MultipartFile image, @AuthenticationPrincipal User user) throws StorageException {

        EditInfoUserRequest newPost = userService.editProfileAvatar(user, image);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(newPost);
    }

    @Operation(summary = "Este m??todo lo que hace es editar tu biograf??a")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha cambiado la biograf??a con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditInfoUserRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "descripcion":"este es mi nueva descripcion"
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Estas otorgando datos err??neos",
                    content = @Content)
    })
    @PutMapping("/edit/bio")
    @JsonView(viewUser.editProfile.class)
    public EditInfoUserRequest editProfileBio(@Parameter(name = "usuario logeado", description = "Se debe proporcionar el token del usuario logeado")
                                              @AuthenticationPrincipal User user, @Parameter(name = "Cuerpo de la petici??n", description = "Se debe proporcionar el cuerpo con su respectivo nuevo valor para la descripci??n")
                                              @RequestBody EditInfoUserRequest request) {
        return userService.editProfileBio(user, request);
    }


    @Operation(summary = "Este m??todo lo que hace es editar tu biograf??a")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha cambiado la biograf??a con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "phone":"609835694"
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Estas otorgando datos err??neos",
                    content = @Content)
    })
    @PutMapping("/edit/phone")
    @JsonView(viewUser.editProfile.class)
    public EditPhoneUserRequest editPhone(@Parameter(name = "usuario logeado", description = "Se debe proporcionar el token del usuario logeado")
                                          @AuthenticationPrincipal User user,
                                          @Parameter(name = "Cuerpo de la petici??n", description = "Se debe proporcionar el n??mero de tel??fono nuevo")
                                          @Valid @RequestBody EditPhoneUserRequest request) {
        return userService.editProfilePhone(user, request);
    }


    @Operation(summary = "Este m??todo lo que hace es editar tu fecha de cumplea??os")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha cambiado la fecha de cumplea??os con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditInfoUserRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                  "birthday":"17/12/2001"
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Estas otorgando datos err??neos",
                    content = @Content)
    })
    @PutMapping("/edit/birthday")
    @JsonView(viewUser.editProfile.class)
    public EditInfoUserRequest editBirthday(@Parameter(name = "Usuario logeado",
            description = "Se debe proporcionar el token del usuario logeado")
                                            @AuthenticationPrincipal User user,
                                            @Parameter(name = "", description = "")
                                            @Valid
                                            @RequestBody EditInfoUserRequest request) {
        return userService.editProfileBirthday(user, request);
    }

    @Operation(summary = "Este m??todo obtiene todos los datos del usuario logeado, id, nombre,posts, comentarios de posts...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los datos correctamente del usurio logeado",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                 "id": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                 "username": "bmacalester1",
                                                 "createdAt": "22/02/2023 19:36:30",
                                                 "email": "bmacalester1@hotmail.com",
                                                 "avatar": "avatar.png",
                                                 "phone": "3011096944",
                                                 "birthday": "02/04/2002",
                                                 "enabled": true,
                                                 "myPost": [
                                                     {
                                                         "id": 3,
                                                         "tag": "#CR7",
                                                         "image": "cr7.jpg",
                                                         "uploadDate": "22/02/2023",
                                                         "author": "bmacalester1",
                                                         "idAuthor": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                         "authorFile": "avatar.png",
                                                         "countLikes": 0,
                                                         "commentaries": [
                                                             {
                                                                 "id": 46,
                                                                 "message": "engineer rich schemas",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "22/02/2023"
                                                             }
                                                         ]
                                                     },
                                                     {
                                                         "id": 4,
                                                         "tag": "#CR7",
                                                         "image": "cr7.jpg",
                                                         "uploadDate": "22/02/2023",
                                                         "author": "bmacalester1",
                                                         "idAuthor": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                         "authorFile": "avatar.png",
                                                         "countLikes": 0,
                                                         "commentaries": [
                                                             {
                                                                 "id": 50,
                                                                 "message": "engage 24/7 users",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "22/02/2023"
                                                             }
                                                         ]
                                                     }
                                                 ],
                                                 "roles": [
                                                     "USER"
                                                 ],
                                                 "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.
                                                 eyJzdWIiOiJkODgyNTc1OC1kMDJhLTRiY2MtODE0Ni05NWZiNmZhM2RlZDciLCJpYXQiOjE2NzcwOTEwMDgsImV4cCI6MTY3NzY5NTgwOH0.
                                                 XJofqqY6kDfku9wWjK4r4Xfa1wN8QdTsF8_fXn6tDxOlNoCrZueh_6xVry1vybjmUYDvbrdNmHPc6-X_-PDK8g"
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "403",
                    description = "No puedes acceder a esta petici??n",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petici??n de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petici??n", content = @Content)
    })
    @GetMapping("/me")
    public UserResponse getMyProfile(@Parameter(description = "Se pasa el token del usuario logeado", name = "user", required = true, content = @Content)
                                     @AuthenticationPrincipal User user) {
        if (user == null) {
            throw new AccessDeniedException("");
        }
        String token = jwtProvider.generateToken(user);
        Optional<User> u = userService.addPostToUser(user.getUsername());
        UserResponse userResponse = UserResponse.fromUser(u.get());
        userResponse.setToken(token);
        return userResponse;
    }


    @Operation(summary = "Este m??todo trae el perfil con los datos de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha obtenido los datos correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EditInfoUserRequest.class),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": "51057cde-9852-4cd5-be5e-091979495656",
                                                    "username": "wbeetham0",
                                                    "createdAt": "23/02/2023 19:47:32",
                                                    "email": "wbeetham0@gmail.com",
                                                    "avatar": "avatar.png",
                                                    "biography": "Hi i am a new in that!",
                                                    "phone": "3908006159",
                                                    "birthday": "29/12/2004",
                                                    "enabled": true,
                                                    "myPost": [
                                                        {
                                                            "id": 1,
                                                            "tag": "#CR7",
                                                            "image": "cr7.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0,
                                                            "commentaries": [
                                                                {
                                                                    "id": 41,
                                                                    "message": "innovate intuitive models",
                                                                    "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 42,
                                                                    "message": "productize magnetic e-markets",
                                                                    "authorName": "9905d7cf-66c1-40d5-a1de-3c1de754b030",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 43,
                                                                    "message": "cultivate dot-com infomediaries",
                                                                    "authorName": "e814eaf6-64b2-423c-a94e-82c5a09da4dd",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 44,
                                                                    "message": "repurpose visionary action-items",
                                                                    "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 49,
                                                                    "message": "embrace clicks-and-mortar e-services",
                                                                    "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 63,
                                                                    "message": "unleash end-to-end experiences",
                                                                    "authorName": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 65,
                                                                    "message": "recontextualize open-source initiatives",
                                                                    "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                    "uploadCommentary": "23/02/2023"
                                                                }
                                                            ]
                                                        },
                                                        {
                                                            "id": 2,
                                                            "tag": "#CR7",
                                                            "image": "cr7.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0,
                                                            "commentaries": [
                                                                {
                                                                    "id": 45,
                                                                    "message": "morph web-enabled initiatives",
                                                                    "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                    "uploadCommentary": "23/02/2023"
                                                                },
                                                                {
                                                                    "id": 70,
                                                                    "message": "harness transparent platforms",
                                                                    "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                    "uploadCommentary": "23/02/2023"
                                                                }
                                                            ]
                                                        },
                                                        {
                                                            "id": 11,
                                                            "tag": "#MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 12,
                                                            "tag": "#MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 13,
                                                            "tag": "#MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 21,
                                                            "tag": "##NEYMAR",
                                                            "description": "Neymar is the best player",
                                                            "image": "neymar.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 22,
                                                            "tag": "##CR7",
                                                            "image": "cr7.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 31,
                                                            "tag": "##MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 32,
                                                            "tag": "##MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        },
                                                        {
                                                            "id": 33,
                                                            "tag": "##MESSI",
                                                            "image": "messi.jpg",
                                                            "uploadDate": "23/02/2023",
                                                            "author": "wbeetham0",
                                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                                            "authorFile": "avatar.png",
                                                            "countLikes": 0
                                                        }
                                                    ],
                                                    "roles": [
                                                        "ADMIN",
                                                        "USER"
                                                    ],
                                                    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI1MTA1N2NkZS05ODUyLTRjZDUtYmU1ZS0wOTE5Nzk0OTU2NTYiLCJpYXQiOjE2NzcxNzgwNjAsImV4cCI6MTY3Nzc4Mjg2MH0.5sIXNb0iYbzJveejC_3tFEafTK9vHiUOMF5Bnt5JYDXXNhyLs9aideyfvv8SltTljkHSdTCQ0Zl-wrwt1ww79Q"
                                                }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Estas otorgando datos err??neos",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content)
    })
    @JsonView(viewUser.UserInfo.class)
    @GetMapping("/info/user/{id}")
    public UserResponse getInfoUser(@Parameter(name = "ID", description = "Id del usuario a tener info")
                                    @PathVariable UUID id,
                                    @AuthenticationPrincipal User user) {
        return userService.findByIdInfoUser(id);
    }


}