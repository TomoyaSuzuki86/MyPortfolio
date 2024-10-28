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
import com.example.myPortfolio.exception.DuplicateEmailException;
import com.example.myPortfolio.exception.InvalidPasswordException;
import com.example.myPortfolio.exception.InvalidUserException;
import com.example.myPortfolio.repository.UsersRepository;

class UsersServiceTest {

  @Mock
  private UsersRepository usersRepository;

  @InjectMocks
  private UsersService usersService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  /**
   * 新しいユーザーを登録するテスト。 重複するメールアドレスの場合、DuplicateEmailException が発生することを確認。
   */
  @Test
  void testCreateUsers_DuplicateEmail() {
    Users mockUser = new Users();
    mockUser.setEmail("test@example.com");
    mockUser.setPassword("password");

    when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

    assertThrows(DuplicateEmailException.class, () -> {
      usersService.createUsers(mockUser);
    });

    verify(usersRepository, times(1)).findByEmail("test@example.com");
    verify(usersRepository, times(0)).save(any(Users.class));
  }

  /**
   * メールアドレスとパスワードでユーザーを認証するテスト。 正しいメールアドレスとパスワードを指定したとき、ユーザー情報が返されるか確認。
   */
  @Test
  void testAuthenticateUser_Success() {
    Users mockUser = new Users();
    mockUser.setEmail("test@example.com");
    mockUser.setPassword("password");

    when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

    Users result = usersService.authenticateUser("test@example.com", "password");

    assertNotNull(result);
    assertEquals("test@example.com", result.getEmail());
    verify(usersRepository, times(1)).findByEmail("test@example.com");
  }

  /**
   * 誤ったパスワードを指定したとき、InvalidPasswordException が発生することを確認するテスト。
   */
  @Test
  void testAuthenticateUser_InvalidPassword() {
    Users mockUser = new Users();
    mockUser.setEmail("test@example.com");
    mockUser.setPassword("correct_password");

    when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

    assertThrows(InvalidPasswordException.class, () -> {
      usersService.authenticateUser("test@example.com", "wrong_password");
    });

    verify(usersRepository, times(1)).findByEmail("test@example.com");
  }

  /**
   * 存在しないメールアドレスを指定したとき、InvalidUserException が発生することを確認するテスト。
   */
  @Test
  void testAuthenticateUser_InvalidUser() {
    when(usersRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    assertThrows(InvalidUserException.class, () -> {
      usersService.authenticateUser("notfound@example.com", "password");
    });

    verify(usersRepository, times(1)).findByEmail("notfound@example.com");
  }

  /**
   * ユーザーIDでユーザーを検索するテスト。 存在するユーザーIDを指定したとき、ユーザー情報が返されるか確認。
   */
  @Test
  void testFindById_Success() {
    Users mockUser = new Users();
    mockUser.setId(1L);
    mockUser.setEmail("test@example.com");

    when(usersRepository.findById(1L)).thenReturn(Optional.of(mockUser));

    Optional<Users> result = usersService.findById(1L);

    assertTrue(result.isPresent());
    assertEquals(1L, result.get().getId());
    verify(usersRepository, times(1)).findById(1L);
  }

  /**
   * 存在しないユーザーIDを指定したとき、空の結果が返されるか確認するテスト。
   */
  @Test
  void testFindById_NotFound() {
    when(usersRepository.findById(999L)).thenReturn(Optional.empty());

    Optional<Users> result = usersService.findById(999L);

    assertFalse(result.isPresent());
    verify(usersRepository, times(1)).findById(999L);
  }

  /**
   * メールアドレスでユーザーを検索するテスト。 存在するメールアドレスを指定したとき、ユーザー情報が返されるか確認。
   */
  @Test
  void testFindByEmail_Success() {
    Users mockUser = new Users();
    mockUser.setEmail("test@example.com");

    when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

    Optional<Users> result = usersService.findByEmail("test@example.com");

    assertTrue(result.isPresent());
    assertEquals("test@example.com", result.get().getEmail());
    verify(usersRepository, times(1)).findByEmail("test@example.com");
  }

  /**
   * 存在しないメールアドレスを指定したとき、空の結果が返されるか確認するテスト。
   */
  @Test
  void testFindByEmail_NotFound() {
    when(usersRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

    Optional<Users> result = usersService.findByEmail("notfound@example.com");

    assertFalse(result.isPresent());
    verify(usersRepository, times(1)).findByEmail("notfound@example.com");
  }
}
