package com.example.myPortfolio.exception;

public class UserNotFoundException extends RuntimeException {

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   * @param cause   エラー原因
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
