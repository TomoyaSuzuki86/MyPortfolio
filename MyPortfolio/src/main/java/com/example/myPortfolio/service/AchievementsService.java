package com.example.myPortfolio.service;

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
   * タスクIDに紐づくすべての実績を取得し、実績時間の合計を算出する
   */
  public int calcSumAchievementTime(long tasksId) {
    List<Achievements> achievementsList = this.findByTasksId(tasksId);
    if (CollectionUtils.isEmpty(achievementsList)) {
      return 0;
    }

    final int sumAchievementTime = achievementsList.stream().map(Achievements::getActualTime).reduce(0, (a, b) -> a + b);

    return sumAchievementTime;
  }
}
