package com.devlog.remember.feature.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeUp
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
import com.devlog.remember.designsystem.ButtonStyle
import com.devlog.remember.designsystem.RDSButton
import com.devlog.remember.model.Word
import com.devlog.remember.utils.rememberPlatformTts
import org.jetbrains.compose.ui.tooling.preview.Preview



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

    val tts = rememberPlatformTts()

    if (queue.isEmpty()) {
        LaunchedEffect(Unit) { onFinished() }
        return
    }
    val current = queue.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        LessonHeader(knownCount = knownCount, total = total, onBack = onBack)
        Spacer(Modifier.height(8.dp))

        LessonProgress(knownCount = knownCount, total = total)
        Spacer(Modifier.height(16.dp))

        LessonCard(
            word = current,
            showMeaning = showMeaning,
            onToggle = { showMeaning = !showMeaning },
            onSpeak = { tts.speak(current.headword) }
        )

        Spacer(Modifier.height(8.dp))

        LessonActions(
            onUnknown = {
                queue.addLast(queue.removeFirst())
                showMeaning = true
            },
            onKnown = {
                queue.removeFirst()
                knownCount++
                showMeaning = true
            }
        )
    }
}

@Composable
private fun LessonHeader(knownCount: Int, total: Int, onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        EdgeIconButton(onClick = onBack)
        Spacer(Modifier.width(8.dp))
        Text("오늘 학습", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Text("$knownCount / $total", color = Color(0xFF6B7280))
    }
}

@Composable
private fun LessonProgress(knownCount: Int, total: Int) {
    LinearProgressIndicator(
        progress = { knownCount / total.toFloat() },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(8.dp)),
        trackColor = Color(0xFFE5E7EB)
    )
}

@Composable
private fun LessonCard(
    word: Word,
    showMeaning: Boolean,
    onToggle: () -> Unit,
    onSpeak: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF3F4F6))
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 18.dp)
        ) {
            if (!showMeaning) {
                Text(
                    word.meaning,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 44.sp,
                )
                if (word.exampleKo.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        word.exampleKo,
                        fontSize = 14.sp,
                        color = Color(0xFF9CA3AF),
                        textAlign = TextAlign.Start,
                        lineHeight = 20.sp
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("탭: 단어 보기", fontSize = 14.sp, color = Color(0xFF9CA3AF))
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        word.headword,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,

                    )
                    Spacer(Modifier.width(12.dp))
                    IconButton(onClick = onSpeak) {
                        Icon(
                            imageVector = Icons.Filled.VolumeUp,
                            contentDescription = "Play pronunciation",
                            tint = Color(0xFF4B5563)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                if (word.exampleEn.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        word.exampleEn,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280),
                        textAlign = TextAlign.Start,
                        lineHeight = 20.sp
                    )
                }
                Text("탭: 뜻/예문 보기", fontSize = 14.sp, color = Color(0xFF9CA3AF))
            }
        }
    }
}

@Composable
private fun LessonActions(
    onUnknown: () -> Unit,
    onKnown: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
        verticalArrangement = Arrangement.Bottom
    ) {
        RDSButton(
            onClick = onUnknown,
            modifier = Modifier.fillMaxWidth(),
            style = ButtonStyle.Danger,
            size = ButtonSize.Large,
            text = "모르는 단어예요"
        )
        Spacer(Modifier.height(16.dp))
        RDSButton(
            onClick = onKnown,
            modifier = Modifier.fillMaxWidth(),
            style = ButtonStyle.Success,
            size = ButtonSize.Large,
            text = "알고 있는 단어예요"
        )
    }
}

@Composable
private fun EdgeIconButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "뒤로가기",
            modifier = Modifier.size(24.dp)
        )
    }
}



private fun sampleWord() = Word(
    id = 1,
    headword = "executive",
    pos = "n.",
    meaning = "경영진, 임원",
    exampleEn = "All of the executives agreed with the new policy.",
    exampleKo = "모든 경영진이 새 정책에 동의했습니다.",
    level = 1
)

@Preview
@Composable
private fun PreviewLessonHeader() {
    LessonHeader(knownCount = 3, total = 10, onBack = {})
}

@Preview
@Composable
private fun PreviewLessonProgress() {
    LessonProgress(knownCount = 5, total = 10)
}

@Preview
@Composable
private fun PreviewLessonCardMeaning() {
    LessonCard(
        word = sampleWord(),
        showMeaning = false,
        onToggle = {},
        onSpeak = {}
    )
}

@Preview
@Composable
private fun PreviewLessonCardWord() {
    LessonCard(
        word = sampleWord(),
        showMeaning = true,
        onToggle = {},
        onSpeak = {}
    )
}

@Preview
@Composable
private fun PreviewLessonActions() {
    LessonActions(onUnknown = {}, onKnown = {})
}
@Preview
@Composable
private fun PreviewLessonScreen() {
    LessonScreen(
        batch = listOf(
            sampleWord(),
            sampleWord().copy(id = 2, headword = "innovation", meaning = "혁신")
        ),
        onFinished = {},
        onBack = {}
    )
}