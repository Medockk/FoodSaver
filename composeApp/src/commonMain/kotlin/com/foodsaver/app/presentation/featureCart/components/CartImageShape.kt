package com.foodsaver.app.presentation.featureCart.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.foodsaver.app.ui.LocalFoodSaverThemeComposition

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CartProductItemPreview() {
    LocalFoodSaverThemeComposition {
        val state = CartProductItemState(
            productName = "Qewew",
            productPrice = 120.00,
            productSize = "14''",
            productImageUri = "",
            productCount = 2,
            onIncreaseClick = { /*TODO()*/ },
            onDecreaseClick = { /*TODO()*/ },
            onRemoveClick = { /*TODO()*/ },
            isProductEditing = false
        )

        CartProductItem(state, Modifier.fillMaxWidth())
    }
}

class CartImageShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val bottomLeftTop = Size(0f, size.height - 22f)
        val bottomLeftBottom = Size(24f, size.height)
        val bottomRightTop = Size(size.width, size.height - 24f)
        val bottomRightBottom = Size(size.width - 22f, size.height)

        val leftCenter = Size(0f, bottomLeftTop.height - 3f)
        val leftTop = Size(32f, 0f)

        val rightTop = Size(size.width - 32f, 0f)
        val rightCenter = Size(size.width, bottomRightTop.height - 3f)

        val path = Path().apply {
            // creating bottom shape
            moveTo(leftTop.width, leftTop.height)
            lineTo(rightTop.width, rightTop.height)

            quadraticTo(
                x2 = rightCenter.width,
                y2 = rightCenter.height,
                x1 = size.width,
                y1 = 0f
            )

            lineTo(bottomRightTop.width, bottomRightTop.height)
            quadraticTo(
                x1 = size.width,
                y1 = size.height,
                x2 = bottomRightBottom.width,
                y2 = bottomRightBottom.height
            )

            lineTo(bottomLeftBottom.width, bottomLeftBottom.height)
            quadraticTo(
                x1 = 0f,
                y1 = size.height,
                x2 = bottomLeftTop.width,
                y2 = bottomLeftTop.height
            )

            lineTo(leftCenter.width, leftCenter.height)
            quadraticTo(
                x1 = 0f,
                y1 = 0f,
                x2 = leftTop.width,
                y2 = leftTop.height
            )
            close()
        }

        return Outline.Generic(path)
    }
}