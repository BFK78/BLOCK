package com.basim.block.features.authentication.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.theme.BlockTextStyles
import com.basim.block.features.authentication.R

// ── Illustrations ──────────────────────────────────────────────────────────────────────────────
// Each illustration is a 345×300 canvas (Box) with a rounded panel and absolutely-offset "blocks",
// matching the Figma frames exactly (nodes 39:11 / 40:18 / 41:30).

private val ILLUSTRATION_HEIGHT = 300.dp
private val PANEL_RADIUS = 24.dp // no exact Dimens token (radii jump 20 → 28)

@Composable
internal fun OnboardingIllustration(pageIndex: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(ILLUSTRATION_HEIGHT),
    ) {
        Panel()
        when (pageIndex) {
            0 -> TrackBlocks()
            1 -> ForecastBlocks()
            else -> SaveBlocks()
        }
    }
}

@Composable
private fun Panel() {
    Box(
        modifier = Modifier
            .offset(x = 32.dp, y = 40.dp)
            .size(width = 280.dp, height = 220.dp)
            .clip(RoundedCornerShape(PANEL_RADIUS))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(PANEL_RADIUS),
            ),
    )
}

@Composable
private fun Block(x: Dp, y: Dp, size: Dp, color: Color, radius: Dp) {
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .size(size)
            .clip(RoundedCornerShape(radius))
            .background(color),
    )
}

@Composable
private fun TrackBlocks() {
    val emerald = MaterialTheme.colorScheme.secondary
    val emeraldDark = MaterialTheme.colorScheme.secondaryContainer
    val champagne = MaterialTheme.colorScheme.tertiary
    val r = 10.dp
    // row 1 — emerald
    Block(64.dp, 80.dp, 36.dp, emerald, r)
    Block(108.dp, 80.dp, 36.dp, emerald, r)
    Block(152.dp, 80.dp, 36.dp, emerald, r)
    Block(196.dp, 80.dp, 36.dp, emerald, r)
    // row 2 — dark emerald
    Block(64.dp, 132.dp, 36.dp, emeraldDark, r)
    Block(108.dp, 132.dp, 36.dp, emeraldDark, r)
    Block(152.dp, 132.dp, 36.dp, emeraldDark, r)
    // row 3 — champagne
    Block(64.dp, 184.dp, 36.dp, champagne, r)
    Block(108.dp, 184.dp, 36.dp, champagne, r)
}

@Composable
private fun ForecastBlocks() {
    val emerald = MaterialTheme.colorScheme.secondary
    val emeraldDark = MaterialTheme.colorScheme.secondaryContainer
    val champagne = MaterialTheme.colorScheme.tertiary
    val r = 10.dp
    Block(70.dp, 212.dp, 34.dp, emerald, r)
    Block(116.dp, 212.dp, 34.dp, emerald, r)
    Block(116.dp, 170.dp, 34.dp, emerald, r)
    Block(162.dp, 212.dp, 34.dp, emerald, r)
    Block(162.dp, 170.dp, 34.dp, emerald, r)
    Block(162.dp, 128.dp, 34.dp, emeraldDark, r)
    Block(208.dp, 212.dp, 34.dp, emerald, r)
    Block(208.dp, 170.dp, 34.dp, emerald, r)
    Block(208.dp, 128.dp, 34.dp, emerald, r)
    Block(208.dp, 86.dp, 34.dp, champagne, r)
    Icon(
        painter = painterResource(R.drawable.features_authentication_onboarding_ic_sparkle),
        contentDescription = stringResource(
            R.string.features_authentication_onboarding_sparkle_content_desc,
        ),
        tint = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .offset(x = 248.dp, y = 52.dp)
            .size(36.dp),
    )
}

@Composable
private fun SaveBlocks() {
    val emerald = MaterialTheme.colorScheme.secondary
    val champagne = MaterialTheme.colorScheme.tertiary
    val r = 12.dp
    Block(96.dp, 196.dp, 44.dp, emerald, r)
    Block(96.dp, 144.dp, 44.dp, emerald, r)
    Block(96.dp, 92.dp, 44.dp, emerald, r)
    DashedBlock(x = 96.dp, y = 40.dp, size = 44.dp, color = champagne, radius = r)
    GoalTag()
}

@Composable
private fun DashedBlock(x: Dp, y: Dp, size: Dp, color: Color, radius: Dp) {
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .size(size)
            .drawBehind {
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(radius.toPx()),
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(4.dp.toPx(), 4.dp.toPx()),
                        ),
                    ),
                )
            },
    )
}

@Composable
private fun GoalTag() {
    Column(
        modifier = Modifier
            .offset(x = 180.dp, y = 80.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = stringResource(R.string.features_authentication_onboarding_save_goal_name),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.features_authentication_onboarding_save_goal_amount),
            style = BlockTextStyles.numericMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}
