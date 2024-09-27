package com.example.myPortfolio.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.TasksRepository;
import com.example.myPortfolio.repository.UsersRepository;

@Service
public class TasksService {

  @Autowired
  private TasksRepository tasksRepository;

  @Autowired
  private UsersRepository usersRepository;

  public Tasks createTasks(Tasks tasks) {
    // タスク作成ロジック
    return tasksRepository.save(tasks);
  }

  public List<Tasks> findAllByUsersId(Long usersId) {
    Users users = usersRepository.findById(usersId).orElse(null);
    if (users != null) {
      return tasksRepository.findByUsersAndDeleteFlag(users, 0);
    }
    return new ArrayList<>();
  }
}
