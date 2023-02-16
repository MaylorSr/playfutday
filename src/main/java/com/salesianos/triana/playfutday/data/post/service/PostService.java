package com.salesianos.triana.playfutday.data.post.service;

import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.commentary.repository.CommentaryRepository;
import com.salesianos.triana.playfutday.data.files.service.FileSystemStorageService;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.model.UserRole;
import com.salesianos.triana.playfutday.data.user.repository.UserRepository;
import com.salesianos.triana.playfutday.exception.GlobalEntityListNotFounException;
import com.salesianos.triana.playfutday.exception.GlobalEntityNotFounException;
import com.salesianos.triana.playfutday.exception.NotPermission;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repo;

    private final CommentaryRepository repoCommentary;

    private final UserRepository userRepository;

    private final FileSystemStorageService storageService;

    public String postExists = "The list of post is empty";

    public PageResponse<PostResponse> findAllPost(String s, Pageable pageable) {
        List<SearchCriteria> params = SearchCriteriaExtractor.extractSearchCriteriaList(s);
        PageResponse<PostResponse> res = search(params, pageable);
        if (res.getContent().isEmpty()) {
            throw new GlobalEntityListNotFounException(postExists);
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

    public PostResponse createPostByUser(PostRequest postRequest, MultipartFile image, User user) {
        String filename = storageService.store(image);

        return PostResponse.of(
                repo.save(Post.builder()
                        .tag(postRequest.getTag())
                        .image(filename)
                        .author(user)
                        .description(postRequest.getDescription())
                        .build())
        );
    }


    public PostResponse giveCommentByUser(Long id, User user, CommentaryRequest request) {
        return repo.findById(id).map(
                post -> {
                    post.getCommentaries().add(Commentary.builder()
                            .post(post)
                            .author(user.getUsername())
                            .message(request.getMessage())
                            .build());
                    repo.save(post);
                    return PostResponse.of(post);
                }
        ).orElseThrow(() -> new GlobalEntityNotFounException(postExists));
    }

    public PostResponse giveLikeByUser(Long id, User user) {
        return repo.findById(id).map(
                post -> {
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
        ).orElseThrow(() -> new GlobalEntityNotFounException("The post not found!"));
    }


    public ResponseEntity<?> deletePostByUser(Long id, UUID idU, User user) {

        return repo.findById(id).map(
                post -> {
                    return userRepository.findById(idU)
                            .map(oldUser -> {
                                        if (oldUser.getMyPost().contains(post)) {
                                            if (oldUser.getId().equals(user.getId()) || user.getRoles().contains(UserRole.ADMIN)) {
                                                oldUser.getMyPost().remove(post);
                                                repo.delete(post);
                                                userRepository.save(oldUser);
                                                return ResponseEntity.noContent().build();
                                            }
                                            throw new NotPermission();
                                        }
                                        throw new GlobalEntityNotFounException("The post not found in your posts");
                                    }
                            ).orElseThrow(() -> new GlobalEntityNotFounException("The user not exists"));
                }
        ).orElseThrow(() -> new GlobalEntityNotFounException(postExists));
    }

    public ResponseEntity<?> deleteCommentary(Long id) {
        Commentary commentaryOptional = repoCommentary.findById(id).orElseThrow(() -> new GlobalEntityNotFounException("The commentary id not exists"));
        repoCommentary.delete(commentaryOptional);
        return ResponseEntity.noContent().build();
    }


    @Transactional
    public PostResponse createPostOptional(PostRequest postRequest, MultipartFile file, User user) {
        String filename = storageService.store(file);
        return PostResponse.of(
                repo.save(Post.builder()
                        .tag(postRequest.getTag())
                        .image(filename)
                        .author(user)
                        .description(postRequest.getDescription())
                        .build())
        );
    }


}
