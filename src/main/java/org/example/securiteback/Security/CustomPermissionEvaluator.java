package org.example.securiteback.Security;

import org.example.securiteback.Enitities.Personne;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !(permission instanceof String permissionName)) {
            return false;
        }

        Personne personne = (Personne) authentication.getPrincipal();

        return personne.getRole().getPermissions().stream()
                .anyMatch(p -> p.getName().getName().equals(permissionName));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}