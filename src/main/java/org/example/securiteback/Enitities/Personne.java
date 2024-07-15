package org.example.securiteback.Enitities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.securiteback.Jwt.Role;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String password;

    private boolean isAccountNonLocked = true;

    private int failedAttempt = 0;

    private Date lockTime;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "personne", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dossier> listDossier = new ArrayList<>();
}