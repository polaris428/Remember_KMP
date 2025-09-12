package com.devlog.remember.feature.home



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    appTitle: String = "RememBear",
    totalLearned: Int,
    reviewsDue: Int,
    onStartLesson: () -> Unit,
    onChangeGoal: () -> Unit,
    onOpenReviews: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 타이틀
        Text(appTitle, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)

        // 총 학습한 단어
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("총 학습한 단어", color = Color(0xFF4B5563))
            Text("$totalLearned", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
        }

        // 버튼 행: 목표 변경 / 오늘 학습 시작
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onChangeGoal,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) { Text("목표 변경") }

            Button(
                onClick = onStartLesson,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF635BFF))
            ) { Text("오늘 학습 시작", fontWeight = FontWeight.SemiBold) }
        }

        // 복습 카드
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Text("오늘 복습 ${reviewsDue}개", fontWeight = FontWeight.SemiBold)
                }
                Button(
                    onClick = onOpenReviews,
                    enabled = reviewsDue > 0,
                    shape = RoundedCornerShape(12.dp),
                    colors = if (reviewsDue > 0)
                        ButtonDefaults.buttonColors(containerColor = Color(0xFF6B7280))
                    else
                        ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE5E7EB),
                            contentColor = Color(0xFF9CA3AF)
                        )
                ) {
                    Text(if (reviewsDue > 0) "복습 시작" else "복습 없음")
                }
            }
        }




    }
}
