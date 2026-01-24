package com.secure.notes.repositories;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;

public interface UserRepository
{
	 Optional<User> findByUserName(String username);

}
