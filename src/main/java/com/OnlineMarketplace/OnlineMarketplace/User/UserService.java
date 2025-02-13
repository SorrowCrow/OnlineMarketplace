package com.OnlineMarketplace.OnlineMarketplace.User;

import com.OnlineMarketplace.OnlineMarketplace.Auth.SignUpRequestDTO;
import com.OnlineMarketplace.OnlineMarketplace.Role.ERole;
import com.OnlineMarketplace.OnlineMarketplace.Role.Role;
import com.OnlineMarketplace.OnlineMarketplace.Role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

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


        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
        user.setAccountVerified(false);

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(role);
        user.setRoles(roles);

        sendVerificationEmail(user.getEmail(), token);

        return userRepository.save(user);
    }

    private void sendVerificationEmail(String email, String token) {
        String subject = "Verify Your Email - Online Marketplace";
        String verificationUrl = "http://localhost:5173/verifyEmail?token=" + token;

        String message = "Thank you for registering! Please click the link below to verify your email:\n\n"
                + verificationUrl + "\n\n"
                + "This link will expire in 24 hours.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

    public boolean verifyUser(String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
                return false; // Token expired
            }
            user.setAccountVerified(true);
            user.setVerificationToken(null);
            user.setVerificationTokenExpiry(null);
            userRepository.save(user);
            return true;
        }
        return false;
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

    public User updateUserCart(User user) {
        Optional<User> dbUser = userRepository.findById(user.getId());

        if (dbUser.isPresent()) {
            User existingUser = dbUser.get();

            existingUser.setCart(user.getCart());

            return userRepository.save(existingUser);
        }
        return null;

    }

    public boolean resendVerificationEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && !userOptional.get().isAccountVerified()) {
            String newToken = UUID.randomUUID().toString();
            User user = userOptional.get();
            user.setVerificationToken(newToken);
            user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
            userRepository.save(user);
            sendVerificationEmail(user.getEmail(), newToken);
            return true;
        }
        return false;
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

    public User updateUserDetails(UserDetailsDTO userDetailsDTO, User user) {
        user.setName(userDetailsDTO.getName());
        user.setSurname(userDetailsDTO.getSurname());
        if(!user.getEmail().equals(userDetailsDTO.getEmail())){
            String token = UUID.randomUUID().toString();
            user.setVerificationToken(token);
            user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours
            user.setAccountVerified(false);
            sendVerificationEmail(userDetailsDTO.getEmail(), token);
        }
        user.setEmail(userDetailsDTO.getEmail());
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}