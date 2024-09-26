package com.example.myPortfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Achievement;
import com.example.myPortfolio.entity.Task;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
  List<Achievement> findByTaskAndDeleteFlag(Task task, int deleteFlag);
}