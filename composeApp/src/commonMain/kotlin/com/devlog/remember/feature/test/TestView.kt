// composeApp/src/commonMain/kotlin/com/devlog/remember/feature/test/TestScreenRN.kt
package com.devlog.remember.feature.test

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devlog.remember.model.Word
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.ArrayDeque

@Composable
fun TestScreen(
    words: List<Word>,
    onCompleteAllCorrect: () -> Unit,
    onBack: () -> Unit = {}
) {

    val greenBg  = Color(0xFFD1FAE5)  // emerald-100
    val greenBd  = Color(0xFF10B981)  // emerald-500
    val redBg    = Color(0xFFFEE2E2)  // red-100
    val redBd    = Color(0xFFEF4444)  // red-500
    val chipBg   = Color(0xFFF8FAFC)  // slate-50
    val chipBd   = Color(0xFFE5E7EB)  // gray-200
    val cardBg   = Color(0xFFF3F4F6)  // gray-100
    val hintTx   = Color(0xFF9CA3AF)  // gray-400
    val titleTx  = Color(0xFF111827)  // gray-900

    val queue = remember(words) { ArrayDeque(words) }
    var correctCount by remember { mutableStateOf(0) }
    var locked by remember { mutableStateOf(false) }              // 피드백 동안 입력 잠금
    var selected by remember { mutableStateOf<String?>(null) }
    var correctAnswer by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    fun makeOptions(pool: List<Word>, current: Word, count: Int = 4): List<String> {
        val wrongs = pool.filter { it != current }.shuffled().take(count - 1).map { it.meaning }
        return (wrongs + current.meaning).shuffled()
    }

    if (queue.isEmpty()) {
        LaunchedEffect(Unit) { onCompleteAllCorrect() }
        return
    }

    val current = queue.first()
    val options = remember(current, words) { makeOptions(words, current) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "←",
                fontSize = 20.sp,
                modifier = Modifier
                    .clickable(enabled = !locked) { onBack() }
                    .padding(4.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text("테스트", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = titleTx)
            Spacer(Modifier.weight(1f))
            Text("남은 ${queue.size} / 전체 ${words.size} · 정답 $correctCount", color = hintTx)
        }
        Spacer(Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { correctCount / words.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            trackColor = Color(0xFFE5E7EB)
        )
        Spacer(Modifier.height(16.dp))

        // 문제 카드
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 18.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(current.term, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = titleTx)
                Spacer(Modifier.height(6.dp))
                Text("n. • L1", fontSize = 14.sp, color = hintTx) // 필요 없으면 지워도 됨
                Spacer(Modifier.height(16.dp))
            }
        }
        Spacer(Modifier.height(14.dp))

        // 보기(칩) 리스트
        options.forEach { option ->
            val isSelected = selected == option
            val isCorrect  = (option == correctAnswer)
            val border = when {
                isSelected && correctAnswer != null && option == current.meaning -> BorderStroke(1.dp, greenBd)
                isSelected && correctAnswer != null && option != current.meaning -> BorderStroke(1.dp, redBd)
                !isSelected && correctAnswer != null && isCorrect               -> BorderStroke(1.dp, greenBd) // 오답 선택 시 정답도 표시
                else -> BorderStroke(1.dp, chipBd)
            }
            val bg = when {
                isSelected && correctAnswer != null && option == current.meaning -> greenBg
                isSelected && correctAnswer != null && option != current.meaning -> redBg
                !isSelected && correctAnswer != null && isCorrect               -> greenBg.copy(alpha = 0.6f)
                else -> chipBg
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = bg,
                border = border,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .let {
                        if (!locked) it.clickable {
                            if (selected != null) return@clickable
                            selected = option
                            correctAnswer = current.meaning
                            locked = true
                            scope.launch {
                                delay(600)
                                if (option == current.meaning) {
                                    queue.removeFirst()
                                    correctCount++
                                } else {
                                    queue.removeFirst()
                                    queue.addLast(current)
                                }
                                // reset feedback state
                                selected = null
                                correctAnswer = null
                                locked = false
                            }
                        } else it
                    }
            ) {
                Text(
                    option,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    color = titleTx
                )
            }
        }

        Spacer(Modifier.height(8.dp))
    }
}
