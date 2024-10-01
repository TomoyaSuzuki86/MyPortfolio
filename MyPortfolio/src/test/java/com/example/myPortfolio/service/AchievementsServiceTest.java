package com.example.myPortfolio.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.myPortfolio.entity.Achievements;
import com.example.myPortfolio.entity.Tasks;
import com.example.myPortfolio.repository.AchievementsRepository;

class AchievementsServiceTest extends AchievementsService {

    @Mock
    private AchievementsRepository achievementsRepository;

    @InjectMocks
    private AchievementsService achievementsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * タスクIDに紐づく実績を取得するテスト。
     * <p>
     * タスクIDに紐づく2つのモック実績を返す設定にし、サービスクラスのメソッドが
     * 正しくリポジトリのデータを取得するかを検証します。
     * </p>
     */
    @Test
    void testFindByTasksId_Success() {
        // テスト用のモック実績リストを作成
        List<Achievements> mockAchievementsList = new ArrayList<>();
        mockAchievementsList.add(new Achievements(null, "Description 1", 30));
        mockAchievementsList.add(new Achievements(null, "Description 2", 60));

        // 実績リポジトリがタスクIDに紐づく実績を返すよう設定
        when(achievementsRepository.findByTasksIdAndDeleteFlag(1L, 0)).thenReturn(mockAchievementsList);

        List<Achievements> result = achievementsService.findByTasksId(1L);

        // 実績の取得結果を検証
        assertEquals(2, result.size());
        assertEquals("Description 1", result.get(0).getDescription());
        assertEquals(30, result.get(0).getActualTime());
        verify(achievementsRepository, times(1)).findByTasksIdAndDeleteFlag(1L, 0);
    }

    /**
     * 実績を新規作成するテスト。
     * <p>
     * タスクオブジェクトを使用して、サービスクラスのメソッドが正しく
     * 実績を作成し、リポジトリに保存されるかを確認します。
     * </p>
     */
    @Test
    void testCreateAchievement_Success() {
        // テスト用のタスクオブジェクト
        Tasks mockTask = new Tasks();
        mockTask.setTaskName("Task 1");

        Achievements mockAchievement = new Achievements(mockTask, "New Achievement", 120);

        // achievementsRepository.save() が呼ばれたときにモック実績を返す
        when(achievementsRepository.save(any(Achievements.class))).thenReturn(mockAchievement);

        // 実績を作成
        Achievements result = achievementsService.createAchievement(mockTask, "New Achievement", 120);

        // 実績作成結果を検証
        assertEquals("New Achievement", result.getDescription());
        assertEquals(120, result.getActualTime());
        verify(achievementsRepository, times(1)).save(any(Achievements.class));
    }

    /**
     * 実績IDで実績を検索するテスト。
     * <p>
     * 実績IDが見つかる場合に、正しく実績情報が返されるかを確認します。
     * </p>
     */
    @Test
    void testFindById_Success() {
        Achievements mockAchievement = new Achievements(null, "Sample Achievement", 60);
        when(achievementsRepository.findById(1L)).thenReturn(Optional.of(mockAchievement));

        Optional<Achievements> result = achievementsService.findById(1L);

        // 実績が正しく返されることを確認
        assertTrue(result.isPresent());
        assertEquals("Sample Achievement", result.get().getDescription());
        verify(achievementsRepository, times(1)).findById(1L);
    }

    /**
     * 存在しない実績IDで検索するテスト。
     * <p>
     * 実績IDが見つからない場合、空の結果が返されることを確認します。
     * </p>
     */
    @Test
    void testFindById_NotFound() {
        when(achievementsRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Achievements> result = achievementsService.findById(999L);

        // 実績が存在しないことを確認
        assertFalse(result.isPresent());
        verify(achievementsRepository, times(1)).findById(999L);
    }

    /**
     * 実績の削除フラグを設定するテスト。
     * <p>
     * 実績の削除フラグが設定され、リポジトリに保存されるかを確認します。
     * </p>
     */
    @Test
    void testDeleteAchievement() {
        Achievements mockAchievement = new Achievements(null, "Sample Achievement", 60);
        mockAchievement.setId(1L);

        // 削除フラグを設定
        achievementsService.deleteAchievement(mockAchievement);

        // 削除フラグの確認
        assertEquals(1, mockAchievement.getDeleteFlag());
        verify(achievementsRepository, times(1)).save(mockAchievement);
    }

    /**
     * 実績時間の合計を計算するテスト。
     * <p>
     * タスクIDに紐づく3つの実績を使用して、合計時間を正しく算出できるか確認します。
     * </p>
     */
    @Test
    void testCalcSumAchievementTime_Success() {
        // テスト用の実績リスト
        List<Achievements> mockAchievementsList = new ArrayList<>();
        mockAchievementsList.add(new Achievements(null, "Task 1", 30));
        mockAchievementsList.add(new Achievements(null, "Task 2", 50));
        mockAchievementsList.add(new Achievements(null, "Task 3", 20));

        // achievementsRepository がタスクIDに紐づく実績リストを返すよう設定
        when(achievementsRepository.findByTasksIdAndDeleteFlag(1L, 0)).thenReturn(mockAchievementsList);

        int result = achievementsService.calcSumAchievementTime(1L);

        // 実績時間の合計を検証
        assertEquals(100, result); // 合計時間は100分
        verify(achievementsRepository, times(1)).findByTasksIdAndDeleteFlag(1L, 0);
    }

    /**
     * 実績リストが空の場合の合計時間計算テスト。
     * <p>
     * 実績リストが空のとき、合計時間が0になることを確認します。
     * </p>
     */
    @Test
    void testCalcSumAchievementTime_EmptyList() {
        // 空の実績リストを返すように設定
        when(achievementsRepository.findByTasksIdAndDeleteFlag(1L, 0)).thenReturn(new ArrayList<>());

        int result = achievementsService.calcSumAchievementTime(1L);

        // 実績が空のとき、合計は0
        assertEquals(0, result);
        verify(achievementsRepository, times(1)).findByTasksIdAndDeleteFlag(1L, 0);
    }
}
