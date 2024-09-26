package com.example.myPortfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Achievement;
import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.repository.AchievementRepository;
import com.example.myPortfolio.repository.TaskRepository;

@Service
public class AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private TaskRepository taskRepository;
    
    public Achievement createAchievement(Achievement achievement) {
        // 実績作成ロジック
        return achievementRepository.save(achievement);
    }
    
    public List<Achievement> findByTaskId(Long taskId) {
      Task task = taskRepository.findById(taskId).orElse(null);
      if (task != null) {
          return achievementRepository.findByTaskAndDeleteFlag(task, 0);
      }
      return new ArrayList<>();
  }
}