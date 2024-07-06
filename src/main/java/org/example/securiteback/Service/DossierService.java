package org.example.securiteback.Service;

import org.example.securiteback.Enitities.Dossier;
import org.example.securiteback.Repo.DossierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DossierService {

    @Autowired
    private DossierRepository dossierRepository;

    public Dossier createDossier(Dossier dossier) {
        return dossierRepository.save(dossier);
    }

    public Dossier updateDossier(Long id, Dossier dossierDetails) {
        Optional<Dossier> optionalDossier = dossierRepository.findById(id);

        if (optionalDossier.isPresent()) {
            Dossier dossier = optionalDossier.get();
            dossier.setTitre(dossierDetails.getTitre());
            dossier.setDescription(dossierDetails.getDescription());
            return dossierRepository.save(dossier);
        } else {
            throw new RuntimeException("Dossier not found with id " + id);
        }
    }

    public void deleteDossier(Long id) {
        Optional<Dossier> optionalDossier = dossierRepository.findById(id);

        if (optionalDossier.isPresent()) {
            dossierRepository.delete(optionalDossier.get());
        } else {
            throw new RuntimeException("Dossier not found with id " + id);
        }
    }

    public List<Dossier> getAllDossiers() {
        return dossierRepository.findAll();
    }

    public Dossier getDossierById(Long id) {
        return dossierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dossier not found with id " + id));
    }
}