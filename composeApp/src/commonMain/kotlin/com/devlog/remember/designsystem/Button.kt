package com.devlog.remember.designsystem


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * 디자인 시스템 버튼 (Primary / Success / Danger / Outline / Ghost)
 * - 로딩 지원, 좌/우 아이콘, 세 가지 크기, 꽉 채우기 옵션
 */
@Composable
fun RDSButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Primary,
    size: ButtonSize = ButtonSize.Medium,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    leading: (@Composable (() -> Unit))? = null,
    trailing: (@Composable (() -> Unit))? = null,
    text: String
) {
    val colors = style.colors()
    val height = size.height
    val corner = size.corner
    val textStyle = size.textStyle()

    val baseModifier = if (fullWidth) modifier.fillMaxWidth() else modifier

    val shape: Shape = RoundedCornerShape(corner)

    val buttonColors = when (style) {
        ButtonStyle.Primary, ButtonStyle.Success, ButtonStyle.Danger ->
            ButtonDefaults.buttonColors(
                containerColor = colors.container,
                contentColor = colors.content,
                disabledContainerColor = colors.container.copy(alpha = 0.4f),
                disabledContentColor = colors.content.copy(alpha = 0.6f),
            )
        ButtonStyle.Outline ->
            ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = colors.container,
                disabledContentColor = colors.container.copy(alpha = 0.4f)
            )
        ButtonStyle.Ghost ->
            ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = colors.container,
                disabledContentColor = colors.container.copy(alpha = 0.4f)
            )
    }

    val border = when (style) {
        ButtonStyle.Outline -> BorderStroke(1.5.dp, colors.container.copy(alpha = 0.55f))
        else -> null
    }


    val backgroundBrush =
        if (style == ButtonStyle.Primary && enabled && !loading)
            Brush.horizontalGradient(
                listOf(colors.container, colors.container.copy(0.92f))
            ) else null


    val contentPadding = PaddingValues(
        horizontal = size.hPadding, vertical = size.vPadding
    )

    (if (style == ButtonStyle.Outline) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled && !loading,
            shape = shape,
            border = border,
            modifier = baseModifier.height(height),
            colors = buttonColors,
            contentPadding = contentPadding
        ) { ButtonContent(loading, leading, trailing, text, textStyle) }
    } else {
        // Filled / Ghost
        Button(
            onClick = onClick,
            enabled = enabled && !loading,
            shape = shape,
            modifier = baseModifier
                .height(height)
                .then(
                    if (backgroundBrush != null)
                        Modifier.clip(shape) else Modifier
                ),
            colors = buttonColors,
            elevation = if (style == ButtonStyle.Ghost)
                ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            else ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
            contentPadding = contentPadding
        ) {
            if (backgroundBrush != null) {
                // 그라데이션 덮기
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Box(
                        Modifier
                            .matchParentSize()
                            .clip(shape)
                            .background(backgroundBrush)
                    )
                    ButtonContent(loading, leading, trailing, text, textStyle)
                }
            } else {
                ButtonContent(loading, leading, trailing, text, textStyle)
            }
        }
    })
}

@Composable
private fun ButtonContent(
    loading: Boolean,
    leading: (@Composable (() -> Unit))?,
    trailing: (@Composable (() -> Unit))?,
    text: String,
    textStyle: androidx.compose.ui.text.TextStyle
) {
    AnimatedContent(targetState = loading, label = "btn-loading") { isLoading ->
        if (isLoading) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    strokeWidth = 2.5.dp,
                    modifier = Modifier
                        .size(18.dp),
                    color = LocalContentColor.current
                )
                Spacer(Modifier.width(8.dp))
                Text("로딩 중…", style = textStyle)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leading != null) {
                    leading()
                    Spacer(Modifier.width(8.dp))
                }
                Text(text, style = textStyle, maxLines = 1)
                if (trailing != null) {
                    Spacer(Modifier.width(8.dp))
                    trailing()
                }
            }
        }
    }
}

@Immutable
enum class ButtonStyle { Primary, Success, Danger, Outline, Ghost }

@Immutable
enum class ButtonSize(val height: Dp, val hPadding: Dp, val vPadding: Dp, val corner: Dp) {
    Small(40.dp, 14.dp, 8.dp, 12.dp),
    Medium(48.dp, 18.dp, 10.dp, 14.dp),
    Large(56.dp, 22.dp, 12.dp, 16.dp);

    @Composable
    fun textStyle() = when (this) {
        Small -> LocalTextStyle.current.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Medium -> LocalTextStyle.current.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Large -> LocalTextStyle.current.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

private data class ButtonColors(val container: Color, val content: Color)

@Composable
private fun ButtonStyle.colors(): ButtonColors {
    // 라이트/다크 모두 무난한 팔레트
    return when (this) {
        ButtonStyle.Primary -> ButtonColors(container = Color(0xFF635BFF), content = Color.White)
        ButtonStyle.Success -> ButtonColors(container = Color(0xFF10B981), content = Color.White)
        ButtonStyle.Danger  -> ButtonColors(container = Color(0xFFEF4444), content = Color.White)
        ButtonStyle.Outline -> ButtonColors(container = Color(0xFF374151), content = Color(0xFF374151))
        ButtonStyle.Ghost   -> ButtonColors(container = Color(0xFF111827), content = Color(0xFF111827))
    }
}


@Preview
@Composable
fun PreviewUiKitButton() {
    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RDSButton(onClick = {}, text = "Primary")
        RDSButton(onClick = {}, text = "Success", style = ButtonStyle.Success)
        RDSButton(onClick = {}, text = "Danger", style = ButtonStyle.Danger)
        RDSButton(onClick = {}, text = "Outline", style = ButtonStyle.Outline)
        RDSButton(onClick = {}, text = "Ghost", style = ButtonStyle.Ghost)
        RDSButton(onClick = {}, text = "Loading…", loading = true, fullWidth = true)
        RDSButton(onClick = {}, text = "Large", size = ButtonSize.Large, fullWidth = true)
    }
}
