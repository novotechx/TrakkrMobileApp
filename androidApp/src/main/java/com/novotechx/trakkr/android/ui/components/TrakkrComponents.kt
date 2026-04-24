package com.novotechx.trakkr.android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novotechx.trakkr.android.ui.theme.TrakkrColors

@Composable
fun StatBox(
    label: String,
    value: String,
    unit: String = "",
    large: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = value,
            fontSize = if (large) 28.sp else 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            color = TrakkrColors.TextPrimary,
            letterSpacing = (-0.5).sp,
        )
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = TrakkrColors.Gold,
            )
        }
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = TrakkrColors.TextDim,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
fun TrakkrCard(
    modifier: Modifier = Modifier,
    highlight: Boolean = false,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val bg = if (highlight) TrakkrColors.GoldSubtle else TrakkrColors.Surface
    val borderColor = if (highlight) TrakkrColors.GoldBorder else TrakkrColors.Outline
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(bg, shape)
            .border(1.dp, borderColor, shape)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(14.dp),
    ) {
        content()
    }
}

@Composable
fun TrakkrChip(
    label: String,
    active: Boolean = false,
    onClick: () -> Unit = {},
    activeColor: Color = TrakkrColors.Gold,
) {
    val bg = if (active) activeColor else TrakkrColors.SurfaceContainer
    val textColor = if (active) TrakkrColors.Background else TrakkrColors.TextDim
    val borderColor = if (active) Color.Transparent else TrakkrColors.Outline

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val gradient = Brush.linearGradient(
        colors = listOf(TrakkrColors.GoldDark, TrakkrColors.Gold, TrakkrColors.GoldLight),
    )
    val disabledGradient = Brush.linearGradient(
        colors = listOf(TrakkrColors.SurfaceContainer, TrakkrColors.SurfaceContainer),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (enabled) gradient else disabledGradient)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (enabled) TrakkrColors.Background else TrakkrColors.TextDim,
        )
    }
}

@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = TrakkrColors.Gold,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(1.5.dp, color, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
    }
}

@Composable
fun StartButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradient = Brush.linearGradient(
        colors = listOf(TrakkrColors.GoldDark, TrakkrColors.Gold, TrakkrColors.GoldLight),
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "▶  START",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TrakkrColors.Background,
            letterSpacing = 2.sp,
        )
    }
}

@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        color = TrakkrColors.Gold,
        letterSpacing = 1.2.sp,
        modifier = modifier.padding(bottom = 6.dp),
    )
}

@Composable
fun TrakkrListItem(
    title: String,
    subtitle: String = "",
    icon: String = "",
    rightText: String = "",
    rightColor: Color = TrakkrColors.TextDim,
    showDivider: Boolean = true,
    onClick: () -> Unit = {},
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 10.dp, horizontal = 4.dp),
        ) {
            if (icon.isNotEmpty()) {
                Text(
                    text = icon,
                    fontSize = 16.sp,
                    modifier = Modifier.width(24.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TrakkrColors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        fontSize = 10.sp,
                        color = TrakkrColors.TextDim,
                    )
                }
            }
            if (rightText.isNotEmpty()) {
                Text(
                    text = rightText,
                    fontSize = 11.sp,
                    color = rightColor,
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(color = TrakkrColors.Outline, thickness = 1.dp)
        }
    }
}

@Composable
fun ActivityTypeSelector(
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    val types = listOf(
        "🏃" to "Run",
        "🚴" to "Cycle",
        "🚶" to "Walk",
        "🥾" to "Hike",
        "⚡" to "Other",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        types.forEachIndexed { index, (emoji, label) ->
            val isSelected = index == selectedIndex
            val bg = if (isSelected) TrakkrColors.GoldSubtle else TrakkrColors.Surface
            val borderColor = if (isSelected) TrakkrColors.GoldBorder else TrakkrColors.Outline
            val borderWidth = if (isSelected) 2.dp else 1.dp

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bg)
                    .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
                    .clickable { onSelect(index) }
                    .padding(vertical = 10.dp, horizontal = 4.dp),
            ) {
                Text(text = emoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) TrakkrColors.Gold else TrakkrColors.TextDim,
                )
            }
        }
    }
}

@Composable
fun CircleButton(
    size: Dp,
    onClick: () -> Unit,
    borderColor: Color = TrakkrColors.Outline,
    backgroundColor: Color = Color.Transparent,
    useGoldGradient: Boolean = false,
    content: @Composable () -> Unit,
) {
    val bgModifier = if (useGoldGradient) {
        Modifier.background(
            Brush.linearGradient(listOf(TrakkrColors.GoldDark, TrakkrColors.Gold)),
            CircleShape,
        )
    }
    else {
        Modifier.background(backgroundColor, CircleShape)
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .then(bgModifier)
            .border(2.dp, if (useGoldGradient) Color.Transparent else borderColor, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun StatusBadge(
    text: String,
    color: Color,
    bgColor: Color,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp),
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            color = color,
        )
    }
}
