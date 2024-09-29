package com.example.myPortfolio.form;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeForm {
  private List<TaskForm> tasksList;
  private String taskName; // タスク名
  private int targetTime; // 目標時間
  private String description; // 実績内容
  private int actualTime; // 実績時間
  
  // デフォルトコンストラクタ
  public HomeForm() {
    this.tasksList = new ArrayList<TaskForm>();
  }
  
  // 引数ありコンストラクタ
  public HomeForm(List<TaskForm> tasksList) {
    this.tasksList = tasksList;
  }
  
  public List<TaskForm> getTasksList() {
    if(CollectionUtils.isEmpty(this.tasksList)) {
      return new ArrayList<TaskForm>();
    }
    return this.tasksList;
  }
}