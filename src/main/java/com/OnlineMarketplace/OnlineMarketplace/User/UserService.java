package com.OnlineMarketplace.OnlineMarketplace.User;

import com.OnlineMarketplace.OnlineMarketplace.Auth.SignUpRequestDTO;
import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void deleteByUsername(String username) {
        userRepository.deleteByEmail(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(SignUpRequestDTO request) {
        User user = new User(request.getEmail(), request.getName(), request.getSurname(), request.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(role);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User createUser(User user, Set<String> roleNames) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(ERole.valueOf(roleName))
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);
        return userRepository.save(user);
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User updateUser(Long id, User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getSurname() != null) {
                existingUser.setSurname(user.getSurname());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }


    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
