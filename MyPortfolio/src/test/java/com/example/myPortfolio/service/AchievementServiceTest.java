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

import com.example.myPortfolio.entity.Achievement;
import com.example.myPortfolio.entity.Task;
import com.example.myPortfolio.repository.AchievementRepository;
import com.example.myPortfolio.repository.TaskRepository;

class AchievementServiceTest extends AchievementService {

  // モックにするリポジトリ
  @Mock
  private AchievementRepository achievementRepository;

  @Mock
  private TaskRepository taskRepository;

  // テスト対象のAchievementService
  @InjectMocks
  private AchievementService achievementService;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  /**
   * 実績を正しく作成するテスト。
   * <p>
   * モックの {@code Achievement}
   * オブジェクトを使用して、{@link AchievementService#createAchievement(Achievement)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モック実績の生成</li>
   * <li>{@code achievementRepository.save()} が呼ばれたとき、モック実績を返すことを確認</li>
   * <li>実績が正しく作成されたことを検証</li>
   * <li>{@code achievementRepository.save()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testCreateAchievement_Success() {
    // テスト用のモック実績を作成
    Achievement mockAchievement = new Achievement(null, null, "Completed task", 120, 0, null, null);

    // achievementRepository.save() メソッドが呼ばれたときにモック実績を返すように設定
    when(achievementRepository.save(any(Achievement.class))).thenReturn(mockAchievement);

    // AchievementServiceのcreateAchievementメソッドを実行
    Achievement createdAchievement = achievementService.createAchievement(mockAchievement);

    // 実績が正しく作成されたことを検証
    assertNotNull(createdAchievement);
    assertEquals("Completed task", createdAchievement.getDescription());

    // achievementRepositoryのsaveが1回呼ばれたことを検証
    verify(achievementRepository, times(1)).save(any(Achievement.class));
  }

  /**
   * タスクIDから実績を検索し、該当する実績が存在する場合のテスト。
   * <p>
   * モックの {@code Task} オブジェクトと {@code Achievement} オブジェクトを使用して、
   * {@link AchievementService#findByTaskId(Long)} メソッドが正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックタスクと実績リストの生成</li>
   * <li>{@code taskRepository.findById()} が呼ばれたとき、モックタスクを返すことを確認</li>
   * <li>{@code achievementRepository.findByTaskAndDeleteFlag()}
   * が呼ばれたとき、モック実績を返すことを確認</li>
   * <li>実績が正しく返されたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByTaskId_TaskExists() {
    // テスト用のモックタスクを作成
    Task mockTask = new Task(null, null, "Task 1", 60, 0, null, null);

    // テスト用のモック実績リストを作成
    List<Achievement> mockAchievements = new ArrayList<>();
    mockAchievements.add(new Achievement(null, mockTask, "First achievement", 60, 0, null, null));
    mockAchievements.add(new Achievement(null, mockTask, "Second achievement", 90, 0, null, null));

    // taskRepository.findById() メソッドが呼ばれたときにモックタスクを返すように設定
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTask));

    // achievementRepository.findByTaskAndDeleteFlag() メソッドが呼ばれたときにモック実績を返すように設定
    when(achievementRepository.findByTaskAndDeleteFlag(any(Task.class), anyInt())).thenReturn(mockAchievements);

    // AchievementServiceのfindByTaskIdメソッドを実行
    List<Achievement> foundAchievements = achievementService.findByTaskId(1L);

    // 実績が正しく返されたことを検証
    assertFalse(foundAchievements.isEmpty());
    assertEquals(2, foundAchievements.size());
    assertEquals("First achievement", foundAchievements.get(0).getDescription());

    // achievementRepositoryのfindByTaskAndDeleteFlagが1回呼ばれたことを検証
    verify(achievementRepository, times(1)).findByTaskAndDeleteFlag(any(Task.class), anyInt());
  }

  /**
   * タスクIDから実績を検索し、該当する実績が存在しない場合のテスト。
   * <p>
   * {@link AchievementService#findByTaskId(Long)} メソッドが空のリストを返す場合の動作を検証します。
   * 
   * <ul>
   * <li>{@code taskRepository.findById()} が呼ばれたときにモックタスクを返すことを確認</li>
   * <li>{@code achievementRepository.findByTaskAndDeleteFlag()}
   * が呼ばれたときに空のリストを返すことを確認</li>
   * <li>実績が存在しないことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByTaskId_NoAchievements() {
    // テスト用のモックタスクを作成
    Task mockTask = new Task(null, null, "Task 1", 60, 0, null, null);

    // taskRepository.findById() メソッドが呼ばれたときにモックタスクを返すように設定
    when(taskRepository.findById(anyLong())).thenReturn(Optional.of(mockTask));

    // achievementRepository.findByTaskAndDeleteFlag() メソッドが呼ばれたときに空のリストを返すように設定
    when(achievementRepository.findByTaskAndDeleteFlag(any(Task.class), anyInt())).thenReturn(new ArrayList<>());

    // AchievementServiceのfindByTaskIdメソッドを実行
    List<Achievement> foundAchievements = achievementService.findByTaskId(1L);

    // 実績が存在しないことを検証
    assertTrue(foundAchievements.isEmpty());

    // achievementRepositoryのfindByTaskAndDeleteFlagが1回呼ばれたことを検証
    verify(achievementRepository, times(1)).findByTaskAndDeleteFlag(any(Task.class), anyInt());
  }
}
