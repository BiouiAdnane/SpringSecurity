package org.example.securiteback;

import org.example.securiteback.Enitities.Personne;
import org.example.securiteback.Enum.Permissions;
import org.example.securiteback.Jwt.Permission;
import org.example.securiteback.Jwt.Role;
import org.example.securiteback.Repo.PermissionRepository;
import org.example.securiteback.Repo.PersonneRepository;
import org.example.securiteback.Repo.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SecuriteBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuriteBackApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	//@Bean
	CommandLineRunner run(PersonneRepository personneRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
		return args -> {

			// Create Permissions
			Permission readPermission = new Permission(null, Permissions.USER_READ);
			Permission createPermission = new Permission(null, Permissions.USER_CREATE);
			Permission updatePermission = new Permission(null, Permissions.USER_UPDATE);
			Permission deletePermission = new Permission(null, Permissions.USER_DELETE);

			permissionRepository.save(readPermission);
			permissionRepository.save(createPermission);
			permissionRepository.save(updatePermission);
			permissionRepository.save(deletePermission);

			// Create Roles and set Permissions
			Role adminRole = new Role();
			adminRole.setName("ROLE_ADMIN");
			adminRole.setPermissions(new HashSet<>(Set.of(readPermission, createPermission, updatePermission, deletePermission)));

			Role supervisorRole = new Role();
			supervisorRole.setName("ROLE_SUPERVISOR");
			supervisorRole.setPermissions(new HashSet<>(Set.of(readPermission, createPermission, updatePermission)));

			Role managerRole = new Role();
			managerRole.setName("ROLE_MANAGER");
			managerRole.setPermissions(new HashSet<>(Set.of(readPermission, createPermission)));

			Role visitorRole = new Role();
			visitorRole.setName("ROLE_VISITOR");
			visitorRole.setPermissions(new HashSet<>(Set.of(readPermission)));

			roleRepository.save(adminRole);
			roleRepository.save(supervisorRole);
			roleRepository.save(managerRole);
			roleRepository.save(visitorRole);

			// Create Users
			Personne admin = new Personne();
			admin.setNom("Adnane");
			admin.setEmail("admin@example.com");
			admin.setPassword(passwordEncoder.encode("adminpassword"));
			admin.setRole(adminRole);
			personneRepository.save(admin);

			Personne supervisor = new Personne();
			supervisor.setNom("Ahmed");
			supervisor.setEmail("supervisor@example.com");
			supervisor.setPassword(passwordEncoder.encode("supervisorpassword"));
			supervisor.setRole(supervisorRole);
			personneRepository.save(supervisor);

			Personne manager = new Personne();
			manager.setNom("Mourad");
			manager.setEmail("manager@example.com");
			manager.setPassword(passwordEncoder.encode("managerpassword"));
			manager.setRole(managerRole);
			personneRepository.save(manager);

			Personne visitor = new Personne();
			visitor.setNom("Youssef");
			visitor.setEmail("visitor@example.com");
			visitor.setPassword(passwordEncoder.encode("visitorpassword"));
			visitor.setRole(visitorRole);
			personneRepository.save(visitor);
		};
	}
}