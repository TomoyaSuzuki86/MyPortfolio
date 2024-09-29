package com.example.myPortfolio.config;

import java.util.EnumSet;

import jakarta.servlet.SessionTrackingMode;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

  @Bean
  ServletContextInitializer servletContextInitializer() {
    return servletContext -> {
      // クッキーのみを使用してセッションを管理
      servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    };
  }
}