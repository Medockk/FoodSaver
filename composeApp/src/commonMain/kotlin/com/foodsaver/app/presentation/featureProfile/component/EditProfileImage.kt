package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.image.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.pen_icon
import org.jetbrains.compose.resources.vectorResource

@Composable
fun EditProfileImage(
    image: Any?,
    onChangeImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(modifier) {
        if (image is String) {
            AsyncImageWithShimmerLoading(
                model = image,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape)
            )
        }

        if (image is ImageBitmap) {
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        IconButton(
            onClick = onChangeImageClick,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = FoodSaverTheme.colorScheme.primary
            ),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.pen_icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(13.dp)
                    .size(16.dp),
                tint = Color.White
            )
        }
    }
}