package com.example.myPortfolio.mapper;

import org.springframework.stereotype.Component;

import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.entity.Users;
import com.example.myPortfolio.form.TaskForm;

/**
 * Tasks エンティティと TaskForm の変換を行うクラス
 */
@Component
public class TaskMapper {

    /**
     * Tasks エンティティから TaskForm への変換を行うメソッド
     * 
     * @param tasks 変換対象のタスクエンティティ
     * @return 変換された TaskForm オブジェクト
     */
    public TaskForm toForm(Tasks tasks) {
        if (tasks == null) {
            return null;
        }

        // 達成率を計算 (実績時間 / 目標時間 * 100)
        int achievementRate = tasks.getTargetTime() > 0 
                ? (int) ((tasks.getActualTime() / (double) tasks.getTargetTime()) * 100) 
                : 0;

        // TaskForm を作成して値を設定
        TaskForm form = new TaskForm();
        form.setTaskName(tasks.getTaskName());
        form.setTargetTime(tasks.getTargetTime());
        form.setActualTime(tasks.getActualTime());
        form.setAchievementRate(achievementRate);

        return form;
    }

    /**
     * TaskForm から Tasks エンティティへの変換を行うメソッド
     * 
     * @param form 変換対象のフォーム
     * @param users フォームに関連するユーザーエンティティ
     * @return 変換された Tasks エンティティ
     */
    public Tasks toEntity(TaskForm form, Users users) {
        if (form == null || users == null) {
            return null;
        }

        // 新規の Tasks エンティティを作成して値を設定
        Tasks tasks = new Tasks(users, form.getTaskName(), form.getTargetTime());
        tasks.setActualTime(form.getActualTime());

        return tasks;
    }

    /**
     * 既存の Tasks エンティティを TaskForm の内容で更新するメソッド
     * 
     * @param form 更新用のフォーム
     * @param tasks 更新対象のタスクエンティティ
     * @return 更新された Tasks エンティティ
     */
    public Tasks updateEntity(TaskForm form, Tasks tasks) {
        if (form == null || tasks == null) {
            return null;
        }

        // Task エンティティの値をフォームの内容で上書き
        tasks.setTaskName(form.getTaskName());
        tasks.setTargetTime(form.getTargetTime());
        tasks.setActualTime(form.getActualTime());

        return tasks;
    }
}
