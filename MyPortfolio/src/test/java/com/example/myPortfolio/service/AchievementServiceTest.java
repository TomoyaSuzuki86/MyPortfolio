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

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.repository.AchievementsRepository;
import com.example.myPortfolio.repository.TasksRepository;

class AchievementsServiceTest extends AchievementsService {

  // モックにするリポジトリ
  @Mock
  private AchievementsRepository achievementsRepository;

  @Mock
  private TasksRepository tasksRepository;

  // テスト対象のAchievementsService
  @InjectMocks
  private AchievementsService achievementsService;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  /**
   * 実績を正しく作成するテスト。
   * <p>
   * モックの {@code Achievements}
   * オブジェクトを使用して、{@link AchievementsService#createAchievements(Achievements)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モック実績の生成</li>
   * <li>{@code achievementsRepository.save()} が呼ばれたとき、モック実績を返すことを確認</li>
   * <li>実績が正しく作成されたことを検証</li>
   * <li>{@code achievementsRepository.save()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testCreateAchievements_Success() {
    // テスト用のモック実績を作成
    Achievements mockAchievements = new Achievements(null, null, "Completed tasks", 120, 0, null, null);

    // achievementsRepository.save() メソッドが呼ばれたときにモック実績を返すように設定
    when(achievementsRepository.save(any(Achievements.class))).thenReturn(mockAchievements);

    // AchievementsServiceのcreateAchievementsメソッドを実行
    Achievements createdAchievements = achievementsService.createAchievements(mockAchievements);

    // 実績が正しく作成されたことを検証
    assertNotNull(createdAchievements);
    assertEquals("Completed tasks", createdAchievements.getDescription());

    // achievementsRepositoryのsaveが1回呼ばれたことを検証
    verify(achievementsRepository, times(1)).save(any(Achievements.class));
  }

  /**
   * タスクIDから実績を検索し、該当する実績が存在する場合のテスト。
   * <p>
   * モックの {@code Tasks} オブジェクトと {@code Achievements} オブジェクトを使用して、
   * {@link AchievementsService#findByTasksId(Long)} メソッドが正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックタスクと実績リストの生成</li>
   * <li>{@code tasksRepository.findById()} が呼ばれたとき、モックタスクを返すことを確認</li>
   * <li>{@code achievementsRepository.findByTasksAndDeleteFlag()}
   * が呼ばれたとき、モック実績を返すことを確認</li>
   * <li>実績が正しく返されたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByTasksId_TasksExists() {
    // テスト用のモックタスクを作成
    Tasks mockTasks = new Tasks(null, null, "Tasks 1", 60, 0, null, null);

    // テスト用のモック実績リストを作成
    List<Achievements> mockAchievementss = new ArrayList<>();
    mockAchievementss.add(new Achievements(null, mockTasks, "First achievements", 60, 0, null, null));
    mockAchievementss.add(new Achievements(null, mockTasks, "Second achievements", 90, 0, null, null));

    // tasksRepository.findById() メソッドが呼ばれたときにモックタスクを返すように設定
    when(tasksRepository.findById(anyLong())).thenReturn(Optional.of(mockTasks));

    // achievementsRepository.findByTasksAndDeleteFlag() メソッドが呼ばれたときにモック実績を返すように設定
    when(achievementsRepository.findByTasksAndDeleteFlag(any(Tasks.class), anyInt())).thenReturn(mockAchievementss);

    // AchievementsServiceのfindByTasksIdメソッドを実行
    List<Achievements> foundAchievementss = achievementsService.findByTasksId(1L);

    // 実績が正しく返されたことを検証
    assertFalse(foundAchievementss.isEmpty());
    assertEquals(2, foundAchievementss.size());
    assertEquals("First achievements", foundAchievementss.get(0).getDescription());

    // achievementsRepositoryのfindByTasksAndDeleteFlagが1回呼ばれたことを検証
    verify(achievementsRepository, times(1)).findByTasksAndDeleteFlag(any(Tasks.class), anyInt());
  }

  /**
   * タスクIDから実績を検索し、該当する実績が存在しない場合のテスト。
   * <p>
   * {@link AchievementsService#findByTasksId(Long)} メソッドが空のリストを返す場合の動作を検証します。
   * 
   * <ul>
   * <li>{@code tasksRepository.findById()} が呼ばれたときにモックタスクを返すことを確認</li>
   * <li>{@code achievementsRepository.findByTasksAndDeleteFlag()}
   * が呼ばれたときに空のリストを返すことを確認</li>
   * <li>実績が存在しないことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByTasksId_NoAchievementss() {
    // テスト用のモックタスクを作成
    Tasks mockTasks = new Tasks(null, null, "Tasks 1", 60, 0, null, null);

    // tasksRepository.findById() メソッドが呼ばれたときにモックタスクを返すように設定
    when(tasksRepository.findById(anyLong())).thenReturn(Optional.of(mockTasks));

    // achievementsRepository.findByTasksAndDeleteFlag() メソッドが呼ばれたときに空のリストを返すように設定
    when(achievementsRepository.findByTasksAndDeleteFlag(any(Tasks.class), anyInt())).thenReturn(new ArrayList<>());

    // AchievementsServiceのfindByTasksIdメソッドを実行
    List<Achievements> foundAchievementss = achievementsService.findByTasksId(1L);

    // 実績が存在しないことを検証
    assertTrue(foundAchievementss.isEmpty());

    // achievementsRepositoryのfindByTasksAndDeleteFlagが1回呼ばれたことを検証
    verify(achievementsRepository, times(1)).findByTasksAndDeleteFlag(any(Tasks.class), anyInt());
  }
}
