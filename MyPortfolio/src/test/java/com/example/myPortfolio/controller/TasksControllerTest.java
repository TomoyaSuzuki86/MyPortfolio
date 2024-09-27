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

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.service.TasksService;

class TasksControllerTest extends TasksController {

  // モックにするTaskServiceクラス
  @Mock
  private TasksService tasksService;

  // テスト対象のTasksControllerクラス
  @InjectMocks
  private TasksController tasksController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTasks() {
    // モックの振る舞いを設定
    Tasks mockTasks = new Tasks(null, null, "Sample Tasks", 120, 0, null, null);

    // サービスのcreateTasksメソッドが呼ばれたとき、mockTasksを返す
    when(tasksService.createTasks(any(Tasks.class))).thenReturn(mockTasks);

    // コントローラのcreateTasksメソッドを実行
    ResponseEntity<Tasks> response = tasksController.createTasks(mockTasks);

    // タスクが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockTasks, response.getBody());

    // サービスのcreateTasksが1回呼ばれたことを確認
    verify(tasksService, times(1)).createTasks(any(Tasks.class));
  }

  @Test
  public void testGetTasksByUsers() {
    // モックのタスクリストを作成
    List<Tasks> mockTasks = new ArrayList<>();
    Tasks tasks1 = new Tasks(null, null, "Tasks 1", 60, 0, null, null);
    mockTasks.add(tasks1);

    Tasks tasks2 = new Tasks(null, null, "Tasks 2", 90, 0, null, null);
    mockTasks.add(tasks2);

    // サービスのgetTasksByUsersメソッドが呼ばれたとき、mockTasksを返す
    when(tasksService.findAllByUsersId(anyLong())).thenReturn(mockTasks);

    // コントローラのgetTasksByUsersメソッドを実行
    ResponseEntity<List<Tasks>> response = tasksController.getTasksByUsers(1L);

    // タスクリストが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockTasks, response.getBody());

    // サービスのfindAllByUsersが1回呼ばれたことを確認
    verify(tasksService, times(1)).findAllByUsersId(anyLong());
  }

  @Test
  public void testGetTasksByUsers_NotFound() {
    // サービスのfindAllByUsersメソッドが呼ばれたとき、空のリストを返す
    when(tasksService.findAllByUsersId(anyLong())).thenReturn(new ArrayList<>());

    // コントローラのgetTasksByUsersメソッドを実行
    ResponseEntity<List<Tasks>> response = tasksController.getTasksByUsers(1L);

    // タスクが見つからない場合、200 OKと空のリストを返す
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(0, response.getBody().size());

    // サービスのfindAllByUsersが1回呼ばれたことを確認
    verify(tasksService, times(1)).findAllByUsersId(anyLong());
  }

}
