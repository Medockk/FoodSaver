@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureMain.Product

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SharedTransitionScope.ProductScreenRoot(
    productId: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {

    ProductScreen(productId, navController, animatedVisibilityScope)
}

@Composable
private fun SharedTransitionScope.ProductScreen(
    productId: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Expires",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_expiresAt_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Name",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_name_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                )
                Spacer(Modifier.height(5.dp))
                Text(text = "Desc",
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_desc_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ))

                Spacer(Modifier.height(15.dp))

                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = "100$",
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("product_cost_${productId}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                ))
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("product_btn_${productId}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    ){
                        Text("Add to cart")
                    }
                }
            }
        }
    }

}