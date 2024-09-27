package com.example.myPortfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.entity.Users;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByUserAndDeleteFlag(Users user, int deleteFlag);
}