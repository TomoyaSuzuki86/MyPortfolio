package com.example.myPortfolio.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

  @NotBlank(message = "メールアドレスを入力してください")
  @Email(message = "正しいメールアドレス形式で入力してください")
  private String email;

  @NotBlank(message = "パスワードを入力してください")
  private String password;

  // デフォルトコンストラクタ
  public RegisterForm() {}

  // 引数ありコンストラクタ
  public RegisterForm(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
