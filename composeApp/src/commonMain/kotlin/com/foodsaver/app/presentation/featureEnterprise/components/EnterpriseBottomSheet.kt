package com.foodsaver.app.presentation.featureEnterprise.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.foodsaver.app.common.PrimaryButton
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.coreModel.model.OrganizationModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterpriseImagesModel
import com.foodsaver.app.featureEnterprises.domain.model.EnterprisesModel
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_camera_icon
import foodsaver.composeapp.generated.resources.ic_heart_icon
import org.jetbrains.compose.resources.painterResource

@Preview(showBackground = true, device = Devices.PIXEL_6_PRO, backgroundColor = 0xFFFFFFFF)
@Composable
private fun EnterpriseBottomSheetPreview() {

    Box(Modifier.fillMaxSize()) {

        EnterpriseBottomSheet(
            enterpriseModel = EnterprisesModel(
                id = "",
                latitude = 0.0,
                longitude = 0.0,
                addressName = "Chkalova 6/1",
                organization = OrganizationModel(
                    id = "",
                    organizationName = "OKEI"
                )
            ),
            images = listOf(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onAddEnterpriseClick = {},
            onMenuButtonClick = {}
        )
    }
}

@Composable
fun EnterpriseBottomSheet(
    enterpriseModel: EnterprisesModel,
    images: List<EnterpriseImagesModel>,
    onAddEnterpriseClick: () -> Unit,
    onMenuButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.Top) {
            Column(Modifier) {
                Text(
                    text = enterpriseModel.organization.organizationName,
                    color = FoodSaverTheme.colorScheme.onBackground,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = enterpriseModel.addressName,
                    color = FoodSaverTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.weight(1f))

            Image(
                painter = painterResource(Res.drawable.ic_heart_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                colorFilter = ColorFilter.tint(Color.Red)
            )
        }

        Spacer(Modifier.height(15.dp))

        if (images.isNotEmpty()) {
            Text(
                text = "Images",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp)
            )
            Spacer(Modifier.height(5.dp))
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(images) { image ->
                SubcomposeAsyncImage(
                    model = image.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(130.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    loading = {
                        Box(Modifier.fillMaxSize().shimmerEffect())
                    },
                    contentScale = ContentScale.Crop,
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .height(130.dp)
                        .width(80.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(FoodSaverTheme.colorScheme.surfaceDim.copy(0.5f))
                        .clickable(onClick = onAddEnterpriseClick)
                        .animateItem(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_camera_icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White),
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        PrimaryButton(
            text = "Menu",
            onClick = onMenuButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}