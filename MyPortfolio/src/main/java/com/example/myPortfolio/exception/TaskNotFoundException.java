package com.example.myPortfolio.exception;

/**
 * タスクが見つからない場合の例外クラス
 */
public class TaskNotFoundException extends RuntimeException {

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   */
  public TaskNotFoundException(String message) {
    super(message);
  }

  /**
   * コンストラクタ
   *
   * @param message エラーメッセージ
   * @param cause   エラー原因
   */
  public TaskNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
