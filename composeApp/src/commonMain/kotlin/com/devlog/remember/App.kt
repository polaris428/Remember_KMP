package com.devlog.remember

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.devlog.remember.feature.home.HomeScreen
import com.devlog.remember.feature.lesson.LessonScreen // (이전 RN 스타일 LessonScreen 사용)
import com.devlog.remember.model.Word
import com.devlog.remember.data.loadWords
enum class Route { Home, Lesson }
@Composable
fun App() {

    MaterialTheme {


        var route by remember { mutableStateOf(Route.Home) }


        var totalLearned by remember { mutableStateOf(0) }
        var words by remember { mutableStateOf<List<Word>>(emptyList()) }
        var idx by remember { mutableStateOf(0) }

        LaunchedEffect(Unit) { words = loadWords() }
        val batch = remember(words) { words.take(15) } // 오늘 학습 분량

        when (route) {
            Route.Home -> HomeScreen(
                totalLearned = totalLearned,
                reviewsDue = 0,
                onStartLesson = {
                    idx = 0                 // 오늘 학습 시작 시 인덱스 리셋
                    route = Route.Lesson
                },
                onChangeGoal = { /* TODO */ },
                onOpenReviews = { /* TODO */ }
            )

            Route.Lesson -> LessonScreen(
                words = batch,
                index = idx,
                onKnown = {
                    idx++
                    if (idx >= batch.size) {         // 오늘 분량 끝
                        totalLearned += batch.size
                        route = Route.Home
                    }
                },
                onUnknown = {
                    idx++
                    if (idx >= batch.size) {
                        totalLearned += batch.size
                        route = Route.Home
                    }
                }
            )
        }
    }
}
