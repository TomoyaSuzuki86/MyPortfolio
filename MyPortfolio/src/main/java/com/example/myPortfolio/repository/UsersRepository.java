package com.example.myPortfolio.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmail(String email);

  Optional<Users> findByEmailAndPassword(String email, String password);
}