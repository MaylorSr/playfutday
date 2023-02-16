package com.salesianos.triana.playfutday.data.post.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.service.PostService;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.search.page.PageResponse;
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

    /**
     * Muestra todos los post de los usuarios
     *
     * @return
     */
    @GetMapping("/")
    @JsonView({viewPost.PostResponse.class})
    public PageResponse<PostResponse> findAllPost(
            @RequestParam(value = "s", defaultValue = "") String s,
            @PageableDefault(size = 5, page = 0) Pageable pageable) {
        return postService.findAllPost(s, pageable);
    }

    /**
     * Obtiene los post del usuario que esta logeado
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    @JsonView(viewPost.PostResponse.class)
    public PageResponse<PostResponse> getAll(@PageableDefault(size = 3, page = 0) Pageable pageable, @AuthenticationPrincipal User user) {
        return postService.findAllPostByUserName(user.getUsername(), pageable);
    }

    /**
     * Obtener lista de post por el nombre del usuario
     */

    @GetMapping("/user/{username}")
    @JsonView({viewPost.PostResponse.class})
    public PageResponse<PostResponse> findPostOfUser(@PageableDefault(size = 3, page = 0) Pageable pageable, @PathVariable String username) {
        return postService.findAllPostByUserName(username, pageable);
    }


    /**
     * Crea un post
     *
     * @param postRequest
     * @param user
     * @return
     */
    @PostMapping("/")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> savePostByUser(@Valid @RequestPart("image") MultipartFile image,
                                                       @RequestPart("post") PostRequest postRequest,
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

    /**
     * Añade un me gusta del usuario logeado
     *
     * @param user
     * @param id
     * @return
     */
    @PostMapping("/like/{id}")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> saveLikeByUser(@AuthenticationPrincipal User user, @PathVariable Long id) {
        PostResponse postWithLike = postService.giveLikeByUser(id, user);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postWithLike.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(postWithLike);
    }

    /**
     * Añade un comentario del usuario logeado
     *
     * @param request
     * @param id
     * @param user
     * @return
     */
    @PostMapping("/commentary/{id}")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> saveCommentaryByUser(@Valid @RequestBody CommentaryRequest request, @PathVariable Long id, @AuthenticationPrincipal User user) {
        PostResponse newCommentaryInPost = postService.giveCommentByUser(id, user, request);
        URI createdURI = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newCommentaryInPost.getId()).toUri();
        return ResponseEntity
                .created(createdURI)
                .body(newCommentaryInPost);
    }

    /**
     * Eliminar un post del usuario
     */

    @DeleteMapping("/user/{id}/user/{idU}")
    public ResponseEntity<?> deletePostOfUser(@PathVariable Long id, @PathVariable UUID idU, @AuthenticationPrincipal User user) {
        return postService.deletePostByUser(id, idU, user);
    }

    /**
     * Eliminar un comentario desde el punto de vista de un administrador
     */
    @DeleteMapping("/delete/commentary/{id}")
    public ResponseEntity<?> deleteCommentaryByUserForAdmin(@PathVariable Long id) {
        return postService.deleteCommentary(id);
    }



}
