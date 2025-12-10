@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.foodsaver.app.presentation.FeatureMain.Product

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.foodsaver.app.common.shimmerEffect
import com.foodsaver.app.domain.model.ProductModel
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.ProductScreenRoot(
    productId: String,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: PVM = koinViewModel()
) {

    viewModel.loadProduct(productId)
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let {
        ProductScreen(
            productId = productId,
            state = it,
            navController = navController,
            animatedVisibilityScope = animatedVisibilityScope
        )
    }
}

@Composable
private fun SharedTransitionScope.ProductScreen(
    productId: String,
    state: ProductModel,
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
                    text = state.expiresAt,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_expiresAt_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        )
                )
                Spacer(Modifier.height(5.dp))

                SubcomposeAsyncImage(
                    model = state.photoUrl,
                    contentDescription = null,
                    loading = {
                        Box(Modifier.fillMaxSize().shimmerEffect())
                    },
                    success = {
                        SubcomposeAsyncImageContent()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = state.name,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_name_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            },
                            renderInOverlayDuringTransition = false
                        ),
                )
                Spacer(Modifier.height(5.dp))
                Text(text = state.description,
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("product_desc_${productId}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = { _, _ ->
                                tween()
                            }
                        ))

                Spacer(Modifier.height(15.dp))

                Row {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = "${state.costUnit} ${state.cost.roundToInt()}",
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState("product_cost_${productId}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = { _, _ ->
                                        tween()
                                    }
                                ))
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("product_btn_${productId}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween()
                                }
                            )
                    ){
                        Text("Add to cart")
                    }
                }
            }
        }
    }

}