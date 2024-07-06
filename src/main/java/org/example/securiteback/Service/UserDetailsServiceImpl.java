package org.example.securiteback.Service;

import org.example.securiteback.Enitities.Personne;
import org.example.securiteback.Repo.PersonneRepository;
import org.example.securiteback.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PersonneService personneService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Personne personne = personneRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        if (!personne.isAccountNonLocked()) {
            if (personneService.unlockWhenTimeExpired(personne)) {
                // Account is unlocked, proceed with authentication
            } else {
                throw new RuntimeException("Account is locked, please contact admin");
            }
        }

        return UserDetailsImpl.build(personne);
    }
}