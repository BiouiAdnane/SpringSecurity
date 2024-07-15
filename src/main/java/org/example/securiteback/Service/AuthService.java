package org.example.securiteback.Service;

import org.example.securiteback.Enitities.Personne;
import org.example.securiteback.Repo.PersonneRepository;
import org.example.securiteback.Security.JwtUtils;
import org.example.securiteback.Security.UserDetailsImpl;
import org.example.securiteback.payload.request.LoginRequest;
import org.example.securiteback.payload.response.JwtResponse;
import org.example.securiteback.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PersonneRepository personneRepository;
    private final JwtUtils jwtUtils;
    private final PersonneService personneService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, PersonneRepository personneRepository, JwtUtils jwtUtils, PersonneService personneService) {
        this.authenticationManager = authenticationManager;
        this.personneRepository = personneRepository;
        this.jwtUtils = jwtUtils;
        this.personneService = personneService;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Optional<Personne> userOpt = personneRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            Personne user = userOpt.get();
            if (!user.isAccountNonLocked()) {
                throw new RuntimeException("User account is locked. Please contact admin.");
            }
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            personneService.resetFailedAttempts(userDetails.getUsername());

            return new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getFullName(),
                    userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        } catch (BadCredentialsException e) {
            personneService.increaseFailedAttempts(loginRequest.getEmail());
            int attemptsLeft = 5 - personneService.getFailedAttempts(loginRequest.getEmail());
            if (attemptsLeft <= 0) {
                throw new RuntimeException("Account locked due to too many failed login attempts.");
            }
            throw new RuntimeException("Invalid credentials. Attempts left: " + attemptsLeft);
        }
    }

    public MessageResponse logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new MessageResponse("User logged out successfully!");
    }
}