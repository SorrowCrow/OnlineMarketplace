package com.OnlineMarketplace.OnlineMarketplace.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * Get all roles.
     *
     * @return List of all roles.
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Get a role by its name.
     *
     * @param name The name of the role (e.g., ROLE_USER).
     * @return The role if found, otherwise 404 Not Found.
     */
    @GetMapping("/{name}")
    public ResponseEntity<Role> getRoleByName(@PathVariable ERole name) {
        Optional<Role> role = roleService.findByName(name);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Get a role by its name.
     *
     * @param id The id of the role (e.g., 1,2...).
     * @return The role if found, otherwise 404 Not Found.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Role> getRoleByName(@PathVariable Integer id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}