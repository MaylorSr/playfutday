package com.salesianos.triana.playfutday.data.commentary.repository;

import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
}
