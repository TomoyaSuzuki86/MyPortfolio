package com.example.myPortfolio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;

public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
  List<Achievements> findByTasksAndDeleteFlag(Tasks tasks, int deleteFlag);

  List<Achievements> findByTasksIdAndDeleteFlag(Long tasksId, int deleteFlag);
}