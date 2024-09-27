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

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.service.UsersService;

class UsersControllerTest extends UsersController {

  // モックにするUsersServiceクラス
  @Mock
  private UsersService usersService;

  // テスト対象のUserControllerクラス
  @InjectMocks
  private UsersController usersController;

  @BeforeEach
  public void setUp() {
    // Mockitoの初期化
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testRegisterUser() {
    // モックの振る舞いを設定
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // サービスのsaveメソッドが呼ばれたとき、mockUserを返す
    when(usersService.createUser(any(Users.class))).thenReturn(mockUsers);

    // コントローラのregisterUsersメソッドを実行
    ResponseEntity<Users> response = usersController.registerUser(mockUsers);

    // ユーザーが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockUsers, response.getBody());

    // サービスのcreateUsersが1回呼ばれたことを確認
    verify(usersService, times(1)).createUser(any(Users.class));
  }

  @Test
  public void testGetUserByEmail() {
    // モックの振る舞いを設定
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // サービスのfindByEmailメソッドが呼ばれたとき、mockUserを返す
    when(usersService.findByEmail(anyString())).thenReturn(Optional.of(mockUsers));

    // コントローラのgetUserByEmailメソッドを実行
    ResponseEntity<Users> response = usersController.getUserByEmail("test@example.com");

    // ユーザーが正しく返されたか確認
    assertEquals(HttpStatus.OK, response.getStatusCode()); // 修正箇所
    assertEquals(mockUsers, response.getBody());

    // サービスのfindByEmailが1回呼ばれたことを確認
    verify(usersService, times(1)).findByEmail(anyString());
  }

  @Test
  public void testGetUserByEmail_NotFound() {
    // サービスのfindByEmailメソッドが呼ばれたとき、空の結果を返す
    when(usersService.findByEmail(anyString())).thenReturn(Optional.empty());

    // コントローラのgetUserByEmailメソッドを実行
    ResponseEntity<Users> response = usersController.getUserByEmail("nonexistent@example.com");

    // ユーザーが見つからない場合、404 Not Foundを返す
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // 修正箇所

    // サービスのfindByEmailが1回呼ばれたことを確認
    verify(usersService, times(1)).findByEmail(anyString());
  }
}