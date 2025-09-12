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
import com.devlog.remember.model.Word

@Composable
fun LessonScreen(
    words: List<Word>,              // 오늘 묶음 (예: words.take(15))
    index: Int,                     // 현재 인덱스 (0-based)
    onPrev: () -> Unit = {},        // ← 버튼 등 필요시
    onKnown: () -> Unit,            // “알고 있는 단어예요”
    onUnknown: () -> Unit           // “모르는 단어예요”
) {
    val total = words.size.coerceAtLeast(1)
    val safeIdx = index.coerceIn(0, total - 1)
    val item = words[safeIdx]
    var revealed by remember(safeIdx) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // 헤더
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("←", fontSize = 20.sp, modifier = Modifier.clickable { onPrev() })
            Spacer(Modifier.width(8.dp))
            Text("오늘 학습", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
            Text("${safeIdx}/$total", color = Color(0xFF6B7280))
        }
        Spacer(Modifier.height(8.dp))

        // 프로그레스바
        LinearProgressIndicator(
            progress = (safeIdx / total.toFloat()),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp)),
            trackColor = Color(0xFFE5E7EB)
        )
        Spacer(Modifier.height(16.dp))

        // 카드
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFF3F4F6))
                .clickable { revealed = true },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.term,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                if (revealed) {
                    Text(item.meaning, fontSize = 18.sp, color = Color(0xFF374151))
                    item.example?.let {
                        Spacer(Modifier.height(6.dp))
                        Text(it, fontSize = 14.sp, color = Color(0xFF6B7280), textAlign = TextAlign.Center)
                    }
                } else {
                    Text("탭: 뜻/예문 보기", fontSize = 14.sp, color = Color(0xFF9CA3AF))
                }
            }
        }

        // 하단 버튼
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { revealed = false; onUnknown() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(16.dp)
            ) { Text("모르는 단어예요", fontWeight = FontWeight.SemiBold) }

            Button(
                onClick = { revealed = false; onKnown() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(16.dp)
            ) { Text("알고 있는 단어예요", fontWeight = FontWeight.SemiBold) }
        }
        Spacer(Modifier.height(8.dp))
    }
}
