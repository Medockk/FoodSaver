package com.foodsaver.app.presentation.featureProfile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.foodsaver.app.ui.FoodSaverTheme

@Composable
fun MenuItems(
    items: List<MenuItemState>,
    modifier: Modifier = Modifier
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = FoodSaverTheme.colorScheme.backgroundSecondary,
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Spacer(Modifier.height(20.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { item ->
                MenuItem(
                    state = item,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}