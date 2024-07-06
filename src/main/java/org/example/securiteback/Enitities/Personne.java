package org.example.securiteback.Enitities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.securiteback.Jwt.Role;

import java.util.Date;

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
}