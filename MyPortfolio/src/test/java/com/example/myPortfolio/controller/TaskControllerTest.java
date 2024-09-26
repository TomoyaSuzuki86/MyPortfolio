package com.example.myPortfolio.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.service.TaskService;

class TaskControllerTest extends TaskController {

  // モックにするTaskServiceクラス
  @Mock
  private TaskService taskService;

  // テスト対象のTaskControllerクラス
  @InjectMocks
  private TaskController taskController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTask() {
    // モックの振る舞いを設定
    Task mockTask = new Task(null, null, "Sample Task", 120, 0, null, null);

    // サービスのcreateTaskメソッドが呼ばれたとき、mockTaskを返す
    when(taskService.createTask(any(Task.class))).thenReturn(mockTask);

    // コントローラのcreateTaskメソッドを実行
    ResponseEntity<Task> response = taskController.createTask(mockTask);

    // タスクが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockTask, response.getBody());

    // サービスのcreateTaskが1回呼ばれたことを確認
    verify(taskService, times(1)).createTask(any(Task.class));
  }

  @Test
  public void testGetTasksByUser() {
    // モックのタスクリストを作成
    List<Task> mockTasks = new ArrayList<>();
    Task task1 = new Task(null, null, "Task 1", 60, 0, null, null);
    mockTasks.add(task1);

    Task task2 = new Task(null, null, "Task 2", 90, 0, null, null);
    mockTasks.add(task2);

    // サービスのgetTasksByUserメソッドが呼ばれたとき、mockTasksを返す
    when(taskService.findAllByUserId(anyLong())).thenReturn(mockTasks);

    // コントローラのgetTasksByUserメソッドを実行
    ResponseEntity<List<Task>> response = taskController.getTasksByUser(1L);

    // タスクリストが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockTasks, response.getBody());

    // サービスのfindAllByUserが1回呼ばれたことを確認
    verify(taskService, times(1)).findAllByUserId(anyLong());
  }

  @Test
  public void testGetTasksByUser_NotFound() {
    // サービスのfindAllByUserメソッドが呼ばれたとき、空のリストを返す
    when(taskService.findAllByUserId(anyLong())).thenReturn(new ArrayList<>());

    // コントローラのgetTasksByUserメソッドを実行
    ResponseEntity<List<Task>> response = taskController.getTasksByUser(1L);

    // タスクが見つからない場合、200 OKと空のリストを返す
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());

    // サービスのfindAllByUserが1回呼ばれたことを確認
    verify(taskService, times(1)).findAllByUserId(anyLong());
  }

}
