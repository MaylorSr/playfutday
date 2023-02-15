package com.salesianos.triana.playfutday.data.post.repository;

import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query("""
            SELECT p FROM Post p JOIN User u ON (p.author.id  = u.id) where u.username =:username order by p.uploadDate desc
            """)
    Page<Post> findAllPostOfOneUserByUserName(@Param("username") String username, Pageable pageable);

    @Query("""
            SELECT p FROM Post p JOIN User u ON (p.author.id  = u.id) where u.id =:uuid
            """)
    List<Post> findAllPostOfUser(@Param("uuid") UUID uuid);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Post p JOIN p.likes l WHERE p.id = :postId AND l.id = :userId")
    boolean existsLikeByUser(@Param("postId") Long postId, @Param("userId") UUID userId);

    @Query("""
            SELECT p FROM Post p JOIN p.likes l WHERE l.id =:id order by p.uploadDate desc
            """)
    Page<Post> findAllPostFavUser(@Param("id") UUID id, Pageable pageable);


}




