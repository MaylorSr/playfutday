package com.salesianos.triana.playfutday.data.post.repository;

import com.salesianos.triana.playfutday.data.post.dto.PostResponse;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthor(User author);

    @Query("SELECT p FROM Post p WHERE p.author.username = :username")
    List<Post> findAllPostOfOneUserByUserName(@Param("username") String username);


    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Post p JOIN p.likes l WHERE p.id = :postId AND l.id = :userId")
    boolean existsLikeByUser(@Param("postId") Long postId, @Param("userId") UUID userId);

}



