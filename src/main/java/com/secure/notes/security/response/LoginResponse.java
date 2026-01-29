package com.secure.notes.security.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter	
@AllArgsConstructor
public class LoginResponse 
{
	
	private String username;
	private String jwtToken;
	private List<String> roles;

}
