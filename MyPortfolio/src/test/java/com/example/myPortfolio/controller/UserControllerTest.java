package com.example.myPortfolio.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.myPortfolio.entity.User;
import com.example.myPortfolio.service.UserService;

class UserControllerTest extends UserController {

  // モックにするUserServiceクラス
  @Mock
  private UserService userService;

  // テスト対象のUserControllerクラス
  @InjectMocks
  private UserController userController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRegisterUser() {
    // モックの振る舞いを設定
    User mockUser = new User(null, "test@example.com", "password", null, null);

    // サービスのsaveメソッドが呼ばれたとき、mockUserを返す
    when(userService.createUser(any(User.class))).thenReturn(mockUser);

    // コントローラのregisterUserメソッドを実行
    ResponseEntity<User> response = userController.registerUser(mockUser);

    // ユーザーが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockUser, response.getBody());

    // サービスのcreateUserが1回呼ばれたことを確認
    verify(userService, times(1)).createUser(any(User.class));
  }

  @Test
  public void testGetUserByEmail() {
    // モックの振る舞いを設定
    User mockUser = new User(null, "test@example.com", "password", null, null);

    // サービスのfindByEmailメソッドが呼ばれたとき、mockUserを返す
    when(userService.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

    // コントローラのgetUserByEmailメソッドを実行
    ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

    // ユーザーが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockUser, response.getBody());

    // サービスのfindByEmailが1回呼ばれたことを確認
    verify(userService, times(1)).findByEmail(anyString());
  }

  @Test
  public void testGetUserByEmail_NotFound() {
    // サービスのfindByEmailメソッドが呼ばれたとき、空の結果を返す
    when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

    // コントローラのgetUserByEmailメソッドを実行
    ResponseEntity<User> response = userController.getUserByEmail("nonexistent@example.com");

    // ユーザーが見つからない場合、404 Not Foundを返す
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // 修正箇所

    // サービスのfindByEmailが1回呼ばれたことを確認
    verify(userService, times(1)).findByEmail(anyString());
  }
}