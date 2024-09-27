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

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.TasksRepository;
import com.example.myPortfolio.repository.UsersRepository;

class TasksServiceTest extends TasksService {

  // モックにするリポジトリ
  @Mock
  private TasksRepository tasksRepository;

  @Mock
  private UsersRepository usersRepository;

  // テスト対象のTasksService
  @InjectMocks
  private TasksService tasksService;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  /**
   * タスクを正しく作成するテスト。
   * <p>
   * モックの {@code Tasks} オブジェクトを使用して、{@link TasksService#createTasks(Tasks)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックタスクの生成</li>
   * <li>{@code tasksRepository.save()} が呼ばれたとき、モックタスクを返すことを確認</li>
   * <li>タスクが正しく作成されたことを検証</li>
   * <li>{@code tasksRepository.save()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testCreateTasks_Success() {
    // テスト用のモックタスクを作成
    Tasks mockTasks = new Tasks(null, null, "Sample Tasks", 120, 0, null, null);

    // tasksRepository.save() メソッドが呼ばれたときにモックタスクを返すように設定
    when(tasksRepository.save(any(Tasks.class))).thenReturn(mockTasks);

    // TaskServiceのcreateTasksメソッドを実行
    Tasks createdTasks = tasksService.createTasks(mockTasks);

    // タスクが正しく作成されたことを検証
    assertNotNull(createdTasks);
    assertEquals("Sample Tasks", createdTasks.getTaskName());

    // tasksRepositoryのsaveが1回呼ばれたことを検証
    verify(tasksRepository, times(1)).save(any(Tasks.class));
  }

  /**
   * ユーザーIDからタスクを検索し、該当するタスクが存在する場合のテスト。
   * <p>
   * モックの {@code Tasks} オブジェクトと {@code Users} オブジェクトを使用して、
   * {@link TasksService#findAllByUsersId(Long)} メソッドが正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックユーザーとタスクリストの生成</li>
   * <li>{@code usersRepository.findById()} が呼ばれたとき、モックユーザーを返すことを確認</li>
   * <li>{@code tasksRepository.findByUsersAndDeleteFlag()} が呼ばれたとき、モックタスクを返すことを確認
   * </li>
   * <li>タスクが正しく返されたことを検証</li>
   * <li>{@code tasksRepository.findByUsersAndDeleteFlag()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindAllByUsersId_UsersExists() {
    // テスト用のモックユーザーを作成
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // テスト用のモックタスクリストを作成
    List<Tasks> mockTasks = new ArrayList<>();
    mockTasks.add(new Tasks(null, mockUsers, "Tasks 1", 60, 0, null, null));
    mockTasks.add(new Tasks(null, mockUsers, "Tasks 2", 90, 0, null, null));

    // usersRepository.findById() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(mockUsers));

    // tasksRepository.findByUsersAndDeleteFlag() メソッドが呼ばれたときにモックタスクを返すように設定
    when(tasksRepository.findByUsersAndDeleteFlag(any(Users.class), anyInt())).thenReturn(mockTasks);

    // TasksServiceのfindAllByUsersIdメソッドを実行
    List<Tasks> foundTasks = tasksService.findAllByUsersId(1L);

    // タスクが正しく返されたことを検証
    assertFalse(foundTasks.isEmpty());
    assertEquals(2, foundTasks.size());
    assertEquals("Tasks 1", foundTasks.get(0).getTaskName());

    // tasksRepositoryのfindByUsersAndDeleteFlagが1回呼ばれたことを検証
    verify(tasksRepository, times(1)).findByUsersAndDeleteFlag(any(Users.class), anyInt());
  }

  /**
   * ユーザーIDからタスクを検索し、該当するタスクが存在しない場合のテスト。
   * <p>
   * {@link TasksService#findAllByUsersId(Long)} メソッドが空のリストを返す場合の動作を検証します。
   * 
   * <ul>
   * <li>{@code usersRepository.findById()} が呼ばれたときにモックユーザーを返すことを確認</li>
   * <li>{@code tasksRepository.findByUsersAndDeleteFlag()} が呼ばれたときに空のリストを返すことを確認
   * </li>
   * <li>タスクが存在しないことを検証</li>
   * </ul>
   */
  @Test
  public void testFindAllByUsersId_NoTasks() {
    // テスト用のモックユーザーを作成
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // usersRepository.findById() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.findById(anyLong())).thenReturn(Optional.of(mockUsers));

    // tasksRepository.findByUsersAndDeleteFlag() メソッドが呼ばれたときに空のリストを返すように設定
    when(tasksRepository.findByUsersAndDeleteFlag(any(Users.class), anyInt())).thenReturn(new ArrayList<>());

    // TasksServiceのfindAllByUsersIdメソッドを実行
    List<Tasks> foundTasks = tasksService.findAllByUsersId(1L);

    // タスクが存在しないことを検証
    assertTrue(foundTasks.isEmpty());

    // tasksRepositoryのfindByUsersAndDeleteFlagが1回呼ばれたことを検証
    verify(tasksRepository, times(1)).findByUsersAndDeleteFlag(any(Users.class), anyInt());
  }
}
