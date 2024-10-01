package com.example.myPortfolio.exception;

/**
 * 不正なユーザーIDに対する例外クラス
 */
public class InvalidUserException extends RuntimeException {

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   */
  public InvalidUserException(String message) {
    super(message);
  }

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   * @param cause   エラー原因
   */
  public InvalidUserException(String message, Throwable cause) {
    super(message, cause);
  }
}
