package com.example.myPortfolio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.form.RegisterForm;
import com.example.myPortfolio.service.UsersService;

@Controller
@RequestMapping({"/", "/register"})
public class RegisterController {

  @Autowired
  private UsersService usersService;

  // 登録画面を表示
  @GetMapping
  public String showRegisterPage(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return "register"; // register.htmlを表示
  }

  // ユーザー登録処理
  @PostMapping
  public String registerUser(@ModelAttribute RegisterForm registerForm, Model model) {
    try {
      // RegisterFormからUsersオブジェクトを作成
      Users newUser = new Users(registerForm.getEmail(), registerForm.getPassword());
      usersService.createUsers(newUser);
      return "redirect:/login"; // 登録成功後、ログイン画面にリダイレクト
    } catch (Exception e) {
      model.addAttribute("error", "Registration failed: " + e.getMessage());
      return "register"; // エラーメッセージを表示してregister.htmlを再表示
    }
  }
}
