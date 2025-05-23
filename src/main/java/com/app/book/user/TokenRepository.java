package com.app.book.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long>{
	
	Optional<Token> findByToken(String token);
}
