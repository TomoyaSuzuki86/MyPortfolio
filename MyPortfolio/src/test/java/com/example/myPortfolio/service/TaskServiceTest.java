package com.example.myPortfolio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.TaskRepository;
import com.example.myPortfolio.repository.UsersRepository;

class TaskServiceTest extends TaskService {

  // モックにするリポジトリ
  @Mock
  private TaskRepository taskRepository;

  @Mock
  private UsersRepository usersRepository;

  // テスト対象のTaskService
  @InjectMocks
  private TaskService taskService;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  /**
   * タスクを正しく作成するテスト。
   * <p>
   * モックの {@code Task} オブジェクトを使用して、{@link TaskService#createTask(Task)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックタスクの生成</li>
   * <li>{@code taskRepository.save()} が呼ばれたとき、モックタスクを返すことを確認</li>
   * <li>タスクが正しく作成されたことを検証</li>
   * <li>{@code taskRepository.save()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testCreateTask_Success() {
    // テスト用のモックタスクを作成
    Task mockTask = new Task(null, null, "Sample Task", 120, 0, null, null);

    // taskRepository.save() メソッドが呼ばれたときにモックタスクを返すように設定
    when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

    // TaskServiceのcreateTaskメソッドを実行
    Task createdTask = taskService.createTask(mockTask);

    // タスクが正しく作成されたことを検証
    assertNotNull(createdTask);
    assertEquals("Sample Task", createdTask.getTaskName());

    // taskRepositoryのsaveが1回呼ばれたことを検証
    verify(taskRepository, times(1)).save(any(Task.class));
  }

  /**
   * ユーザーIDからタスクを検索し、該当するタスクが存在する場合のテスト。
   * <p>
   * モックの {@code Task} オブジェクトと {@code Users} オブジェクトを使用して、
   * {@link TaskService#findAllByUserId(Long)} メソッドが正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックユーザーとタスクリストの生成</li>
   * <li>{@code usersRepository.findById()} が呼ばれたとき、モックユーザーを返すことを確認</li>
   * <li>{@code taskRepository.findByUserAndDeleteFlag()} が呼ばれたとき、モックタスクを返すことを確認
   * </li>
   * <li>タスクが正しく返されたことを検証</li>
   * <li>{@code taskRepository.findByUserAndDeleteFlag()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindAllByUserId_UserExists() {
    // テスト用のモックユーザーを作成
    Users mockUser = new Users(null, "test@example.com", "password", null, null);

    // テスト用のモックタスクリストを作成
    List<Task> mockTasks = new ArrayList<>();
    mockTasks.add(new Task(null, mockUser, "Task 1", 60, 0, null, null));
    mockTasks.add(new Task(null, mockUser, "Task 2", 90, 0, null, null));

    // usersRepository.findById() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

    // taskRepository.findByUserAndDeleteFlag() メソッドが呼ばれたときにモックタスクを返すように設定
    when(taskRepository.findByUserAndDeleteFlag(any(Users.class), anyInt())).thenReturn(mockTasks);

    // TaskServiceのfindAllByUserIdメソッドを実行
    List<Task> foundTasks = taskService.findAllByUserId(1L);

    // タスクが正しく返されたことを検証
    assertFalse(foundTasks.isEmpty());
    assertEquals(2, foundTasks.size());
    assertEquals("Task 1", foundTasks.get(0).getTaskName());

    // taskRepositoryのfindByUserAndDeleteFlagが1回呼ばれたことを検証
    verify(taskRepository, times(1)).findByUserAndDeleteFlag(any(Users.class), anyInt());
  }

  /**
   * ユーザーIDからタスクを検索し、該当するタスクが存在しない場合のテスト。
   * <p>
   * {@link TaskService#findAllByUserId(Long)} メソッドが空のリストを返す場合の動作を検証します。
   * 
   * <ul>
   * <li>{@code usersRepository.findById()} が呼ばれたときにモックユーザーを返すことを確認</li>
   * <li>{@code taskRepository.findByUserAndDeleteFlag()} が呼ばれたときに空のリストを返すことを確認
   * </li>
   * <li>タスクが存在しないことを検証</li>
   * </ul>
   */
  @Test
  public void testFindAllByUserId_NoTasks() {
    // テスト用のモックユーザーを作成
    Users mockUser = new Users(null, "test@example.com", "password", null, null);

    // usersRepository.findById() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));

    // taskRepository.findByUserAndDeleteFlag() メソッドが呼ばれたときに空のリストを返すように設定
    when(taskRepository.findByUserAndDeleteFlag(any(Users.class), anyInt())).thenReturn(new ArrayList<>());

    // TaskServiceのfindAllByUserIdメソッドを実行
    List<Task> foundTasks = taskService.findAllByUserId(1L);

    // タスクが存在しないことを検証
    assertTrue(foundTasks.isEmpty());

    // taskRepositoryのfindByUserAndDeleteFlagが1回呼ばれたことを検証
    verify(taskRepository, times(1)).findByUserAndDeleteFlag(any(Users.class), anyInt());
  }
}
