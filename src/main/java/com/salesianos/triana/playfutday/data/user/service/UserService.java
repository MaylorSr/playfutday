package com.salesianos.triana.playfutday.data.user.service;


import com.salesianos.triana.playfutday.data.commentary.repository.CommentaryRepository;
import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.post.repository.PostRepository;
import com.salesianos.triana.playfutday.data.post.service.PostService;
import com.salesianos.triana.playfutday.data.user.dto.UserRequest;
import com.salesianos.triana.playfutday.data.user.dto.UserResponse;
import com.salesianos.triana.playfutday.data.user.model.User;
import com.salesianos.triana.playfutday.data.user.model.UserRole;
import com.salesianos.triana.playfutday.data.user.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private final PostService postService;


    public User createUser(UserRequest createUserRequest, EnumSet<UserRole> roles) {
        User user = User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .phone(createUserRequest.getPhone())
                .roles(roles)
                .build();
        return userRepository.save(user);
    }

    public ResponseEntity<?> deleteUser(UUID idU, User user) {
        Optional<User> optionalUser = userRepository.findById(idU);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getId().equals(user.getId()) || user.getRoles().contains(UserRole.ADMIN)) {
                userRepository.delete(optionalUser.get());
            }
            return ResponseEntity.noContent().build();

        }
        throw new GlobalEntityNotFounException("User not found with that id");
    }

    public User createUserWithUserRole(UserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UserRole.USER));
    }

    public User createUserWithAdminRole(UserRequest createUserRequest) {
        return createUser(createUserRequest, EnumSet.of(UserRole.ADMIN));
    }

    public PageResponse<UserResponse> findAll(String s, Pageable pageable) {
        List<SearchCriteria> params = SearchCriteriaExtractor.extractSearchCriteriaList(s);
        PageResponse<UserResponse> res = search(params, pageable);
        if (res.getContent().isEmpty()) {
            throw new GlobalEntityListNotFounException("The list of users is empty in that page");
        }
        return res;
    }


    public PageResponse<UserResponse> search(List<SearchCriteria> params, Pageable pageable) {
        GenericSpecificationBuilder genericSpecificationBuilder = new GenericSpecificationBuilder(params);
        Specification<User> spec = genericSpecificationBuilder.build();
        Page<UserResponse> userResponsePage = userRepository.findAll(spec, pageable).map(UserResponse::fromUser);
        return new PageResponse<>(userResponsePage);
    }


    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public PageResponse<PostResponse> findMyFavPost(User user, Pageable pageable) {
        PageResponse<PostResponse> res = pageablePost(pageable, user);
        if (res.getContent().isEmpty()) {
            throw new GlobalEntityListNotFounException("The list of post that you put is empty in that page");
        }
        return res;
    }


    public PageResponse<PostResponse> pageablePost(Pageable pageable, User user) {
        Page<Post> postListFav = postRepository.findAllPostFavUser(user.getId(), pageable);
        Page<PostResponse> postResponsePage =
                new PageImpl<>
                        (postListFav.stream().toList(), pageable, postListFav.getTotalPages()).map(PostResponse::of);
        return new PageResponse<>(postResponsePage);
    }


    public UserResponse banUser(UUID id) {
        return userRepository.findById(id).map(user1 -> {
            user1.setEnabled(!user1.isEnabled());
            return UserResponse.fromUser(
                    userRepository.save(user1)
            );
        }).orElseThrow(() -> new GlobalEntityNotFounException("The user with id not exists"));


    }

    public UserResponse addAdminRoleToUser(UUID id) {

        return userRepository.findById(id).map(old -> {
            if (old.getRoles().contains(UserRole.ADMIN)) {
                old.getRoles().remove(UserRole.ADMIN);
            } else {
                old.getRoles().add(UserRole.ADMIN);
            }
            userRepository.save(old);
            return UserResponse.fromUser(old);
        }).orElseThrow(() -> new GlobalEntityNotFounException("The user with that id not exists!"));
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findFirstByUsername(username);
    }

    public Optional<User> edit(User user) {
        return userRepository.findById(user.getId())
                .map(u -> {
                    u.setAvatar(user.getAvatar());
                    return userRepository.save(u);
                }).or(() -> Optional.empty());

    }

    public Optional<User> editPassword(UUID userId, String newPassword) {

        // Aquí no se realizan comprobaciones de seguridad. Tan solo se modifica

        return userRepository.findById(userId)
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(u);
                }).or(Optional::empty);

    }

    public void delete(User user) {
        deleteById(user.getId());
    }

    public void deleteById(UUID id) {
        // Prevenimos errores al intentar borrar algo que no existe
        if (userRepository.existsById(id))
            userRepository.deleteById(id);
    }

    public boolean passwordMatch(User user, String clearPassword) {
        return passwordEncoder.matches(clearPassword, user.getPassword());
    }


    public boolean userExistsEmail(String s) {
        return userRepository.existsByEmailIgnoreCase(s);
    }

    public boolean userPhoneUnique(String s) {
        return userRepository.existsByPhoneIgnoreCase(s);
    }


}
