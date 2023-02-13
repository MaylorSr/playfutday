package com.salesianos.triana.playfutday.data.post.service;

import com.salesianos.triana.playfutday.data.commentary.dto.CommentaryRequest;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.commentary.repository.CommentaryRepository;
import com.salesianos.triana.playfutday.data.post.dto.PostRequest;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repo;

    private final CommentaryRepository repoCommentary;
    private final UserRepository userRepository;


    public List<PostResponse> findAllPost() {
        return repo.findAll().stream().map(PostResponse::of).toList();
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
        Commentary newCommentary = Commentary
                .builder()
                .message(request.getMessage())
                .user(user)
                .post(repo.findById(id).orElseThrow(() -> new RuntimeException("The post dont exists")))
                .build();
        return (
                repo.findById(id).map(post -> {
                    post.getCommentaries().add(newCommentary);
                    repo.save(post);
                    return PostResponse.of(post);
                }).orElseThrow(() -> new RuntimeException("The post dont exists"))
        );

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
        throw new RuntimeException("The");
    }


    public boolean deletePostByUser(Long id, User user) {
        if (user.getMyPost().contains(repo.findById(id))) {
            user.getMyPost().remove(repo.findById(id));
            return true;
        }
        return false;

    }





}