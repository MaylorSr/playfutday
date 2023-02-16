package com.salesianos.triana.playfutday.data.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.salesianos.triana.playfutday.data.commentary.model.Commentary;
import com.salesianos.triana.playfutday.data.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Post implements Serializable {
    @Id
    @GeneratedValue()
    private Long id;

    private String tag;

    private String description;

    private String image;

    @CreatedDate
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime uploadDate = LocalDateTime.now();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_POST_USER"))
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likes;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.MERGE)
    @Builder.Default
    private List<Commentary> commentaries = new ArrayList<>();
/*

    @PreRemove
    public void preRemovePost() {
        this.getLikes().clear();
    }*/
}

