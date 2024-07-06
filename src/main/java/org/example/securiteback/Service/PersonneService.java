package org.example.securiteback.Service;

import org.example.securiteback.Enitities.Personne;
import org.example.securiteback.Repo.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonneService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Autowired
    private PersonneRepository personneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Personne createPersonne(Personne personne) {
        personne.setPassword(passwordEncoder.encode(personne.getPassword()));
        return personneRepository.save(personne);
    }

    public Personne updatePersonne(Long id, Personne personneDetails) {
        Optional<Personne> optionalPersonne = personneRepository.findById(id);

        if (optionalPersonne.isPresent()) {
            Personne personne = optionalPersonne.get();
            personne.setNom(personneDetails.getNom());
            personne.setPrenom(personneDetails.getPrenom());
            personne.setEmail(personneDetails.getEmail());
            if (personneDetails.getPassword() != null && !personneDetails.getPassword().isEmpty()) {
                personne.setPassword(passwordEncoder.encode(personneDetails.getPassword()));
            }
            return personneRepository.save(personne);
        } else {
            throw new RuntimeException("Personne not found with id " + id);
        }
    }

    public void deletePersonne(Long id) {
        Optional<Personne> optionalPersonne = personneRepository.findById(id);

        if (optionalPersonne.isPresent()) {
            personneRepository.delete(optionalPersonne.get());
        } else {
            throw new RuntimeException("Personne not found with id " + id);
        }
    }

    public List<Personne> getAllPersonnes() {
        return personneRepository.findAll();
    }

    public Personne getPersonneById(Long id) {
        return personneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Personne not found with id " + id));
    }

    public void increaseFailedAttempts(String email) {
        Optional<Personne> userOpt = personneRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Personne user = userOpt.get();
            int newFailAttempts = user.getFailedAttempt() + 1;
            user.setFailedAttempt(newFailAttempts);
            if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                user.setLockTime(new Date());
            }
            personneRepository.save(user);
        }
    }

    public void resetFailedAttempts(String email) {
        Optional<Personne> userOpt = personneRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Personne user = userOpt.get();
            user.setFailedAttempt(0);
            user.setLockTime(null);
            user.setAccountNonLocked(true);
            personneRepository.save(user);
        }
    }

    public int getFailedAttempts(String email) {
        Optional<Personne> userOpt = personneRepository.findByEmail(email);
        return userOpt.map(Personne::getFailedAttempt).orElse(0);
    }

    public boolean unlockWhenTimeExpired(Personne user) {
        long lockTimeInMillis = user.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();
        if (lockTimeInMillis + LOCK_TIME_DURATION < currentTimeInMillis) {
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            personneRepository.save(user);
            return true;
        }
        return false;
    }
}


