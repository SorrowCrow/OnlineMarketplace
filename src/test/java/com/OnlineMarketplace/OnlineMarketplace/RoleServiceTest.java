package com.OnlineMarketplace.OnlineMarketplace;

import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleRepository;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);
    }

    @Test
    void testFindAll_ReturnsListOfRoles() {
        when(roleRepository.findAll()).thenReturn(List.of(role));
        List<Role> roles = roleService.findAll();
        assertEquals(1, roles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testFindByName_WhenRoleExists() {
        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        Optional<Role> result = roleService.findByName(ERole.ROLE_USER);
        assertTrue(result.isPresent());
        assertEquals(ERole.ROLE_USER, result.get().getName());
    }

    @Test
    void testFindByName_WhenRoleDoesNotExist() {
        when(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        Optional<Role> result = roleService.findByName(ERole.ROLE_ADMIN);
        assertFalse(result.isPresent());
    }

    @Test
    void testFindById_WhenRoleExists() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(role));
        Optional<Role> result = roleService.findById(1);
        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testFindById_WhenRoleDoesNotExist() {
        when(roleRepository.findById(2)).thenReturn(Optional.empty());
        Optional<Role> result = roleService.findById(2);
        assertFalse(result.isPresent());
    }
}
