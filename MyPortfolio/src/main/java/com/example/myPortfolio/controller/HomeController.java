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
import com.example.myPortfolio.form.AchievementForm;
import com.example.myPortfolio.form.AchievementForm.AchievementDetail;
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

    if (userId != null) {
      // HomeForm オブジェクトを作成して、ユーザーのタスク情報を格納
      HomeForm homeForm = new HomeForm(tasksService.getTaskFormsByUserId(userId));
      model.addAttribute("homeForm", homeForm);
      return "home";
    } else {
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
    return "redirect:/home";
  }

  // 実績入力画面からの登録処理
  @PostMapping("/addAchievement")
  public String addAchievement(AchievementForm taskAchievementForm, Model model) {
    try {
      Optional<Tasks> optionalTasks = tasksService.findById(taskAchievementForm.getTaskId());
      if (optionalTasks.isPresent()) {
        Tasks task = optionalTasks.get();

        // 各作業内容を実績として登録
        for (AchievementDetail detail : taskAchievementForm.getAchievementDetails()) {
          achievementsService.createAchievement(task, detail.getDescription(), detail.getActualTime());
        }
      }
      return "redirect:/home";
    } catch (Exception e) {
      model.addAttribute("error", "Achievement creation failed: " + e.getMessage());
      return "home";
    }
  }

  /**
   * タスク削除処理
   *
   * @param taskId 削除対象のタスクID
   * @return ホーム画面へのリダイレクト
   */
  @PostMapping("/deleteTask")
  public String deleteTask(@RequestParam Long taskId, Model model) {
    try {
      Optional<Tasks> optionalTask = tasksService.findById(taskId);
      if (optionalTask.isPresent()) {
        Tasks task = optionalTask.get();
        tasksService.deleteTask(task);
      } else {
        model.addAttribute("error", "指定されたタスクが見つかりません。");
      }
    } catch (Exception e) {
      model.addAttribute("error", "タスクの削除に失敗しました: " + e.getMessage());
    }
    return "redirect:/home"; // 削除完了後、ホーム画面へリダイレクト
  }
}
