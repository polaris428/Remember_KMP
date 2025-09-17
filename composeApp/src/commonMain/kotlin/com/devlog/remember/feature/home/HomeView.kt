// composeApp/src/commonMain/kotlin/com/devlog/remember/feature/home/HomeScreenRN.kt
package com.devlog.remember.feature.home

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
import com.devlog.remember.designsystem.ButtonStyle
import com.devlog.remember.designsystem.RDSButton
import com.devlog.remember.utils.Analytics

@Composable
fun HomeScreen(
    totalLearned: Int,
    onStartLesson: () -> Unit,

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("RememBear", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)


        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("총 학습한 단어", color = Color(0xFF4B5563))
            Text("$totalLearned", fontSize = 42.sp, fontWeight = FontWeight.ExtraBold)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {

            RDSButton(
                onClick = onStartLesson,
                text = "오늘 학습 시작",
                style = ButtonStyle.Primary,
                fullWidth = true,
                modifier = Modifier.weight(1f)
            )

        }

        Spacer(Modifier.weight(1f))


    }
}
