package com.foodsaver.app.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.poppins_black
import foodsaver.composeapp.generated.resources.poppins_bold
import foodsaver.composeapp.generated.resources.poppins_regular
import org.jetbrains.compose.resources.Font


private val fonts
    @Composable
    get() = FontFamily(
        Font(Res.font.poppins_black, FontWeight.Black),
        Font(Res.font.poppins_bold, FontWeight.Bold),
        Font(Res.font.poppins_regular, FontWeight.Normal),
    )

data class ThemeTypography(
    val title: TextStyle = TextStyle()
)

val Typography
    @Composable
    get() = ThemeTypography(
        title = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = fonts
        )
    )