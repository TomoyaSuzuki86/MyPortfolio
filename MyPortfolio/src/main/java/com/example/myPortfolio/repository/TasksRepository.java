package com.example.myPortfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
  List<Tasks> findByUsersAndDeleteFlag(Users users, int deleteFlag);
}