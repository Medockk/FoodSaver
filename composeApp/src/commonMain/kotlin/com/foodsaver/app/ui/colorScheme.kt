package com.foodsaver.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val primary: Color = Color.Unspecified,
    val primaryThin: Color = Color.Unspecified,

    val background: Color = Color.Unspecified,
    val onBackground: Color = Color.Unspecified,
    val onBackgroundSubtitle: Color = Color.Unspecified,
    val onBackgroundThin: Color = Color.Unspecified,
    val backgroundContrast: Color = Color.Unspecified,
    val onBackgroundContrast: Color = Color.Unspecified,
    val onBackgroundSecondary: Color = Color.Unspecified,
    val onBackgroundTertiary: Color = Color.Unspecified,

    val onButtonContent: Color = Color.Unspecified,
    val authenticationIconsTint: Color = Color.Unspecified,

    val fabBackground: Color = Color.Unspecified,
    val onFabBackground: Color = Color.Unspecified,

    val placeholderHint: Color = Color.Unspecified,
    val placeholderBackground: Color = Color.Unspecified,
    val onPlaceholderBackgroundInactive: Color = Color.Unspecified,
    val onPlaceholderBackgroundActive: Color = Color.Unspecified,

    val checkboxBorder: Color = Color.Unspecified,
    val checkboxTitle: Color = Color.Unspecified,
    val checkboxFillColor: Color = Color.Unspecified,
    val onCheckboxFillColor: Color = Color.Unspecified,

    val topBarSubtitleColor: Color = Color.Unspecified,
    val searchFieldBackground: Color = Color.Unspecified,

    val categorySeeAllColor: Color = Color.Unspecified,
    val mainCategoryClipColor: Color = Color.Unspecified,
    val primaryShadowColor: Color = Color.Unspecified,

    val shimmerColor: Color = Color.Unspecified,
    val imagePageUnselectedIndicatorColor: Color = Color.Unspecified,
    val imagePageSelectedIndicatorColor: Color = Color.Unspecified,

    val unselectedChipBorderColor: Color = Color.Unspecified,
    val counterColor: Color = Color.Unspecified,
    val counterButtonColor: Color = Color.Unspecified,
    val ingredientBackgroundColor: Color = Color.Unspecified,

    val cartFabColor: Color = Color.Unspecified,
    val cartBottomBarColor: Color = Color.Unspecified,
    val onCartFabColor: Color = Color.Unspecified,
    val completeColor: Color = Color.Unspecified,

    val deleteColor: Color = Color.Unspecified,
    val dividerLineColor: Color = Color.Unspecified,
)

val lightColorScheme = ColorScheme(
    primary = Color(0xFFFF7622),
    primaryThin = Color(0xFFFFE1CE),

    background = Color.White,
    onBackground = Color(0xFF181C2E),
    onBackgroundSubtitle = Color(0xFF32343E),
    onBackgroundThin = Color(0xFF646982),
    onBackgroundSecondary = Color(0xFF1E1D1D),
    onBackgroundTertiary = Color(0xFFA0A5BA),

    backgroundContrast = Color(0xFF121223),
    onBackgroundContrast = Color.White,

    onButtonContent = Color.White,
    authenticationIconsTint = Color(0xFF1E1E2E),

    fabBackground = Color(0xFFECF0F4),
    onFabBackground = Color(0xFF5E616F),

    placeholderHint = Color(0xFFA0A5BA),
    placeholderBackground = Color(0xFFF0F5FA),
    onPlaceholderBackgroundInactive = Color(0xFFB4B9CA),
    onPlaceholderBackgroundActive = Color(0xFFA0A5BA),

    checkboxBorder = Color(0xFFE3EBF2),
    checkboxTitle = Color(0xFF7E8A97),
    checkboxFillColor = Color(0xFFFF3422),
    onCheckboxFillColor = Color.White,

    topBarSubtitleColor = Color(0xFF676767),
    searchFieldBackground = Color(0xFFF6F6F6),

    categorySeeAllColor = Color(0xFF333333),
    mainCategoryClipColor = Color(0xFFFFD27C),
    primaryShadowColor = Color(0xFF96969A),

    shimmerColor = Color(0xFF98A8B8),
    imagePageUnselectedIndicatorColor = Color(0x69FFFFFF),
    imagePageSelectedIndicatorColor = Color(0xFFFFFFFF),
    unselectedChipBorderColor = Color(0xFFEDEDED),
    counterColor = Color(0xFF121223),
    counterButtonColor = Color(0x33FFFFFF),
    ingredientBackgroundColor = Color(0xFFFFEBE4),

    cartFabColor = Color(0x1AFFFFFF),
    onCartFabColor = Color(0xFFFFFFFF),
    completeColor = Color(0xFF059C6A),
    cartBottomBarColor = Color(0xFFFFFFFF),
    deleteColor = Color(0xFFE04444),
    dividerLineColor = Color(0xFFEBEBEB),
)

val darkColorScheme = ColorScheme(
    primary = Color(0xFFFF7622),
    primaryThin = Color(0xFFFFE1CE),

    background = Color.White,
    onBackground = Color(0xFF181C2E),
    onBackgroundSubtitle = Color(0xFF32343E),
    onBackgroundThin = Color(0xFF646982),
    onBackgroundSecondary = Color(0xFF1E1D1D),
    onBackgroundTertiary = Color(0xFFA0A5BA),

    backgroundContrast = Color(0xFF121223),
    onBackgroundContrast = Color.White,

    onButtonContent = Color.White,
    authenticationIconsTint = Color(0xFF1E1E2E),

    fabBackground = Color(0xFFECF0F4),
    onFabBackground = Color(0xFF5E616F),

    placeholderHint = Color(0xFFA0A5BA),
    placeholderBackground = Color(0xFFF0F5FA),
    onPlaceholderBackgroundInactive = Color(0xFFB4B9CA),
    onPlaceholderBackgroundActive = Color(0xFFA0A5BA),

    checkboxBorder = Color(0xFFE3EBF2),
    checkboxTitle = Color(0xFF7E8A97),
    checkboxFillColor = Color(0xFFFF3422),
    onCheckboxFillColor = Color.White,

    topBarSubtitleColor = Color(0xFF676767),
    searchFieldBackground = Color(0xFFF6F6F6),

    categorySeeAllColor = Color(0xFF333333),
    mainCategoryClipColor = Color(0xFFFFD27C),
    primaryShadowColor = Color(0xFF96969A),

    shimmerColor = Color(0xFF98A8B8),
    imagePageUnselectedIndicatorColor = Color(0x69FFFFFF),
    imagePageSelectedIndicatorColor = Color(0xFFFFFFFF),
    unselectedChipBorderColor = Color(0xFFEDEDED),
)