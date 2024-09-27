package com.example.myPortfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.repository.AchievementsRepository;
import com.example.myPortfolio.repository.TasksRepository;

@Service
public class AchievementsService {

    @Autowired
    private AchievementsRepository achievementsRepository;

    @Autowired
    private TasksRepository tasksRepository;
    
    public Achievements createAchievements(Achievements achievements) {
        // 実績作成ロジック
        return achievementsRepository.save(achievements);
    }
    
    public List<Achievements> findByTasksId(Long tasksId) {
      Tasks tasks = tasksRepository.findById(tasksId).orElse(null);
      if (tasks != null) {
          return achievementsRepository.findByTasksAndDeleteFlag(tasks, 0);
      }
      return new ArrayList<>();
  }
}