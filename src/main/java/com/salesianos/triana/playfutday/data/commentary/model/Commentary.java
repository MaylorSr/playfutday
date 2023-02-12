package com.salesianos.triana.playfutday.data.commentary.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.triana.playfutday.data.post.model.Post;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "commentary_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commentary {
    @Id
    @GeneratedValue()
    private Long id;

    private String message;

    @CreatedDate
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate updateCommentary = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_COMMENTARY_IN_POST"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_COMMENTARY_BY_USER"))
    private User user;

}