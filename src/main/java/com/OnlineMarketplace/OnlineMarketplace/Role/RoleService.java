package com.OnlineMarketplace.OnlineMarketplace.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Find all roles.
     *
     * @return A List containing the role if found, otherwise empty.
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Find a role by its name.
     *
     * @param name The name of the role (e.g., ERole.ROLE_USER).
     * @return An Optional containing the role if found, otherwise empty.
     */
    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

    /**
     * Get a role by its ID.
     *
     * @param id The ID of the role.
     * @return An Optional containing the role if found, otherwise empty.
     */
    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }
}
