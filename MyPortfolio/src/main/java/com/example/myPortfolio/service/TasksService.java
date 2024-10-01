package com.example.myPortfolio.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.exception.InvalidUserException;
import com.example.myPortfolio.exception.TaskNotFoundException;
import com.example.myPortfolio.exception.UserNotFoundException;
import com.example.myPortfolio.form.TaskForm;
import com.example.myPortfolio.mapper.TaskMapper;
import com.example.myPortfolio.repository.TasksRepository;

@Service
public class TasksService {

  @Autowired
  private UsersService usersService;

  @Autowired
  private SessionService sessionService;
  
  @Autowired
  private TasksRepository tasksRepository;
  
  @Autowired
  private TaskMapper taskMapper;

  /**
   * 指定ユーザーの全タスクを取得
   * 
   * @param usersId ユーザーID
   * @return ユーザーに紐づくタスクリスト
   */
  public List<Tasks> findByUserId(Long usersId) {
    return tasksRepository.findByUsersIdAndDeleteFlag(usersId, 0);
  }

  /**
   * 指定ユーザーと指定タスクIDに紐づくタスクを取得
   * 
   * @param usersId ユーザーID
   * @param taskId  タスクID
   * @return ユーザーとタスクIDに紐づくタスク（Optional型）
   */
  public Optional<Tasks> findByIdAndUsersId(Long usersId, Long taskId) {
    return tasksRepository.findByIdAndUsersId(usersId, taskId);
  }

  /**
   * タスクIDでタスクを検索する
   * 
   * @param taskId タスクID
   * @return 該当するタスク（存在しない場合は空）
   */
  public Optional<Tasks> findById(Long taskId) {
    return tasksRepository.findById(taskId);
  }

  /**
   * ユーザーに紐づくタスクを登録または更新するメソッド。
   * <p>
   * タスクIDが指定されていない場合は新規作成を行い、指定されている場合は編集処理を実施します。
   * ユーザーがログインしていない、または不正なユーザー・タスクIDが渡された場合は例外をスローします。
   * </p>
   *
   * @param taskId     タスクID (既存のタスクを編集する場合のみ指定、未指定の場合は新規作成)
   * @param taskName   編集または新規作成するタスクの名前（必須）
   * @param targetTime タスクの目標時間（分単位）
   * @return 更新または新規登録された `Tasks` エンティティのオブジェクト
   * @throws Exception ログイン中のユーザー情報が見つからない、または不正なユーザーID・タスクIDが渡された場合に発生
   */
  public Tasks saveOrUpdateTask(Long taskId, String taskName, int targetTime) throws Exception {

    // ログイン中のユーザーIDを取得
    Long userId = sessionService.getLoggedInUserId()
        .orElseThrow(() -> new InvalidUserException("セッションにユーザー情報が見つかりません"));

    // ユーザー情報の検証
    Users user = usersService.findById(userId).orElseThrow(() -> new UserNotFoundException("不正なユーザーIDが設定されています"));

    Tasks task = null;

    // タスクIDが指定されている場合は編集処理
    if (taskId != null) {
      // 自身のタスクかどうか確認し、該当タスクを取得
      task = this.findByIdAndUsersId(userId, taskId).orElseThrow(() -> new TaskNotFoundException("不正なタスクIDが設定されています"));
      task.setTaskName(taskName);
      task.setTargetTime(targetTime);
      this.updateTask(task);
    } else {
      // タスクIDが指定されていない場合は新規作成
      task = this.createTask(user, taskName, targetTime); // タスクの登録
    }

    // タスクをデータベースに保存
    return task;
  }
  
  /**
   * ユーザーのタスクをフォーム形式で取得
   * 
   * @param userId ユーザーID
   * @return タスクフォームリスト
   */
  public List<TaskForm> getTaskFormsByUserId(Long userId) {
      // ユーザーIDに紐づくタスクを取得し、フォーム形式に変換
      List<Tasks> tasksList = findByUserId(userId);
      return tasksList.stream()
              .map(taskMapper::toForm)  // Mapper でエンティティをフォームに変換
              .collect(Collectors.toList());
  }

  /**
   * 新しいタスクを作成する
   * 
   * @param users      ユーザー情報
   * @param taskName   タスク名
   * @param targetTime 目標時間
   * @return 作成されたタスク
   */
  private Tasks createTask(Users users, String taskName, int targetTime) {
    Tasks task = new Tasks(users, taskName, targetTime);
    return tasksRepository.save(task);
  }

  /**
   * タスクを更新するメソッド
   *
   * @param task 更新対象のタスクオブジェクト
   * @return 更新されたタスクオブジェクト
   */
  private Tasks updateTask(Tasks task) {
    return tasksRepository.save(task); // 受け取ったタスクを保存し、更新を行う
  }

  /**
   * タスクの削除フラグを設定する
   * 
   * @param task 削除するタスク
   */
  public void deleteTask(Tasks task) {
    task.withDeleteFlag(1);
    tasksRepository.save(task);
  }

}
