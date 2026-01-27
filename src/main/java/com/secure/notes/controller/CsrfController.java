package com.secure.notes.controller;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CsrfController 
{
	@GetMapping("/api/csrf-token")
	public CsrfToken csrfToken(HttpServletRequest req) 
	{	
		return (CsrfToken) req.getAttribute(CsrfToken.class.getName());	
	}

}
