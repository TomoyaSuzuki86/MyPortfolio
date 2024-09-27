package com.example.myPortfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.TaskRepository;
import com.example.myPortfolio.repository.UsersRepository;

@Service
public class TaskService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private UsersRepository userRepository;

  public Task createTask(Task task) {
    // タスク作成ロジック
    return taskRepository.save(task);
  }

  public List<Task> findAllByUserId(Long userId) {
    Users user = userRepository.findById(userId).orElse(null);
    if (user != null) {
      return taskRepository.findByUserAndDeleteFlag(user, 0);
    }
    return new ArrayList<>();
  }
}
