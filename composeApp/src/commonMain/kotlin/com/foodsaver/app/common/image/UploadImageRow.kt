package com.foodsaver.app.common.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.image.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.modifier.dashedBorder
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.upload_image_view
import foodsaver.composeapp.generated.resources.upload_photo_add
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun UploadImageRow(
    uris: List<String>,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(uris) { uri ->
            AsyncImageWithShimmerLoading(
                model = uri,
                modifier = Modifier
                    .size(110.dp, 100.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Box(
                modifier = Modifier
                    .size(110.dp, 100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .dashedBorder(
                        color = FoodSaverTheme.colorScheme.onBackgroundThin.copy(.4f),
                        on = 8.dp,
                        off = 4.dp,
                        shape = RoundedCornerShape(20.dp)
                    ).clickable(
                        onClick = onUploadClick
                    ),
                contentAlignment = Alignment.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(Res.drawable.upload_image_view),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = stringResource(Res.string.upload_photo_add),
                        color = FoodSaverTheme.colorScheme.onBackgroundThin.copy(.7f),
                        style = FoodSaverTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}