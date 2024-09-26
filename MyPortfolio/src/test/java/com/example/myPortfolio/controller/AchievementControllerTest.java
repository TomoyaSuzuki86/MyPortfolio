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

import com.example.myPortfolio.entity.Achievement;
import com.example.myPortfolio.service.AchievementService;

class AchievementControllerTest extends AchievementController {

  // モックにするAchievementServiceクラス
  @Mock
  private AchievementService achievementService;

  // テスト対象のAchievementControllerクラス
  @InjectMocks
  private AchievementController achievementController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateAchievement() {
    // モックの振る舞いを設定
    Achievement mockAchievement = new Achievement(null, null, "Completed task", 120, 0, null, null);

    // サービスのcreateAchievementメソッドが呼ばれたとき、mockAchievementを返す
    when(achievementService.createAchievement(any(Achievement.class))).thenReturn(mockAchievement);

    // コントローラのcreateAchievementメソッドを実行
    ResponseEntity<Achievement> response = achievementController.createAchievement(mockAchievement);

    // 実績が正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockAchievement, response.getBody());

    // サービスのcreateAchievementが1回呼ばれたことを確認
    verify(achievementService, times(1)).createAchievement(any(Achievement.class));
  }

  @Test
  public void testGetAchievementsByTask() {
    // モックの振る舞いを設定
    List<Achievement> mockAchievements = new ArrayList<>();
    Achievement achievement1 = new Achievement(null, null, "First task completed", 60, 0, null, null);
    mockAchievements.add(achievement1);

    Achievement achievement2 = new Achievement(null, null, "Second task completed", 90, 0, null, null);
    mockAchievements.add(achievement2);

    // サービスのgetAchievementsByTaskメソッドが呼ばれたとき、mockAchievementsを返す
    when(achievementService.findByTaskId(anyLong())).thenReturn(mockAchievements);

    // コントローラのgetAchievementsByTaskメソッドを実行
    ResponseEntity<List<Achievement>> response = achievementController.getAchievementsByTask(1L);

    // 実績が正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockAchievements, response.getBody());

    // サービスのfindByTaskが1回呼ばれたことを確認
    verify(achievementService, times(1)).findByTaskId(anyLong());
  }

  @Test
  public void testGetAchievementsByTask_NotFound() {
    // サービスのfindByTaskメソッドが呼ばれたとき、空のリストを返す
    when(achievementService.findByTaskId(anyLong())).thenReturn(new ArrayList<>());

    // コントローラのgetAchievementsByTaskメソッドを実行
    ResponseEntity<List<Achievement>> response = achievementController.getAchievementsByTask(1L);

    // 実績が見つからない場合、200 OKと空のリストを返す
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(0, response.getBody().size());

    // サービスのfindByTaskが1回呼ばれたことを確認
    verify(achievementService, times(1)).findByTaskId(anyLong());
  }
}
