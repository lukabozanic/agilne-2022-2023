package com.user.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Users findByEmail(String email);
	
	Users findByRole(Role role);
}