package com.salesianos.triana.playfutday.data.user.repository;

import com.salesianos.triana.playfutday.data.user.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findFirstByUsername(String username);


    @EntityGraph(value = "user_with_posts", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);

    boolean existsByEmailIgnoreCase(String s);

    boolean existsByPhoneIgnoreCase(String s);

}

