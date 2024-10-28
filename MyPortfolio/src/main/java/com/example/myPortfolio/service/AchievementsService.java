package com.example.myPortfolio.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.repository.AchievementsRepository;

@Service
public class AchievementsService {

  @Autowired
  private AchievementsRepository achievementsRepository;

  /**
   * 指定されたタスクIDに紐づく全実績を取得
   * 
   * @param taskId タスクID
   * @return タスクに紐づく実績リスト
   */
  public List<Achievements> findByTasksId(Long taskId) {
    return achievementsRepository.findByTasksIdAndDeleteFlag(taskId, 0);
  }

  /**
   * 指定されたタスクIDに紐づく今日日付の全実績を取得
   * 
   * @param taskId タスクID
   * @return 今日の日付に紐づく実績リスト
   */
  public List<Achievements> findByTodayTasksId(Long taskId) {
    LocalDate today = LocalDate.now();
    Date startOfDay = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
    Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

    return achievementsRepository.findByTasksIdAndCreatedAtBetween(taskId, startOfDay, endOfDay);
  }

  /**
   * 実績を新規作成する
   * 
   * @param tasks       タスク情報
   * @param description 実績内容
   * @param actualTime  実績時間
   * @return 作成された実績
   */
  public Achievements createAchievement(Tasks tasks, String description, int actualTime) {
    Achievements achievement = new Achievements(tasks, description, actualTime);
    return achievementsRepository.save(achievement);
  }

  /**
   * 実績IDで実績を検索する
   * 
   * @param achievementId 実績ID
   * @return 該当する実績（存在しない場合は空）
   */
  public Optional<Achievements> findById(Long achievementId) {
    return achievementsRepository.findById(achievementId);
  }

  /**
   * 実績の削除フラグを設定する
   * 
   * @param achievement 削除する実績
   */
  public void deleteAchievement(Achievements achievement) {
    achievement.withDeleteFlag(1);
    achievementsRepository.save(achievement);
  }

  /**
   * タスクIDに紐づくすべての実績を削除する
   * 
   * @param tasksId タスクID
   */
  public void deleteAchievementFromTaskId(Long tasksId) {
    List<Achievements> achievementsList = this.findByTasksId(tasksId);
    if (CollectionUtils.isEmpty(achievementsList)) {
      return;
    }
    for (Achievements achievements : achievementsList) {
      this.deleteAchievement(achievements);
    }
  }

  /**
   * タスクIDに紐づくすべての今日日付の実績を取得し、実績時間の合計を算出する
   */
  public int calcTodaySumAchievementTime(long tasksId) {
    List<Achievements> achievementsList = this.findByTodayTasksId(tasksId);

    if (CollectionUtils.isEmpty(achievementsList)) {
      return 0;
    }

    return achievementsList.stream().mapToInt(Achievements::getActualTime).sum();
  }

}
