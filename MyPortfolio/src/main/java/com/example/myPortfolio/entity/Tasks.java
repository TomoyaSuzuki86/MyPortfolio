package com.example.myPortfolio.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "tasks", indexes = {
    @Index(name = "idx_tasks_users_id", columnList = "users_id"),
    @Index(name = "idx_tasks_delete_flag", columnList = "delete_flag")
})
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
  private int deleteFlag;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

}
