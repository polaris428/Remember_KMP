package com.devlog.remember

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.devlog.remember.data.ProgressStore
import com.devlog.remember.data.loadWords
import com.devlog.remember.feature.home.HomeScreen
import com.devlog.remember.feature.lesson.LessonScreen
import com.devlog.remember.feature.test.TestScreen
import com.devlog.remember.model.Word

enum class Route { Home, Lesson , Test }

@Composable
fun App() {
    MaterialTheme {

        var route by remember { mutableStateOf(Route.Home) }

        // 영속 상태(저장소와 동기화)
        var words by remember { mutableStateOf<List<Word>>(emptyList()) }
        var globalIdx by remember { mutableStateOf(0) }       // 전체 단어에서 다음 시작 위치
        var totalLearned by remember { mutableStateOf(0) }    // 총 학습 수
        var todayCount by remember { mutableStateOf(0) }      // 오늘 학습 수
        var dailyGoal by remember { mutableStateOf(10) }      // 하루 목표

        // 세션 상태(오늘 배치)
        var sessionBatch by remember { mutableStateOf<List<Word>>(emptyList()) }

        // 최초 로딩: 저장소 동기화 + 단어 로딩
        LaunchedEffect(Unit) {
            ProgressStore.ensureToday()
            words        = loadWords()
            globalIdx    = ProgressStore.loadIndex()
            totalLearned = ProgressStore.loadTotal()
            todayCount   = ProgressStore.loadTodayCount()
            dailyGoal    = ProgressStore.loadDailyGoal()
        }



        when (route) {
            Route.Home -> HomeScreen(
                totalLearned = totalLearned,
                onStartLesson = {
                    val start = globalIdx.coerceIn(0, words.size)
                    val end = (start + dailyGoal).coerceAtMost(words.size)
                    sessionBatch = words.subList(start, end)
                    route = Route.Lesson
                },

            )

            Route.Lesson ->
                LessonScreen(
                    batch = sessionBatch,
                    onFinished = {
                        // 레슨 완료 → 테스트 화면으로
                        route = Route.Test

                    },
                    onBack = { route = Route.Home }
                )

            Route.Test -> TestScreen(
                words = sessionBatch, // 오늘 배치 전체로 테스트
                onCompleteAllCorrect = {
                    // 테스트 전부 정답 → 여기서 "한 번에" 저장
                    val learned = sessionBatch.size
                    val newIndex = (globalIdx + learned).coerceAtMost(words.size)

                    ProgressStore.completeSession(
                        countLearned = learned,
                        newIndex = newIndex
                    )

                    // UI 상태 갱신
                    globalIdx    = newIndex
                    totalLearned = ProgressStore.loadTotal()
                    todayCount   = ProgressStore.loadTodayCount()

                    // 홈으로 복귀
                    route = Route.Home
                })

        }
    }
}

/** batch의 첫 요소가 전체 words에서의 시작 인덱스가 idx이므로 그대로 반환 */
private fun List<com.devlog.remember.model.Word>.firstIndexInAll(all: List<com.devlog.remember.model.Word>): Int =
    if (isEmpty()) 0 else all.indexOf(first())
