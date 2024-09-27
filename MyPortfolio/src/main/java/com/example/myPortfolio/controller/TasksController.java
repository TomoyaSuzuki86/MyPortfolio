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

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.service.TasksService;

@RestController
@RequestMapping("/tasks")
public class TasksController {

  @Autowired
  private TasksService tasksService;

  @PostMapping
  public ResponseEntity<Tasks> createTasks(@RequestBody Tasks tasks) {
    Tasks newTasks = tasksService.createTasks(tasks);
    return ResponseEntity.ok(newTasks);
  }

  @GetMapping("/{usersId}")
  public ResponseEntity<List<Tasks>> getTasksByUsers(@PathVariable Long usersId) {
    List<Tasks> tasks = tasksService.findAllByUsersId(usersId);
    if (tasks.isEmpty()) {
      return ResponseEntity.ok(new ArrayList<>()); // 空のリストを返す
    }
    return ResponseEntity.ok(tasks); // タスクが見つかった場合はそのリストを返す
  }
}
