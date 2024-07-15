package org.example.securiteback.Security;

import lombok.Getter;
import org.example.securiteback.Enitities.Personne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;
    private final String email;
    private final String password;
    private final String fullName;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonLocked;

    public UserDetailsImpl(Long id, String email, String password, String fullName, Collection<? extends GrantedAuthority> authorities, boolean isAccountNonLocked) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.authorities = authorities;
        this.isAccountNonLocked = isAccountNonLocked;
    }


    public static UserDetailsImpl build(Personne personne) {
        List<GrantedAuthority> authorities = personne.getRole().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName().getName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                personne.getId(),
                personne.getEmail(),
                personne.getPassword(),
                personne.getNom() + " "+personne.getPrenom(),
                authorities,
                personne.isAccountNonLocked());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}