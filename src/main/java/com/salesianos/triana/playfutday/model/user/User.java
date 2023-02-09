package com.salesianos.triana.playfutday.model.user;

import com.salesianos.triana.playfutday.model.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_entity")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
            /*,
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
            */
    )
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NaturalId
    @Column(unique = true, updatable = false)
    private String userName;

    private String email;

    private String password;

    private String avatar;

    private String biography;

    @NotBlank()
    private int phone;


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> myPost = new ArrayList<>();


    private LocalDate birthday;
    /**ENTREGAR ROL COMO RESPUESTA PARA MOSTRAR UNA COSA O NO EN EL FLUTTER*/


    /**IMPLEMENTAR UNA CLASE QUE SEA ROLES QUE TENGA UNA COLECCION DE SET DE USERROLES SET<USERROLE> O TENER UN
     * ENUMSET<USERROLE>
     * PASAR DE @ELEMENTCOLLECTION A SER ATTRIBUTECONVERTER<ROLES, STRING> EN LA BD SE GUARDARÁ COMO CON "ADMIN, MANAGER"
     * **/
    /*
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;

    /**SI SE CAMBIA A FALSE LO QUE PODEMOS HACER CON ESO ES BANEARLO*/
            /*
    @Builder.Default
    private boolean enabled = true;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<UserRole> roles;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

     */


}
