package com.example.myPortfolio.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myPortfolio.entity.Achievement;
import com.example.myPortfolio.service.AchievementService;

@RestController
@RequestMapping("/achievements")
public class AchievementController {

  @Autowired
  private AchievementService achievementService;

  @PostMapping
  public ResponseEntity<Achievement> createAchievement(@RequestBody Achievement achievement) {
    Achievement newAchievement = achievementService.createAchievement(achievement);
    return ResponseEntity.ok(newAchievement);
  }

  @GetMapping("/task/{taskId}")
  public ResponseEntity<List<Achievement>> getAchievementsByTask(@PathVariable Long taskId) {
    List<Achievement> achievements = achievementService.findByTaskId(taskId);
    if (achievements.isEmpty()) {
      return ResponseEntity.ok(new ArrayList<>());  // 空のリストを返す
    }
    return ResponseEntity.ok(achievements);  // 成功した場合のリストを返す
  }
  
}
