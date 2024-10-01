package com.example.myPortfolio.controller;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.form.LoginForm;
import com.example.myPortfolio.service.UsersService;

@Controller
@RequestMapping("/login")
public class LoginController {

  @Autowired
  private UsersService usersService;

  // ログイン画面を表示
  @GetMapping
  public String showLoginPage(Model model) {
    model.addAttribute("loginForm", new LoginForm()); // LoginFormを追加
    return "login"; // login.htmlを表示
  }

  @PostMapping
  public String login(@RequestParam(required = false) String email, @RequestParam(required = false) String password, HttpSession session, Model model) {
    Optional<Users> userOpt = usersService.authenticateUser(email, password);

    if (userOpt.isPresent()) {
      // ユーザーが存在する場合、セッションにユーザーIDを保存
      session.setAttribute("userId", userOpt.get().getId());
      return "redirect:/home";
    } else {
      model.addAttribute("error", "ユーザーが見つかりませんでした");
      model.addAttribute("loginForm", new LoginForm()); // LoginFormを追加
      return "login"; // ログイン失敗時
    }
  }
}
