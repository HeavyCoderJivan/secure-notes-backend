package com.secure.notes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.secure.notes.dtos.UserDTO;
import com.secure.notes.model.User;
import com.secure.notes.services.UserService;
import lombok.RequiredArgsConstructor;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController 
{
	
	private final UserService userService;
	
	@GetMapping("/getusers")
	public ResponseEntity<List<User>> getAllUsers() 
	{
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	@PutMapping("/update-role")
	public ResponseEntity<String> updateUserRole(@RequestParam Long userId, @RequestParam String roleName) {
		userService.updateUserRole(userId, roleName);
		return ResponseEntity.ok("User role updated");
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}

}
