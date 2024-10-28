package com.example.myPortfolio.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;

public interface AchievementsRepository extends JpaRepository<Achievements, Long> {
  List<Achievements> findByTasksAndDeleteFlag(Tasks tasks, int deleteFlag);

  List<Achievements> findByTasksIdAndDeleteFlag(Long tasksId, int deleteFlag);

  // タスクIDと今日の日付に基づく実績を取得
  @Query("SELECT a FROM Achievements a WHERE a.tasks.id = :taskId AND a.deleteFlag = 0 AND a.createdAt >= :startOfDay AND a.createdAt < :endOfDay")
  List<Achievements> findByTasksIdAndCreatedAtBetween(@Param("taskId") Long taskId,
      @Param("startOfDay") Date startOfDay, @Param("endOfDay") Date endOfDay);
}