package com.example.myPortfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AchievementsDto {
  
  private Long taskId;
  private String description;
  private int actualTime;

}
