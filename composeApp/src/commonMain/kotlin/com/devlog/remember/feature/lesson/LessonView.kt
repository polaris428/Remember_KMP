package com.devlog.remember.feature.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devlog.remember.designsystem.ButtonSize
import com.devlog.remember.model.Word
import com.devlog.remember.designsystem.ButtonStyle
import com.devlog.remember.designsystem.RDSButton
import com.devlog.remember.designsystem.RDSProgressBar
import kotlin.math.max
@Composable
fun LessonScreen(
    batch: List<Word>,
    onFinished: () -> Unit,
    onBack: () -> Unit
) {
    val total = batch.size.coerceAtLeast(1)
    val queue = remember(batch) { ArrayDeque(batch) }
    var knownCount by remember { mutableStateOf(0) }
    var showMeaning by remember(queue.size) { mutableStateOf(true) }

    if (queue.isEmpty()) {
        LaunchedEffect(Unit) { onFinished() }  // ✅ 리스트 안 넘김
        return
    }

    val current = queue.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 헤더 + 진행
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("←", fontSize = 20.sp, modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.width(8.dp))
            Text("오늘 학습", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("$knownCount / $total", color = Color(0xFF6B7280))
        }
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { knownCount / total.toFloat() },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp)),
            trackColor = Color(0xFFE5E7EB)
        )
        Spacer(Modifier.height(16.dp))

        // 카드 (탭으로 뜻↔단어)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF3F4F6))
                .clickable { showMeaning = !showMeaning },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (showMeaning) {
                    Text(current.meaning, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp)); Text("탭: 단어 보기", fontSize = 14.sp, color = Color(0xFF9CA3AF))
                } else {
                    Text(current.term, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp)); Text("탭: 뜻/예문 보기", fontSize = 14.sp, color = Color(0xFF9CA3AF))
                    current.example?.let { Spacer(Modifier.height(8.dp)); Text(it, fontSize = 14.sp, color = Color(0xFF6B7280), textAlign = TextAlign.Center) }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RDSButton(
                onClick = {
                    // 모르면 뒤로
                    queue.addLast(queue.removeFirst())
                    showMeaning = true
                },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.Danger,
                size = ButtonSize.Large,  // ⬅ Medium 대신 Large 써도 안정감 있음
                text = "모르는 단어예요"
            )

            RDSButton(
                onClick = {
                    // 알면 제거 + 진행 증가
                    queue.removeFirst()
                    knownCount++
                    showMeaning = true
                },
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.Success,
                size = ButtonSize.Large,
                text = "알고 있는 단어예요"
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}
