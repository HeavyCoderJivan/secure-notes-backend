package com.secure.notes.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.model.AppRole;
import com.secure.notes.model.Role;
import com.secure.notes.model.User;
import com.secure.notes.repositories.RoleRepository;
import com.secure.notes.repositories.UserRepository;
import com.secure.notes.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService 
{
    
   
    private final  UserRepository userRepository;

    private final  RoleRepository roleRepository;

    @Override
    public void updateUserRole(Long userId, String roleName) {
    	
        User user = userRepository.findById(userId).orElseThrow(
        		() -> new RuntimeException("User not found"));
        
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        
        
        user.setRole(role);
        userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserDTO getUserById(Long id) 
    { 
       //return userRepository.findById(id).orElseThrow();
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }
    
    @Override
    public User findByUsername(String username) 
    {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() ->
        new RuntimeException("User not found with username: " + username));
    }



}
