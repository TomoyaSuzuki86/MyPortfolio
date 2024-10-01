package com.example.myPortfolio.form;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 実績入力用のフォームクラス
 */
@Getter
@Setter
public class AchievementForm {

    private Long taskId;
    private String taskName;
    private int targetTime; // 目標時間
    private List<AchievementDetail> achievementDetails = new ArrayList<>(); // 作業内容のリスト

    // 作業時間の合計
    private int totalActualTime;

    // 達成率
    private int achievementRate;

    @Getter
    @Setter
    public static class AchievementDetail {
        private String description; // 作業内容
        private int actualTime;     // 実績時間
    }
}
