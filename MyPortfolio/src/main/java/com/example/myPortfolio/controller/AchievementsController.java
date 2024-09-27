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

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.service.AchievementsService;

@RestController
@RequestMapping("/achievementss")
public class AchievementsController {

  @Autowired
  private AchievementsService achievementsService;

  @PostMapping
  public ResponseEntity<Achievements> createAchievements(@RequestBody Achievements achievements) {
    Achievements newAchievements = achievementsService.createAchievements(achievements);
    return ResponseEntity.ok(newAchievements);
  }

  @GetMapping("/tasks/{tasksId}")
  public ResponseEntity<List<Achievements>> getAchievementssByTasks(@PathVariable Long tasksId) {
    List<Achievements> achievementss = achievementsService.findByTasksId(tasksId);
    if (achievementss.isEmpty()) {
      return ResponseEntity.ok(new ArrayList<>());  // 空のリストを返す
    }
    return ResponseEntity.ok(achievementss);  // 成功した場合のリストを返す
  }
  
}
