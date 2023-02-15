package com.salesianos.triana.playfutday.data.post.service;

import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.commentary.repository.CommentaryRepository;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.model.UserRole;
import com.salesianos.triana.playfutday.data.user.repository.UserRepository;
import com.salesianos.triana.playfutday.data.user.service.UserService;
import com.salesianos.triana.playfutday.exception.GlobalEntityListNotFounException;
import com.salesianos.triana.playfutday.exception.GlobalEntityNotFounException;
import com.salesianos.triana.playfutday.search.page.PageResponse;
import com.salesianos.triana.playfutday.search.spec.GenericSpecificationBuilder;
import com.salesianos.triana.playfutday.search.util.SearchCriteria;
import com.salesianos.triana.playfutday.search.util.SearchCriteriaExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repo;

    private final CommentaryRepository repoCommentary;

    private final UserRepository userRepository;

    public String postExists = "The list of post is empty";

    public PageResponse<PostResponse> findAllPost(String s, Pageable pageable) {
        List<SearchCriteria> params = SearchCriteriaExtractor.extractSearchCriteriaList(s);
        PageResponse<PostResponse> res = search(params, pageable);
        if (res.getContent().isEmpty()) {
            throw new GlobalEntityListNotFounException("The list of post is empty in that page");
        }
        return res;

    }


    public PageResponse<PostResponse> findAllPostByUserName(String username, Pageable pageable) {
        PageResponse<PostResponse> res = pageablePost(username, pageable);
        if (res.getContent().isEmpty()) {
            throw new GlobalEntityNotFounException(postExists);
        }
        return res;
    }

    public PageResponse<PostResponse> pageablePost(String username, Pageable pageable) {
        Page<Post> postOfOneUserByUserName = repo.findAllPostOfOneUserByUserName(username, pageable);
        Page<PostResponse> postResponsePage =
                new PageImpl<>
                        (postOfOneUserByUserName.stream().toList(), pageable, postOfOneUserByUserName.getTotalPages()).map(PostResponse::of);
        return new PageResponse<>(postResponsePage);
    }

    public PageResponse<PostResponse> search(List<SearchCriteria> params, Pageable pageable) {
        GenericSpecificationBuilder genericSpecificationBuilder = new GenericSpecificationBuilder(params);
        Specification<Post> spec = genericSpecificationBuilder.build();
        Page<PostResponse> postResponsePage = repo.findAll(spec, pageable).map(PostResponse::of);
        return new PageResponse<>(postResponsePage);
    }


    /**
     * Crear un post
     *
     * @param postRequest
     * @param user
     * @return
     */
    public PostResponse createPostByUser(PostRequest postRequest, User user) {
        return PostResponse.of(
                repo.save(Post.builder()
                        .tag(postRequest.getTag())
                        .image(postRequest.getImage())
                        .author(user)
                        .description(postRequest.getDescription())
                        .build())
        );
    }


    public PostResponse giveCommentByUser(Long id, User user, CommentaryRequest request) {
        return repo.findById(id).map(post -> {
            post.getCommentaries().add(Commentary.builder()
                    .post(post)
                    .author(user.getUsername())
                    .message(request.getMessage())
                    .build());
            repo.save(post);
            return PostResponse.of(post);
        }).orElseThrow(() -> new GlobalEntityNotFounException(postExists));
    }

    public PostResponse giveLikeByUser(Long id, User user) {
        Optional<Post> optionalPost = repo.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            List<User> likes = post.getLikes();
            boolean exists = repo.existsLikeByUser(id, user.getId());
            if (!exists) {
                likes.add(user);
            } else {
                likes.remove(likes.indexOf(user) + 1);
                repo.save(post);
            }
            return PostResponse.of(repo.save(post));
        }
        throw new GlobalEntityNotFounException(postExists);
    }


    public ResponseEntity<?> deletePostByUser(Long id, UUID idU, User user) {
        Optional<User> optionalUser = userRepository.findById(idU);
        Optional<Post> optionalPost = repo.findById(id);

        if (optionalUser.isPresent() && optionalPost.isPresent()) {
            User userWithPost = optionalUser.get();
            Post postToDelete = optionalPost.get();
            if (userWithPost.getMyPost().contains(postToDelete)) {
                if (userWithPost.getId().equals(user.getId()) || user.getRoles().contains(UserRole.ADMIN)) {
                    userWithPost.getMyPost().remove(postToDelete);
                    repo.delete(postToDelete);
                    userRepository.save(userWithPost);
                    return ResponseEntity.noContent().build();
                }
                throw new GlobalEntityNotFounException("You not have permission for delete that post!");

            }
            throw new GlobalEntityNotFounException("You not content this post!");


        }

        throw new GlobalEntityNotFounException("The user or the post not found");
    }

    public ResponseEntity<?> deleteCommentary(Long id) {
        Optional<Commentary> commentaryOptional = repoCommentary.findById(id);
        if (commentaryOptional.isPresent()) {
            repoCommentary.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        throw new GlobalEntityNotFounException("The commentary not exists");
    }
}
