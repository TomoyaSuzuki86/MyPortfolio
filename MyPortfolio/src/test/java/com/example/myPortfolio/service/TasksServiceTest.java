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

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.exception.InvalidUserException;
import com.example.myPortfolio.exception.TaskNotFoundException;
import com.example.myPortfolio.exception.UserNotFoundException;
import com.example.myPortfolio.form.TaskForm;
import com.example.myPortfolio.mapper.TaskMapper;
import com.example.myPortfolio.repository.TasksRepository;

class TasksServiceTest extends TasksService {

    @Mock
    private UsersService usersService;

    @Mock
    private SessionService sessionService;

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * ユーザーIDに紐づくタスクのリストを取得するテスト。
     * <p>
     * ユーザーIDが正しく渡された場合に、リポジトリからタスクが取得されるかを確認します。
     * </p>
     */
    @Test
    void testFindByUserId_Success() {
        // モックユーザーとタスクリストを設定
        List<Tasks> mockTasks = new ArrayList<>();
        mockTasks.add(new Tasks(null, "Task 1", 60));
        mockTasks.add(new Tasks(null, "Task 2", 90));

        when(tasksRepository.findByUsersIdAndDeleteFlag(1L, 0)).thenReturn(mockTasks);

        List<Tasks> result = tasksService.findByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTaskName());
        verify(tasksRepository, times(1)).findByUsersIdAndDeleteFlag(1L, 0);
    }

    /**
     * 新しいタスクを作成するテスト。
     * <p>
     * セッションから取得したユーザーIDに対して新規タスクを作成できるかを検証します。
     * </p>
     */
    @Test
    void testSaveOrUpdateTask_CreateNewTask() throws Exception {
        // モックのユーザー設定
        Users mockUser = new Users("test@example.com", "password");

        when(sessionService.getLoggedInUserId()).thenReturn(Optional.of(1L));
        when(usersService.findById(1L)).thenReturn(Optional.of(mockUser));

        Tasks mockTask = new Tasks(mockUser, "New Task", 120);
        when(tasksRepository.save(any(Tasks.class))).thenReturn(mockTask);

        Tasks result = tasksService.saveOrUpdateTask(null, "New Task", 120);

        assertEquals("New Task", result.getTaskName());
        assertEquals(120, result.getTargetTime());
        verify(tasksRepository, times(1)).save(any(Tasks.class));
    }

    /**
     * 既存のタスクを更新するテスト。
     * <p>
     * セッションから取得したユーザーIDに対して既存タスクを正しく更新できるかを検証します。
     * </p>
     */
    @Test
    void testSaveOrUpdateTask_UpdateExistingTask() throws Exception {
        Users mockUser = new Users("test@example.com", "password");
        Tasks existingTask = new Tasks(mockUser, "Old Task", 60);
        existingTask.setId(1L);

        when(sessionService.getLoggedInUserId()).thenReturn(Optional.of(1L));
        when(usersService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(tasksRepository.findByIdAndUsersId(1L, 1L)).thenReturn(Optional.of(existingTask));

        existingTask.setTaskName("Updated Task");
        existingTask.setTargetTime(90);

        when(tasksRepository.save(any(Tasks.class))).thenReturn(existingTask);

        Tasks result = tasksService.saveOrUpdateTask(1L, "Updated Task", 90);

        assertEquals("Updated Task", result.getTaskName());
        assertEquals(90, result.getTargetTime());
        verify(tasksRepository, times(1)).save(any(Tasks.class));
    }

    /**
     * ユーザー情報がセッションに見つからない場合の例外処理テスト。
     * <p>
     * セッションにユーザー情報がない場合、`InvalidUserException` がスローされるか確認します。
     * </p>
     */
    @Test
    void testSaveOrUpdateTask_InvalidUserException() {
        when(sessionService.getLoggedInUserId()).thenReturn(Optional.empty());

        InvalidUserException exception = assertThrows(InvalidUserException.class, () -> {
            tasksService.saveOrUpdateTask(null, "Task", 60);
        });

        assertEquals("セッションにユーザー情報が見つかりません", exception.getMessage());
    }

    /**
     * 存在しないユーザーIDの場合の例外処理テスト。
     * <p>
     * `UserNotFoundException` がスローされるか確認します。
     * </p>
     */
    @Test
    void testSaveOrUpdateTask_UserNotFoundException() {
        when(sessionService.getLoggedInUserId()).thenReturn(Optional.of(1L));
        when(usersService.findById(1L)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            tasksService.saveOrUpdateTask(null, "Task", 60);
        });

        assertEquals("不正なユーザーIDが設定されています", exception.getMessage());
    }

    /**
     * 存在しないタスクIDの場合の例外処理テスト。
     * <p>
     * `TaskNotFoundException` がスローされるか確認します。
     * </p>
     */
    @Test
    void testSaveOrUpdateTask_TaskNotFoundException() {
        Users mockUser = new Users("test@example.com", "password");

        when(sessionService.getLoggedInUserId()).thenReturn(Optional.of(1L));
        when(usersService.findById(1L)).thenReturn(Optional.of(mockUser));
        when(tasksRepository.findByIdAndUsersId(1L, 999L)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            tasksService.saveOrUpdateTask(999L, "Task", 60);
        });

        assertEquals("不正なタスクIDが設定されています", exception.getMessage());
    }

    /**
     * ユーザーIDに紐づくタスクフォームリストの取得テスト。
     * <p>
     * タスクエンティティから正しくフォーム形式に変換されるかを確認します。
     * </p>
     */
    @Test
    void testGetTaskFormsByUserId() {
        Users mockUser = new Users("test@example.com", "password");
        List<Tasks> mockTasks = new ArrayList<>();
        mockTasks.add(new Tasks(mockUser, "Task 1", 60));
        mockTasks.add(new Tasks(mockUser, "Task 2", 90));

        TaskForm mockTaskForm1 = new TaskForm();
        mockTaskForm1.setTaskName("Task 1");
        mockTaskForm1.setTargetTime(60);

        TaskForm mockTaskForm2 = new TaskForm();
        mockTaskForm2.setTaskName("Task 2");
        mockTaskForm2.setTargetTime(90);

        when(tasksRepository.findByUsersIdAndDeleteFlag(1L, 0)).thenReturn(mockTasks);
        when(taskMapper.toForm(mockTasks.get(0))).thenReturn(mockTaskForm1);
        when(taskMapper.toForm(mockTasks.get(1))).thenReturn(mockTaskForm2);

        List<TaskForm> result = tasksService.getTaskFormsByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTaskName());
        assertEquals("Task 2", result.get(1).getTaskName());
    }
}
