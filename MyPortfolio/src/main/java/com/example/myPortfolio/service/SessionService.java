package com.example.myPortfolio.service;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

  @Autowired
  private HttpSession session;

  /**
   * セッションからユーザーIDを取得するメソッド
   *
   * @return ユーザーID（Optional型）
   */
  public Optional<Long> getLoggedInUserId() {
    // セッションから "userId" というキーでユーザーIDを取得する
    Object userId = session.getAttribute("userId");

    // userIdが存在しない場合は null を返す
    if (userId != null && userId instanceof Long) {
      return Optional.of((Long) userId);
    }
    return Optional.empty(); // ユーザーがログインしていない場合
  }
}
