package com.example.myPortfolio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.repository.UsersRepository;

class UsersServiceTest extends UsersService {

  @Mock
  private UsersRepository usersRepository;

  @InjectMocks
  private UsersService usersService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * ユーザーを正しく作成するテスト。
   * <p>
   * モックの {@code Users} オブジェクトを使用して、{@link UsersService#createUsers(Users)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックユーザーの生成</li>
   * <li>{@code usersRepository.save()} が呼ばれたとき、モックユーザーを返すことを確認</li>
   * <li>ユーザーが正しく作成されたことを検証</li>
   * <li>{@code usersRepository.save()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testCreateUsers_Success() {
    // テスト用のモックユーザーを作成
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // usersRepository.save() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.save(any(Users.class))).thenReturn(mockUsers);

    // UsersServiceのcreateUsersメソッドを実行
    Users createdUsers = usersService.createUsers(mockUsers);

    // ユーザーが正しく作成されたことを検証
    assertNotNull(createdUsers);
    assertEquals("test@example.com", createdUsers.getEmail());

    // usersRepositoryのsaveが1回呼ばれたことを検証
    verify(usersRepository, times(1)).save(any(Users.class));
  }

  /**
   * メールアドレスからユーザーを検索し、該当するユーザーが存在する場合のテスト。
   * <p>
   * モックの {@code Users} オブジェクトを使用して、{@link UsersService#findByEmail(String)} メソッドが
   * 正しく動作するかを検証します。
   * 
   * <ul>
   * <li>モックユーザーの生成</li>
   * <li>{@code usersRepository.findByEmail()} が呼ばれたとき、モックユーザーを返すことを確認</li>
   * <li>ユーザーが存在することを検証</li>
   * <li>{@code usersRepository.findByEmail()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByEmail_UsersExists() {
    // テスト用のモックユーザーを作成
    Users mockUsers = new Users(null, "test@example.com", "password", null, null);

    // usersRepository.findByEmail() メソッドが呼ばれたときにモックユーザーを返すように設定
    when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUsers));

    // UsersServiceのfindByEmailメソッドを実行
    Optional<Users> foundUsers = usersService.findByEmail("test@example.com");

    // ユーザーが存在することを検証
    assertTrue(foundUsers.isPresent());
    assertEquals("test@example.com", foundUsers.get().getEmail());

    // usersRepositoryのfindByEmailが1回呼ばれたことを検証
    verify(usersRepository, times(1)).findByEmail(anyString());
  }

  /**
   * メールアドレスからユーザーを検索し、該当するユーザーが存在しない場合のテスト。
   * <p>
   * {@link UsersService#findByEmail(String)} メソッドが空の結果を返す場合の動作を検証します。
   * 
   * <ul>
   * <li>{@code usersRepository.findByEmail()} が呼ばれたときに空の結果を返すことを確認</li>
   * <li>ユーザーが存在しないことを検証</li>
   * <li>{@code usersRepository.findByEmail()} が1回呼ばれたことを検証</li>
   * </ul>
   */
  @Test
  public void testFindByEmail_UsersNotFound() {
    // usersRepository.findByEmail() メソッドが呼ばれたときに空の結果を返すように設定
    when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    // UsersServiceのfindByEmailメソッドを実行
    Optional<Users> foundUsers = usersService.findByEmail("nonexistent@example.com");

    // ユーザーが存在しないことを検証
    assertFalse(foundUsers.isPresent());

    // usersRepositoryのfindByEmailが1回呼ばれたことを検証
    verify(usersRepository, times(1)).findByEmail(anyString());
  }
}