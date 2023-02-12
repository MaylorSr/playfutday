package com.salesianos.triana.playfutday.data.post.repository;

import com.salesianos.triana.playfutday.data.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
