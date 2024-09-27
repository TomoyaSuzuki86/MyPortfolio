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

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.service.AchievementsService;

class AchievementsControllerTest extends AchievementsController {

  // モックにするAchievementsServiceクラス
  @Mock
  private AchievementsService achievementsService;

  // テスト対象のAchievementsControllerクラス
  @InjectMocks
  private AchievementsController achievementsController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateAchievements() {
    // モックの振る舞いを設定
    Achievements mockAchievements = new Achievements(null, null, "Completed tasks", 120, 0, null, null);

    // サービスのcreateAchievementsメソッドが呼ばれたとき、mockAchievementsを返す
    when(achievementsService.createAchievements(any(Achievements.class))).thenReturn(mockAchievements);

    // コントローラのcreateAchievementsメソッドを実行
    ResponseEntity<Achievements> response = achievementsController.createAchievements(mockAchievements);

    // 実績が正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockAchievements, response.getBody());

    // サービスのcreateAchievementsが1回呼ばれたことを確認
    verify(achievementsService, times(1)).createAchievements(any(Achievements.class));
  }

  @Test
  public void testGetAchievementssByTasks() {
    // モックの振る舞いを設定
    List<Achievements> mockAchievementss = new ArrayList<>();
    Achievements achievements1 = new Achievements(null, null, "First tasks completed", 60, 0, null, null);
    mockAchievementss.add(achievements1);

    Achievements achievements2 = new Achievements(null, null, "Second tasks completed", 90, 0, null, null);
    mockAchievementss.add(achievements2);

    // サービスのgetAchievementssByTasksメソッドが呼ばれたとき、mockAchievementssを返す
    when(achievementsService.findByTasksId(anyLong())).thenReturn(mockAchievementss);

    // コントローラのgetAchievementssByTasksメソッドを実行
    ResponseEntity<List<Achievements>> response = achievementsController.getAchievementssByTasks(1L);

    // 実績が正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockAchievementss, response.getBody());

    // サービスのfindByTasksIdが1回呼ばれたことを確認
    verify(achievementsService, times(1)).findByTasksId(anyLong());
  }

  @Test
  public void testGetAchievementssByTasks_NotFound() {
    // サービスのfindByTasksIdメソッドが呼ばれたとき、空のリストを返す
    when(achievementsService.findByTasksId(anyLong())).thenReturn(new ArrayList<>());

    // コントローラのgetAchievementssByTasksメソッドを実行
    ResponseEntity<List<Achievements>> response = achievementsController.getAchievementssByTasks(1L);

    // 実績が見つからない場合、200 OKと空のリストを返す
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(0, response.getBody().size());

    // サービスのfindByTasksIdが1回呼ばれたことを確認
    verify(achievementsService, times(1)).findByTasksId(anyLong());
  }
}
