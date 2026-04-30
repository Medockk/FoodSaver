package com.foodsaver.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.poppins_medium
import foodsaver.composeapp.generated.resources.poppins_regular
import foodsaver.composeapp.generated.resources.sen_bold
import foodsaver.composeapp.generated.resources.sen_extra_bold
import foodsaver.composeapp.generated.resources.sen_medium
import foodsaver.composeapp.generated.resources.sen_regular
import foodsaver.composeapp.generated.resources.sen_semi_bold
import org.jetbrains.compose.resources.Font

data class ThemeTypography(
    val headerBold: TextStyle = TextStyle(),
    val headerBoldSmall: TextStyle = TextStyle(),
    val headerMedium: TextStyle = TextStyle(),
    val headerUppercase: TextStyle = TextStyle(),
    val headerRegularBold: TextStyle = TextStyle(),

    val bodyRegular: TextStyle = TextStyle(),
    val bodyRegularBold: TextStyle = TextStyle(),
    val bodySmall: TextStyle = TextStyle(),
    val bodyMedium: TextStyle = TextStyle(),
    val bodyBold: TextStyle = TextStyle(),

    val inputPlaceholderRegular: TextStyle = TextStyle(),
    val inputPasswordPlaceholderRegular: TextStyle = TextStyle(),

    val bottomBarPrice: TextStyle = TextStyle(),
    val ingredientName: TextStyle = TextStyle(),
    val ingredientsSubtext: TextStyle = TextStyle()
)


private val fonts
    @Composable
    get() = FontFamily(
        Font(Res.font.sen_bold, FontWeight.Bold), // W700
        Font(Res.font.sen_extra_bold, FontWeight.ExtraBold), // W800
        Font(Res.font.sen_regular, FontWeight.Normal), // W400
        Font(Res.font.sen_medium, FontWeight.Medium), // W500
        Font(Res.font.sen_semi_bold, FontWeight.SemiBold), // W600
    )

private val poppinsFonts
    @Composable
    get() = FontFamily(
        Font(Res.font.poppins_medium, FontWeight.Medium),
        Font(Res.font.poppins_regular, FontWeight.Normal),
    )

val Typography
    @Composable
    get() = ThemeTypography(
        headerBold = TextStyle(
            fontFamily = fonts,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        ),
        headerRegularBold = TextStyle(
            fontFamily = fonts,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        ),
        headerBoldSmall = TextStyle(
            fontFamily = fonts,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        ),
        headerMedium = TextStyle(
            fontFamily = fonts,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        bodyRegular = TextStyle(
            fontFamily = fonts,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        headerUppercase = TextStyle(
            fontFamily = fonts,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyRegularBold = TextStyle(
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        ),
        bodySmall = TextStyle(
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyMedium = TextStyle(
            fontFamily = fonts,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        ),
        bodyBold = TextStyle(
            fontFamily = fonts,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        inputPlaceholderRegular = TextStyle(
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
        ),
        inputPasswordPlaceholderRegular = TextStyle(
            fontFamily = fonts,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 16.sp
        ),
        bottomBarPrice = TextStyle(
            fontFamily = fonts,
            fontSize = 28.sp,
            fontWeight = FontWeight.Normal,
        ),
        ingredientName = TextStyle(
            fontFamily = poppinsFonts,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        ingredientsSubtext = TextStyle(
            fontFamily = poppinsFonts,
            fontWeight = FontWeight.Normal,
            fontSize = 8.sp
        ),
    )