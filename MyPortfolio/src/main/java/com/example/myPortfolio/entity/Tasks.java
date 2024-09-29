package com.example.myPortfolio.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tasks {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "users_id", nullable = false)
  private Users users;

  @Column(nullable = false, length = 50)
  private String taskName;

  @Column(nullable = false)
  private int targetTime;

  @Column(nullable = false)
  private int actualTime;

  @Column(nullable = false)
  private int deleteFlag;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @OneToMany(mappedBy = "tasks", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Achievements> achievementsList = new ArrayList<>();

  public Tasks() {
    // デフォルトコンストラクタ
  }

  public Tasks(Users users, String taskName, int targetTime) {
    this.users = users;
    this.taskName = taskName;
    this.targetTime = targetTime;
    this.actualTime = 0; // 初期実績時間は0
    this.deleteFlag = 0;
  }
  
  // 削除フラグを設定するメソッド (Builder Pattern)
  public Tasks withDeleteFlag(int deleteFlag) {
    this.deleteFlag = deleteFlag;
    return this; // 自身のインスタンスを返す
  }
  
  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
    updatedAt = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = new Date();
  }
}
