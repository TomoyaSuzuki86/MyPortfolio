package com.example.myPortfolio.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achievements {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "tasks_id", nullable = false)
  private Tasks tasks;

  @Column(length = 50)
  private String description;

  @Column(nullable = false)
  private int actualTime;

  @Column(nullable = false)
  private int deleteFlag;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  public Achievements() {
    // デフォルトコンストラクタ
  }

  public Achievements(Tasks tasks, String description, int actualTime) {
    this.tasks = tasks;
    this.description = description;
    this.actualTime = actualTime;
    this.deleteFlag = 0;
  }

  public Achievements withDeleteFlag(int deleteFlag) {
    this.deleteFlag = deleteFlag;
    return this;
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
