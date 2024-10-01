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
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 新しいユーザーを登録するテスト。
     * {@code usersRepository.save()} が呼ばれたとき、登録されたユーザーを返すか確認。
     */
    @Test
    void testCreateUsers_Success() {
        // テスト用のユーザーオブジェクト
        Users mockUser = new Users();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        // usersRepository.save() が呼ばれたとき、モックユーザーを返すように設定
        when(usersRepository.save(any(Users.class))).thenReturn(mockUser);

        // UsersService の createUsers メソッドを実行
        Users result = usersService.createUsers(mockUser);

        // 結果を検証
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    /**
     * メールアドレスとパスワードでユーザーを認証するテスト。
     * 正しいメールアドレスとパスワードを指定したとき、ユーザー情報が返されるか確認。
     */
    @Test
    void testAuthenticateUser_Success() {
        Users mockUser = new Users();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");

        // usersRepository.findByEmailAndPassword() が呼ばれたとき、モックユーザーを返すように設定
        when(usersRepository.findByEmailAndPassword("test@example.com", "password")).thenReturn(Optional.of(mockUser));

        Optional<Users> result = usersService.authenticateUser("test@example.com", "password");

        // 結果を検証
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        verify(usersRepository, times(1)).findByEmailAndPassword("test@example.com", "password");
    }

    /**
     * 誤ったメールアドレスまたはパスワードを指定したとき、空の結果が返されるかを確認するテスト。
     */
    @Test
    void testAuthenticateUser_Failure() {
        when(usersRepository.findByEmailAndPassword(anyString(), anyString())).thenReturn(Optional.empty());

        Optional<Users> result = usersService.authenticateUser("wrong@example.com", "wrongpassword");

        // 認証が失敗することを確認
        assertFalse(result.isPresent());
        verify(usersRepository, times(1)).findByEmailAndPassword("wrong@example.com", "wrongpassword");
    }

    /**
     * ユーザーIDでユーザーを検索するテスト。
     * 存在するユーザーIDを指定したとき、ユーザー情報が返されるか確認。
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
     * メールアドレスでユーザーを検索するテスト。
     * 存在するメールアドレスを指定したとき、ユーザー情報が返されるか確認。
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
