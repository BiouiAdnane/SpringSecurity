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

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isAccountNonLocked;

    public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean isAccountNonLocked) {
        this.id = id;
        this.email = email;
        this.password = password;
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