package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleController;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);
    }

    @Test
    void testGetAllRoles() {
        when(roleService.findAll()).thenReturn(List.of(role));
        ResponseEntity<List<Role>> response = roleController.getAllRoles();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(roleService, times(1)).findAll();
    }

    @Test
    void testGetRoleByName_WhenRoleExists() {
        when(roleService.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        ResponseEntity<Role> response = roleController.getRoleByName(ERole.ROLE_USER);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ERole.ROLE_USER, response.getBody().getName());
    }

    @Test
    void testGetRoleByName_WhenRoleDoesNotExist() {
        when(roleService.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        ResponseEntity<Role> response = roleController.getRoleByName(ERole.ROLE_ADMIN);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetRoleById_WhenRoleExists() {
        when(roleService.findById(1)).thenReturn(Optional.of(role));
        ResponseEntity<Role> response = roleController.getRoleByName(1);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testGetRoleById_WhenRoleDoesNotExist() {
        when(roleService.findById(2)).thenReturn(Optional.empty());
        ResponseEntity<Role> response = roleController.getRoleByName(2);
        assertEquals(404, response.getStatusCodeValue());
    }
}
