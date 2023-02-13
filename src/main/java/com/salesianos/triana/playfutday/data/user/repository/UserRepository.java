package com.salesianos.triana.playfutday.data.user.repository;

import com.salesianos.triana.playfutday.data.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findFirstByUsername(String username);

    @EntityGraph(value = "user_with_posts", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);
}