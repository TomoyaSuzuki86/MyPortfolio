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

import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.service.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    Task newTask = taskService.createTask(task);
    return ResponseEntity.ok(newTask);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
    List<Task> tasks = taskService.findAllByUserId(userId);
    if (tasks.isEmpty()) {
      return ResponseEntity.ok(new ArrayList<>()); // 空のリストを返す
    }
    return ResponseEntity.ok(tasks); // タスクが見つかった場合はそのリストを返す
  }
}
