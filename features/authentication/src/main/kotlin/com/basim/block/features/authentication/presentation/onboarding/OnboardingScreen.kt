package com.basim.block.features.authentication.presentation.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.basim.block.core.designkit.designsystem.component.BlockBackground
import com.basim.block.core.designkit.designsystem.component.BlockButton
import com.basim.block.core.designkit.designsystem.component.BlockPageIndicator
import com.basim.block.core.designkit.designsystem.component.BlockTextButton
import com.basim.block.core.designkit.designsystem.style.rememberDefaultScreenStyle
import com.basim.block.core.designkit.designsystem.theme.BlockTheme
import com.basim.block.core.designkit.designsystem.theme.LocalDimens
import com.basim.block.features.authentication.R
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

/**
 * Onboarding — pixel-built from Figma. A 3-page pager (Track / Forecast / Save), each page sharing
 * the same chrome: a Skip pill, a "block" illustration card, a serif headline + body, the page
 * indicator, and a bottom CTA that advances pages and finishes on the last one. Part of the
 * authentication (entry) flow — sits between Splash and the Auth Landing screen.
 *
 * The page model + content live in [OnboardingPage.kt]; the illustration canvas lives in
 * [OnboardingIllustration.kt].
 *
 * Figma: https://www.figma.com/design/R1bw3ysZmoZ83l0VCfUTCt/?node-id=39-7
 * Page: Auth Flows (2:3)  ·  Mode: Dark (canonical) — binds to M3 roles, so it also serves Light.
 */
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onSkip: () -> Unit = {},
    onFinish: () -> Unit = {},
) {
    val dimens = LocalDimens.current
    val pagerState = rememberPagerState(pageCount = { ONBOARDING_PAGES.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == ONBOARDING_PAGES.lastIndex

    Column(
        modifier = modifier
            .styleable(null, rememberDefaultScreenStyle())
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            BlockTextButton(onClick = onSkip) {
                Text(
                    text = stringResource(R.string.features_authentication_onboarding_skip),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.spacing24))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->

            OnboardingPageContent(
                page = ONBOARDING_PAGES[page],
                pageIndex = page,
                // Fade content in/out as it scrolls so the swipe reads as a slide + crossfade.
                modifier = Modifier.graphicsLayer {
                    val pageOffset =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
                            .absoluteValue
                    alpha = 1f - pageOffset.coerceIn(0f, 1f)

                    val scale = 0.85f + (1f - pageOffset.coerceIn(0f, 1f)) * 0.15f
                    scaleX = scale
                    scaleY = scale
                },
            )
        }

        Spacer(modifier = Modifier.height(dimens.spacing24))

        BlockPageIndicator(
            pageCount = ONBOARDING_PAGES.size,
            selectedIndex = pagerState.currentPage,
        )

        Spacer(modifier = Modifier.height(dimens.spacing32))

        BlockButton(
            onClick = {
                if (isLastPage) {
                    onFinish()
                } else {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = pagerState.currentPage + 1,
                            animationSpec = tween(5_00)
                        )
                    }
                }
            },
            shape = CircleShape, // filled pill CTA (Figma Button/Primary, radius-full)
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.controlHeight),
        ) {
            Text(
                text = stringResource(
                    if (isLastPage) {
                        R.string.features_authentication_onboarding_cta_get_started
                    } else {
                        R.string.features_authentication_onboarding_cta_next
                    },
                ),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingScreenPreview() {
    BlockTheme(darkTheme = true) {
        BlockBackground {
            OnboardingScreen()
        }
    }
}

// Per-page previews to verify each illustration (Forecast / Save) without swiping the pager.
@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingForecastPreview() {
    BlockTheme(darkTheme = true) {
        BlockBackground {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                OnboardingPageContent(page = ONBOARDING_PAGES[1], pageIndex = 1)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
private fun OnboardingSavePreview() {
    BlockTheme(darkTheme = true) {
        BlockBackground {
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                OnboardingPageContent(page = ONBOARDING_PAGES[2], pageIndex = 2)
            }
        }
    }
}
