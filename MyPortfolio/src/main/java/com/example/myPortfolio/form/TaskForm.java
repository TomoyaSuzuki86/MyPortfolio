package com.example.myPortfolio.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskForm {

  private long taskId; 
  private String taskName;
  private int targetTime;
  private int actualTime;
  private int achievementRate; // 達成率を計算して設定する
}