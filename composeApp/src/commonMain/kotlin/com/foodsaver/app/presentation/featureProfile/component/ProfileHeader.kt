package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.coreProfile.domain.model.ProfileModel
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun ProfileHeader(
    profile: ProfileModel,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImageWithShimmerLoading(
            model = profile.imageUri,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.width(32.dp))

        Column {
            Text(
                text = profile.fullName,
                color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                style = FoodSaverTheme.typography.bodyBold
            )
            profile.bio?.let { bio ->
                Spacer(Modifier.height(10.dp))
                Text(
                    text = bio,
                    color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                    style = FoodSaverTheme.typography.bodySmall
                )
            }
        }
    }
}