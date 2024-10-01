package com.example.myPortfolio.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
  List<Tasks> findByUsersAndDeleteFlag(Users users, int deleteFlag);

  List<Tasks> findByUsersIdAndDeleteFlag(Long usersId, int deleteFlag);

  Optional<Tasks> findByIdAndUsersId(Long usersId, Long taskId);
}