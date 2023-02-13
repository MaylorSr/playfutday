package com.salesianos.triana.playfutday.data.post.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.interfaces.post.viewPost;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.post.service.PostService;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final UserService userService;
    private final PostService postService;

    private final PostRepository repo;

    /**
     * Muestra todos los post de los usuarios
     *
     * @return
     */
    @GetMapping("/")
    @JsonView({viewPost.PostResponse.class})
    public List<PostResponse> findAllPost() {
        return postService.findAllPost();
    }

    /**
     * Obtiene los post del usuario que esta logeado
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<List<PostResponse>> getAll(@AuthenticationPrincipal User user) {
        return buildResponseOfAList(repo.findByAuthor(user));
    }

    /**
     * Obtener lista de post por el nombre del usuario
     */

    @GetMapping("/user/{username}")
    @JsonView({viewPost.PostResponse.class})
    public ResponseEntity<List<PostResponse>> findPostOfUser(@PathVariable String username) {
        return buildResponseOfAList(repo.findAllPostOfOneUserByUserName(username));
    }

    private ResponseEntity<List<PostResponse>> buildResponseOfAList(List<Post> list) {
        if (list.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(list.stream().map(PostResponse::of).toList());
    }

    /**
     * Crea un post
     *
     * @param postRequest
     * @param user
     * @return
     */
    @PostMapping("/")
    /**
     * @PreUthenticate(isAuthetica) eso hace que tenga que estar autenticado para poder hacer la peticion
     */
    @JsonView(viewPost.PostResponse.class)
    public ResponseEntity<PostResponse> savePostByUser(@RequestBody PostRequest postRequest, @AuthenticationPrincipal User user) {
        PostResponse newPost = postService.createPostByUser(postRequest, user);
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
    public ResponseEntity<PostResponse> saveCommentaryByUser(@RequestBody CommentaryRequest request, @PathVariable Long id, @AuthenticationPrincipal User user) {
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
        if (postService.deleteCommentary(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }


}
