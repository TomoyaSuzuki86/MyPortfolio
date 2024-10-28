package com.example.myPortfolio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

class AchievementsServiceTest {

  @Mock
  private AchievementsRepository achievementsRepository;

  @InjectMocks
  private AchievementsService achievementsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * タスクIDに紐づく実績を取得するテスト。
   */
  @Test
  void testFindByTasksId_Success() {
    // テスト用のモック実績リストを作成
    List<Achievements> mockAchievementsList = new ArrayList<>();
    mockAchievementsList.add(new Achievements(null, "Description 1", 30));
    mockAchievementsList.add(new Achievements(null, "Description 2", 60));

    // 実績リポジトリがタスクIDに紐づく実績を返すよう設定
    when(achievementsRepository.findByTasksIdAndDeleteFlag(1L, 0)).thenReturn(mockAchievementsList);

    List<Achievements> result = achievementsService.findByTasksId(1L);

    // 実績の取得結果を検証
    assertEquals(2, result.size());
    assertEquals("Description 1", result.get(0).getDescription());
    assertEquals(30, result.get(0).getActualTime());
    verify(achievementsRepository, times(1)).findByTasksIdAndDeleteFlag(1L, 0);
  }

  /**
   * 今日日付のタスクIDに紐づく実績を取得するテスト。
   */
  @Test
  void testFindByTodayTasksId_Success() {
    List<Achievements> mockAchievementsList = new ArrayList<>();
    mockAchievementsList.add(new Achievements(null, "Today Achievement 1", 30));
    mockAchievementsList.add(new Achievements(null, "Today Achievement 2", 45));

    LocalDate today = LocalDate.now();
    Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    when(achievementsRepository.findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay))
        .thenReturn(mockAchievementsList);

    List<Achievements> result = achievementsService.findByTodayTasksId(1L);

    assertEquals(2, result.size());
    assertEquals("Today Achievement 1", result.get(0).getDescription());
    assertEquals(30, result.get(0).getActualTime());
    verify(achievementsRepository, times(1)).findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay);
  }

  /**
   * 実績を新規作成するテスト。
   */
  @Test
  void testCreateAchievement_Success() {
    Tasks mockTask = new Tasks();
    mockTask.setTaskName("Task 1");

    Achievements mockAchievement = new Achievements(mockTask, "New Achievement", 120);

    when(achievementsRepository.save(any(Achievements.class))).thenReturn(mockAchievement);

    Achievements result = achievementsService.createAchievement(mockTask, "New Achievement", 120);

    assertEquals("New Achievement", result.getDescription());
    assertEquals(120, result.getActualTime());
    verify(achievementsRepository, times(1)).save(any(Achievements.class));
  }

  /**
   * 実績IDで実績を検索するテスト。
   */
  @Test
  void testFindById_Success() {
    Achievements mockAchievement = new Achievements(null, "Sample Achievement", 60);
    when(achievementsRepository.findById(1L)).thenReturn(Optional.of(mockAchievement));

    Optional<Achievements> result = achievementsService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals("Sample Achievement", result.get().getDescription());
    verify(achievementsRepository, times(1)).findById(1L);
  }

  /**
   * 存在しない実績IDで検索するテスト。
   */
  @Test
  void testFindById_NotFound() {
    when(achievementsRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<Achievements> result = achievementsService.findById(999L);

    assertFalse(result.isPresent());
    verify(achievementsRepository, times(1)).findById(999L);
  }

  /**
   * 実績の削除フラグを設定するテスト。
   */
  @Test
  void testDeleteAchievement() {
    Achievements mockAchievement = new Achievements(null, "Sample Achievement", 60);
    mockAchievement.setId(1L);

    achievementsService.deleteAchievement(mockAchievement);

    assertEquals(1, mockAchievement.getDeleteFlag());
    verify(achievementsRepository, times(1)).save(mockAchievement);
  }

  /**
   * 今日日付の実績時間の合計を計算するテスト。
   */
  @Test
  void testCalcTodaySumAchievementTime_Success() {
    List<Achievements> mockAchievementsList = new ArrayList<>();
    mockAchievementsList.add(new Achievements(null, "Task 1", 30));
    mockAchievementsList.add(new Achievements(null, "Task 2", 50));
    mockAchievementsList.add(new Achievements(null, "Task 3", 20));

    LocalDate today = LocalDate.now();
    Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    when(achievementsRepository.findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay))
        .thenReturn(mockAchievementsList);

    int result = achievementsService.calcTodaySumAchievementTime(1L);

    assertEquals(100, result); // 合計時間は100分
    verify(achievementsRepository, times(1)).findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay);
  }

  /**
   * 実績リストが空の場合の今日日付合計時間計算テスト。
   */
  @Test
  void testCalcTodaySumAchievementTime_EmptyList() {
    LocalDate today = LocalDate.now();
    Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    when(achievementsRepository.findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay))
        .thenReturn(new ArrayList<>());

    int result = achievementsService.calcTodaySumAchievementTime(1L);

    assertEquals(0, result);
    verify(achievementsRepository, times(1)).findByTasksIdAndCreatedAtBetween(1L, startOfDay, endOfDay);
  }
}
