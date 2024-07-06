package org.example.securiteback.Web;

import org.example.securiteback.Enitities.Personne;
import org.example.securiteback.Repo.PersonneRepository;
import org.example.securiteback.Service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personnes")
public class PersonneController {

    @Autowired
    private PersonneService personneService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<Personne> createPersonne(@RequestBody Personne personne) {
        Personne createdPersonne = personneService.createPersonne(personne);
        return ResponseEntity.ok(createdPersonne);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Personne> updatePersonne(@PathVariable Long id, @RequestBody Personne personneDetails) {
        Personne updatedPersonne = personneService.updatePersonne(id, personneDetails);
        return ResponseEntity.ok(updatedPersonne);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<Void> deletePersonne(@PathVariable Long id) {
        personneService.deletePersonne(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<List<Personne>> getAllPersonnes() {
        List<Personne> personnes = personneService.getAllPersonnes();
        return ResponseEntity.ok(personnes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<Personne> getPersonneById(@PathVariable Long id) {
        Personne personne = personneService.getPersonneById(id);
        return ResponseEntity.ok(personne);
    }
}