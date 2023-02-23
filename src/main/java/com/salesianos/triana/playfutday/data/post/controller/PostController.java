package com.salesianos.triana.playfutday.data.post.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.service.PostService;
import com.salesianos.triana.playfutday.data.user.dto.UserResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.search.page.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "Este método obtiene todos los posts de los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los datos correctamente de todos posts de los usuarios",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                 "content": [
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
                                                                 "message": "innovate intuitive models",
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
                                                                 "message": "morph web-enabled initiatives",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "23/02/2023"
                                                             },
                                                             {
                                                                 "message": "harness transparent platforms",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "23/02/2023"
                                                             }
                                                         ]
                                                     },
                                                     {
                                                         "id": 3,
                                                         "tag": "#CR7",
                                                         "image": "cr7.jpg",
                                                         "uploadDate": "23/02/2023",
                                                         "author": "bmacalester1",
                                                         "idAuthor": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                                         "authorFile": "avatar.png",
                                                         "countLikes": 0,
                                                         "commentaries": [
                                                             {
                                                                 "message": "engineer rich schemas",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "23/02/2023"
                                                             },
                                                             {
                                                                 "message": "monetize best-of-breed eyeballs",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "23/02/2023"
                                                             },
                                                             {
                                                                 "message": "strategize cross-media deliverables",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "23/02/2023"
                                                             }
                                                         ]
                                                     }
                                                 ],
                                                 "totalPages": 8
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petición que no lo requiere", content = @Content),
            @ApiResponse(responseCode = "404", description = "La lista está vacía", content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content)
    })
    @GetMapping("/")
    @JsonView({viewPost.PostResponse.class})
    public PageResponse<PostResponse> findAllPost(
            @RequestParam(value = "s", defaultValue = "") String s,
            @PageableDefault(size = 5, page = 0) Pageable pageable) {
        return postService.findAllPost(s, pageable);
    }


    @Operation(summary = "Este método obtiene todos mis posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los datos correctamente de todos mis posts",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                 "content": [
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
                                                                 "message": "innovate intuitive models",
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
                                                                 "message": "morph web-enabled initiatives",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "23/02/2023"
                                                             },
                                                             {
                                                                 "message": "harness transparent platforms",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "23/02/2023"
                                                             }
                                                         ]
                                                     }
                                                 ],
                                                 "totalPages": 8
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petición que no lo requiere", content = @Content),
            @ApiResponse(responseCode = "404", description = "La lista está vacía", content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content),
    })
    @GetMapping("/user")
    @JsonView(viewPost.PostResponse.class)
    public PageResponse<PostResponse> getAll(@PageableDefault(size = 3, page = 0) Pageable pageable,
                                             @Parameter(name = "Usuario", description = "Se debe proporcionar " +
                                                     "el token del usuario logeado para ver sus posts")
                                             @AuthenticationPrincipal User user) {
        return postService.findAllPostByUserName(user.getUsername(), pageable);
    }


    @Operation(summary = "Este método obtiene todos los posts de un usuario concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Sa han devuelto los datos correctamente de todos los posts del usuario en cuestión",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)),
                            examples = {@ExampleObject(
                                    value = """
                                            [
                                             {
                                                 "content": [
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
                                                                 "message": "innovate intuitive models",
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
                                                                 "message": "morph web-enabled initiatives",
                                                                 "authorName": "abb9feac-f0ec-45cf-91a9-5d21c789da2d",
                                                                 "uploadCommentary": "23/02/2023"
                                                             },
                                                             {
                                                                 "message": "harness transparent platforms",
                                                                 "authorName": "cc1692e1-f031-4675-a0b1-96aeeada21aa",
                                                                 "uploadCommentary": "23/02/2023"
                                                             }
                                                         ]
                                                     }
                                                 ],
                                                 "totalPages": 8
                                             }
                                            ]
                                             """
                            )}
                    )}),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de GET a otra distinta, ejemplo POST",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Estas intentando pasar cuerpo a la petición que no lo requiere", content = @Content),
            @ApiResponse(responseCode = "404", description = "La lista está vacía o el usuario no existe", content = @Content),
            @ApiResponse(responseCode = "401", description = "No estas logeado", content = @Content),
    })
    @GetMapping("/user/{username}")
    @JsonView({viewPost.PostResponse.class})
    public PageResponse<PostResponse> findPostOfUser(@PageableDefault(size = 3, page = 0) Pageable pageable, @Parameter(name = "username",
            description = "Se debe proporcionar el username del usuario a buscar los posts") @PathVariable String username) {
        return postService.findAllPostByUserName(username, pageable);
    }


    @Operation(summary = "Este método crea un nuevo post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado correctamente el post",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        {
                                            "id": 1000,
                                            "tag": "#messi",
                                            "description": "Lorem Ipsum Dolor Sit Amet....",
                                            "image": "5842fe0ea6515b1e0ad75b3c_317828.png",
                                            "uploadDate": "23/02/2023",
                                            "author": "wbeetham0",
                                            "idAuthor": "51057cde-9852-4cd5-be5e-091979495656",
                                            "authorFile": "avatar.png",
                                            "countLikes": 0
                                        }
                                    }
                                    """))}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de POST a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "No estas enviando los datos correctamente",
                    content = @Content)
    })
    @PostMapping("/")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> savePostByUser(@Parameter(name = "file", description = "Se debe proporcionar la imagen del post")
                                                       @RequestPart("image") MultipartFile image, @Valid
                                                       @Parameter(name = "POST", description = "Se debe proporcionar el tag y descripción del post")
                                                       @RequestPart("post") PostRequest postRequest,
                                                       @Parameter(name = "Usuario", description = "Se debe proporcionar el token del usuario logeado")
                                                       @AuthenticationPrincipal User user) {
        PostResponse newPost = postService.createPostByUser(postRequest, image, user);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(newPost);
    }

    @Operation(summary = "Este método da un like o lo quita a un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha dado/quitado el like al post",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        {
                                            "id": 15,
                                            "tag": "#MESSI",
                                            "image": "messi.jpg",
                                            "uploadDate": "23/02/2023",
                                            "author": "hpitt2",
                                            "idAuthor": "eabbe12c-363e-49ff-8b0f-cad8daca9bf9",
                                            "authorFile": "avatar.png",
                                            "likesByAuthor": [
                                                "wbeetham0"
                                            ],
                                            "countLikes": 1
                                        }
                                    }
                                    """))}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de POST a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "El id del post no existe",
                    content = @Content)
    })

    @PostMapping("/like/{id}")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> saveLikeByUser(@Parameter(name = "Usuario",
            description = "Se debe proporcionar el token del usuario logeado")
                                                       @AuthenticationPrincipal User user,
                                                       @Parameter(name = "ID", description = "Se debe proporcionar el id del post a dar like") @PathVariable Long id) {
        PostResponse postWithLike = postService.giveLikeByUser(id, user);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postWithLike.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(postWithLike);
    }

    @Operation(summary = "Este método agreaga un comentario a un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha agregado correctamente el comentario al post",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        {
                                            "id": 20,
                                            "tag": "#MESSI",
                                            "image": "messi.jpg",
                                            "uploadDate": "23/02/2023",
                                            "author": "bmacalester1",
                                            "idAuthor": "d8825758-d02a-4bcc-8146-95fb6fa3ded7",
                                            "authorFile": "avatar.png",
                                            "countLikes": 0,
                                            "commentaries": [
                                                {
                                                    "message": "This is my first commentary here aaa!",
                                                    "authorName": "wbeetham0",
                                                    "uploadCommentary": "23/02/2023"
                                                }
                                            ]
                                        }
                                    }
                                    """))}),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de POST a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "El id del post no existe",
                    content = @Content)
    })
    @PostMapping("/commentary/{id}")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> saveCommentaryByUser(
            @Parameter(name = "CommentaryRequest", description = "Se debe de proporcionar el mensaje")
            @Valid @RequestBody CommentaryRequest request,
            @Parameter(name = "ID", description = "Se debe porporcionar el id del post") @PathVariable Long id,
            @Parameter(name = "Usuario", description = "Se debe proporcionar el token del usuario logeado") @AuthenticationPrincipal User user) {
        PostResponse newCommentaryInPost = postService.giveCommentByUser(id, user, request);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCommentaryInPost.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(newCommentaryInPost);
    }

    @Operation(summary = "Este método elmina un post de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "El post se ha eliminado correctamente"),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el usuario u post",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de DELETE a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para realizar esta petición",
                    content = @Content)
    })
    @DeleteMapping("/user/{id}/user/{idU}")
    public ResponseEntity<?> deletePostOfUser(
            @Parameter(name = "ID", description = "Se debe proporcionar el id del post")
            @PathVariable Long id,
            @Parameter(name = "UsuarioID", description = "Se debe proporcionar el id del usuario")
            @PathVariable UUID idU,
            @Parameter(name = "Usuario", description = "Se debe proporcionar el token del usuario logeado")
            @AuthenticationPrincipal User user) {
        return postService.deletePostByUser(id, idU, user);
    }

    @Operation(summary = "Este método elmina un comentario de un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "El post se ha eliminado correctamente"),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el comentario",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "No estas logeado",
                    content = @Content),
            @ApiResponse(responseCode = "405",
                    description = "Estas intentado hacer la petición de DELETE a otra distinta, ejemplo GET",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "No tienes permisos para realizar esta petición",
                    content = @Content)
    })
    @DeleteMapping("/delete/commentary/{id}")
    public ResponseEntity<?> deleteCommentaryByUserForAdmin(@PathVariable Long id) {
        return postService.deleteCommentary(id);
    }


}
