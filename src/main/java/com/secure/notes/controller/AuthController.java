package com.secure.notes.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.secure.notes.model.AppRole;
import com.secure.notes.model.Role;
import com.secure.notes.model.User;
import com.secure.notes.repositories.RoleRepository;
import com.secure.notes.repositories.UserRepository;
import com.secure.notes.security.jwt.JwtUtils;
import com.secure.notes.security.request.LoginRequest;
import com.secure.notes.security.request.SignupRequest;
import com.secure.notes.security.response.LoginResponse;
import com.secure.notes.security.response.MessageResponse;
import com.secure.notes.security.response.UserInfoResponse;
import com.secure.notes.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder encoder;
	private final UserService userService;
	@PostMapping("/public/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad credentials");
			map.put("status", false);
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		LoginResponse response = new LoginResponse(userDetails.getUsername(), jwtToken, roles);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/public/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already in use!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already in use!"));
		}

		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));
		Set<String> strRoles = signUpRequest.getRole();

		Role role;
		if (strRoles == null || strRoles.isEmpty()) {
			role = roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
		}

		else {
			String roleStr = strRoles.iterator().next();
			if (roleStr.equals("admin")) {
				role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			} 
			else {
				role = roleRepository.findByRoleName(AppRole.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			}
			user.setAccountNonLocked(true);
			user.setAccountNonExpired(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);
			user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
			user.setAccountExpiryDate(LocalDate.now().plusYears(1));
			user.setTwoFactorEnabled(false);
			user.setSignUpMethod("email");

		}

		user.setRole(role);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
	
	
	@GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) 
	{
		
		User user = userService.findByUsername(userDetails.getUsername());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),   
                roles
        );
        return ResponseEntity.ok().body(response);
    }
	
	
	@GetMapping("/{username}")
    public String getCurrenUsername(@AuthenticationPrincipal UserDetails userDetails)		
    {
	   return (userDetails!=null) ? userDetails.getUsername():"";
    }
	
}
