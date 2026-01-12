package com.foodsaver.app.presentation.FeatureProfile.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.domain.model.UserModel

@Composable
fun ProfileInfo(
    profile: UserModel,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            model = profile.photoUrl,
            contentDescription = null,
            loading = {
                Box(Modifier.fillMaxSize().shimmerEffect())
            },
            success = {
                SubcomposeAsyncImageContent()
            },
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(30.dp))

        Column {
            if (profile.name == null) {
                with(this@Row) {
                    Box(Modifier.height(30.dp).weight(1f).shimmerEffect())
                }
            }

            profile.name?.let { name ->
                Text(
                    text = name,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp
                )
            }

            profile.bio?.let { bio ->
                Text(
                    text = bio,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    fontSize = 14.sp
                )
            }
        }
    }
}