package com.example.myPortfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

  /**
   * タスク名
   */
  private String taskName;

  /**
   * 目標時間
   */
  private int targetTime;

  /**
   * 実績時間
   */
  private int actualTime;

  /**
   * 進捗状況（達成率）
   */
  private String achievementRate;
}