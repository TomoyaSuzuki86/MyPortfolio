package com.example.myPortfolio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.example.myPortfolio.service.UsersService;

@SpringBootTest
class MyPortfolioApplicationTests {

  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoads() {
    // コンテキストが正しくロードされたことを確認する
    assertNotNull(context, "Application context should be loaded");

    // 特定のBeanがロードされているか確認
    assertNotNull(context.getBean(UsersService.class), "UsersService bean should be loaded");
  }
}