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

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.form.HomeForm;
import com.example.myPortfolio.service.AchievementsService;
import com.example.myPortfolio.service.TasksService;

@Controller
@RequestMapping("/home")
public class HomeController {

  @Autowired
  private HttpSession httpSession; // HttpSession を使用してユーザーを管理

  @Autowired
  private TasksService tasksService;

  @Autowired
  private AchievementsService achievementsService;


  /**
   * ホーム画面の表示
   *
   * @param model 画面に渡すデータを格納するモデルオブジェクト
   * @return ホーム画面のテンプレート名
   */
  @GetMapping
  public String showHomePage(Model model) {

    Long userId = (Long) httpSession.getAttribute("userId");
    
    // ユーザーが存在するかチェック
    if (userId != null) {
      // HomeForm オブジェクトを作成して、ユーザーのタスク情報を格納
      HomeForm homeForm = new HomeForm(tasksService.getTaskFormsByUserId(userId));

      // モデルにホームフォームを設定
      model.addAttribute("homeForm", homeForm);

      // home.html 画面を表示
      return "home";
    } else {
      // ユーザーが未ログインの場合、ログイン画面にリダイレクト
      return "redirect:/login";
    }
  }

  // タスク編集画面からの登録処理
  @PostMapping("/editTask")
  public String editTask(@RequestParam(required = false) Long taskId, @RequestParam String taskName,
      @RequestParam int targetTime, Model model) {
    try {
      tasksService.saveOrUpdateTask(taskId, taskName, targetTime);
    } catch (Exception e) {
      model.addAttribute("error", "Task update failed: " + e.getMessage());
    }
    return "redirect:/home"; // 編集完了後、ホーム画面にリダイレクト
  }

  // 実績入力画面からの登録処理
  @PostMapping("/addAchievement")
  public String addAchievement(@RequestParam Long taskId, @RequestParam String description,
      @RequestParam int actualTime, Model model) {
    try {
      Optional<Tasks> optionalTasks = tasksService.findById(taskId);
      if (optionalTasks.isPresent()) {
        Tasks task = optionalTasks.get();
        achievementsService.createAchievement(task, description, actualTime);
      }
      return "redirect:/home"; // 実績登録後、ホーム画面にリダイレクト
    } catch (Exception e) {
      model.addAttribute("error", "Achievement creation failed: " + e.getMessage());
      return "achievementInputModal"; // エラーがあった場合は、入力画面を再表示
    }
  }
}
