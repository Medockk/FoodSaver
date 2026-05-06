package com.foodsaver.app.presentation.featureFoodDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.foodsaver.app.common.asyncImage.AsyncImageWithShimmerLoading
import com.foodsaver.app.common.button.PrimaryFabButton
import com.foodsaver.app.common.collapsingToolbar.CollapsingTitle
import com.foodsaver.app.common.collapsingToolbar.CollapsingToolbarImage
import com.foodsaver.app.common.collapsingToolbar.rememberToolbarScrollBehavior
import com.foodsaver.app.common.collapsingToolbar.rememberToolbarScrollState
import com.foodsaver.app.common.restaurant.RestaurantSpecifications
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailEvents
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailState
import com.foodsaver.app.featureFoodDetail.presentation.productDetail.FoodDetailViewModel
import com.foodsaver.app.presentation.featureFoodDetails.components.FoodDetailsBottomBar
import com.foodsaver.app.presentation.featureFoodDetails.components.IngredientView
import com.foodsaver.app.presentation.featureFoodDetails.components.SizeView
import com.foodsaver.app.ui.FoodSaverTheme
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.back_icon
import foodsaver.composeapp.generated.resources.favorite_icon
import foodsaver.composeapp.generated.resources.ingredients
import foodsaver.composeapp.generated.resources.selected_favorite_icon
import foodsaver.composeapp.generated.resources.size
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoodDetailsScreenRoot(
    navController: NavController,
    viewModel: FoodDetailViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    FoodDetailsScreen(
        state = state,
        onEvent = viewModel::onEvent,
        navController = navController
    )
}

@Composable
private fun FoodDetailsScreen(
    state: FoodDetailState,
    onEvent: (FoodDetailEvents) -> Unit,
    navController: NavController,
) {
    val listState = rememberLazyListState()
    val toolbarScrollState = rememberToolbarScrollState()
    val scrollBehavior = rememberToolbarScrollBehavior(toolbarScrollState)

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CollapsingToolbarImage(
                modifier = Modifier
                    .fillMaxWidth(),
                navigationIcon = {
                    PrimaryFabButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.back_icon),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    PrimaryFabButton(
                        onClick = {
                            TODO()
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isFavoriteProduct) vectorResource(Res.drawable.selected_favorite_icon)
                            else vectorResource(Res.drawable.favorite_icon),
                            contentDescription = null
                        )
                    }
                },
                collapsingImage = { progress ->
                    val shape = RoundedCornerShape(
                        lerp(
                            0f, 50f, progress
                        ).toInt()
                    )

                    AsyncImageWithShimmerLoading(
                        model = state.product?.imageUris,
                        modifier = Modifier.fillMaxSize()
                            .clip(shape),
                        contentScale = ContentScale.Crop
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f),
                contentPadding = PaddingValues(
                    horizontal = 24.dp
                )
            )
            {
                // product name + restaurant logo
                item {
                    Spacer(Modifier.height(25.dp))
                    Text(
                        text = state.product?.name ?: "",
                        style = FoodSaverTheme.typography.bodyBold,
                        color = FoodSaverTheme.colorScheme.onBackground
                    )

                    state.restaurant?.let { restaurant ->
                        Spacer(Modifier.height(7.dp))
                        Row {
                            state.restaurantDetails?.let { restaurantDetails ->
                                // TODO add restaurant logo
                                AsyncImageWithShimmerLoading(
                                    model = restaurantDetails.logoUri,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(Modifier.width(10.dp))
                            }

                            Text(
                                text = restaurant.name,
                                color = FoodSaverTheme.colorScheme.onBackground,
                                style = FoodSaverTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // restaurant specifications
                item {
                    Spacer(Modifier.height(20.dp))
                    RestaurantSpecifications(
                        rating = state.restaurant?.rating,
                        deliveryCost = state.restaurant?.deliveryCost,
                        averageDeliveryTime = state.restaurant?.averageDeliveryTime,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // food description
                item {
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = state.product?.description ?: "",
                        style = FoodSaverTheme.typography.bodyRegular,
                        color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                        textAlign = TextAlign.Justify
                    )
                }

                // food sizes
                item {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(Res.string.size).uppercase(),
                            color = FoodSaverTheme.colorScheme.onBackgroundTertiary,
                            style = FoodSaverTheme.typography.bodyRegular
                        )
                        Spacer(Modifier.width(10.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            itemsIndexed(state.foodSizes) { index, size ->
                                SizeView(
                                    size = size.size,
                                    isSelected = index == state.selectedSizeIndex,
                                    onClick = {
                                        TODO()
                                    }
                                )
                            }
                        }
                    }
                }

                // ingredients
                item {
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = stringResource(Res.string.ingredients).uppercase(),
                        color = FoodSaverTheme.colorScheme.onBackgroundSubtitle,
                        style = FoodSaverTheme.typography.bodyRegular
                    )

                    Spacer(Modifier.height(20.dp))

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        state.ingredients.forEach { ingredient ->
                            IngredientView(
                                ingredient = ingredient
                            )
                        }
                    }
                }
            }

            state.product?.let { product ->
                FoodDetailsBottomBar(
                    price = product.price,
                    productCount = state.productCount.toInt(),
                    onIncreaseClick = { TODO() },
                    onDecreaseClick = { TODO() },
                    onAddProductToCart = { TODO() },
                    modifier = Modifier
                        .background(FoodSaverTheme.colorScheme.placeholderBackground)
                )
            }
        }
    }

//    Scaffold(
//        modifier = Modifier
//            .nestedScroll(scrollBehavior.nestedScrollConnection),
//        topBar = {
//            CustomToolbar(
//                navigationIcon = {
//                    PrimaryFabButton(onClick = { navController.navigateUp() }) {
//                        Icon(vectorResource(Res.drawable.back_icon), null)
//                    }
//                },
//                actions = {
//                    PrimaryFabButton(onClick = {
//                        // TODO
//                    }) {
//                        Icon(
//                            imageVector = if (state.isFavoriteProduct)
//                                vectorResource(Res.drawable.selected_favorite_icon)
//                            else vectorResource(Res.drawable.favorite_icon),
//                            contentDescription = null
//                        )
//                    }
//                },
//                centralContent = {
//                    AsyncImageWithShimmerLoading(
//                        model = state.product?.imageUris,
//                        modifier = Modifier.fillMaxSize()
//                    )
//                },
//                collapsingTitle = CollapsingTitle(
//                    titleText = state.product?.name ?: "",
//                    expandedTextStyle = FoodSaverTheme.typography.headerMedium
//                ),
//                scrollBehavior = scrollBehavior,
//            )
//        }
//    ) { paddingValues ->
//

//    }
}

@Composable
fun CollapsingImage(
    progress: Float,
    image: @Composable () -> Unit
) {
    // 📐 размеры
    val expandedHeight = 240.dp
    val collapsedSize = 40.dp

    val screenWidth = LocalWindowInfo.current.containerDpSize.width

    val width = lerp(screenWidth, collapsedSize, progress)
    val height = lerp(expandedHeight, collapsedSize, progress)

    // 📍 позиция
    val startX = lerp(0.dp, 72.dp, progress)
    val startY = lerp(0.dp, 16.dp, progress)

    // 🔵 форма
    val corner = lerp(0.dp, 100.dp, progress)

    Box(
        modifier = Modifier
            .offset(x = startX, y = startY)
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(corner))
    ) {
        image()
    }
}

private fun getNavigationIconSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.backNavigationMode) {
        BackNavigationMode.BackArrow -> {
            { Icon(vectorResource(Res.drawable.back_icon), null) }
        }

        BackNavigationMode.None -> {
            null
        }
    }
}

private fun getActionsSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable RowScope.() -> Unit)? {
    return when (toolbarDemoSettings.actionMode) {
        ActionMode.Icon -> {
            {
                Icon(vectorResource(Res.drawable.favorite_icon), null)
            }
        }

        ActionMode.Text -> {
            {
                Text("Button")
            }
        }

        ActionMode.None -> {
            null
        }
    }
}

private fun getCentralContentSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.centralContentMode) {
        CentralContentMode.ProgressBar -> {
            {
                // progress-bar style widget
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = Color.Gray)
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .background(color = Color.Red)
                    )
                }
            }
        }

        CentralContentMode.Title -> {
            {
                Text(text = "Screen title", style = MaterialTheme.typography.titleLarge)
            }
        }

        CentralContentMode.TitleSubtitle -> {
            {
                Column {
                    Text(text = "Screen title", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Subtitle", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        CentralContentMode.None -> {
            null
        }
    }
}

@Composable
private fun getCollapsingTitle(toolbarDemoSettings: ToolbarDemoSettings): CollapsingTitle? {
    return when (toolbarDemoSettings.collapsingTitleMode) {
        CollapsingTitleMode.SectionTitle -> {
            CollapsingTitle.large("Section title")
        }

        CollapsingTitleMode.SubsectionTitle -> {
            CollapsingTitle.medium("Subsection title")
        }

        CollapsingTitleMode.SectionTitleMultiLine -> {
            CollapsingTitle.large("Section title with large multiline text")
        }

        CollapsingTitleMode.SubsectionTitleMultiLine -> {
            CollapsingTitle.medium("Subsection title with large multiline text")
        }

        CollapsingTitleMode.None -> {
            null
        }
    }
}

private fun getAdditionalContentSlot(toolbarDemoSettings: ToolbarDemoSettings): (@Composable () -> Unit)? {
    return when (toolbarDemoSettings.additionalContentMode) {
        AdditionalContentMode.Tabs -> {
            {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TabRow(
                        selectedTabIndex = 0,
                        tabs = {
                            Tab(text = { Text("Favorites") }, selected = true, onClick = { })
                            Tab(text = { Text("Subscriptions") }, selected = false, onClick = { })
                        }
                    )
                }
            }
        }

        AdditionalContentMode.None -> {
            null
        }
    }
}

@Composable
private fun MutableState<ToolbarDemoSettings>.ToolbarDemoSettingsRadio(
    name: String,
    isSelected: (ToolbarDemoSettings) -> Boolean,
    onSelect: (ToolbarDemoSettings) -> ToolbarDemoSettings,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { value = onSelect(value) }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        RadioButton(selected = isSelected(value), onClick = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ToolbarDemoSettingsTitle(text: String) {
    Text(
        modifier = Modifier.padding(16.dp),
        text = text,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun ActionsContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Actions content")
        ToolbarDemoSettingsRadio(
            name = "Icon",
            isSelected = { it.actionMode == ActionMode.Icon },
            onSelect = { it.copy(actionMode = ActionMode.Icon) }
        )
        ToolbarDemoSettingsRadio(
            name = "Text",
            isSelected = { it.actionMode == ActionMode.Text },
            onSelect = { it.copy(actionMode = ActionMode.Text) }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.actionMode == ActionMode.None },
            onSelect = { it.copy(actionMode = ActionMode.None) }
        )
    }
}

@Composable
private fun BackNavigationSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Back navigation content")
        ToolbarDemoSettingsRadio(
            name = "Back Navigation",
            isSelected = { it.backNavigationMode == BackNavigationMode.BackArrow },
            onSelect = { it.copy(backNavigationMode = BackNavigationMode.BackArrow) }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.backNavigationMode == BackNavigationMode.None },
            onSelect = { it.copy(backNavigationMode = BackNavigationMode.None) }
        )
    }
}

@Composable
private fun AdditionalContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Additional content")
        ToolbarDemoSettingsRadio(
            name = "Tabs",
            isSelected = { it.additionalContentMode == AdditionalContentMode.Tabs },
            onSelect = {
                it.copy(additionalContentMode = AdditionalContentMode.Tabs)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.additionalContentMode == AdditionalContentMode.None },
            onSelect = { it.copy(additionalContentMode = AdditionalContentMode.None) }
        )
    }
}

@Composable
private fun CentralContentSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Central content")
        ToolbarDemoSettingsRadio(
            name = "Title",
            isSelected = { it.centralContentMode == CentralContentMode.Title },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.Title)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Title + Subtitle",
            isSelected = { it.centralContentMode == CentralContentMode.TitleSubtitle },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.TitleSubtitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "ProgressBar",
            isSelected = { it.centralContentMode == CentralContentMode.ProgressBar },
            onSelect = {
                it.copy(centralContentMode = CentralContentMode.ProgressBar)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.centralContentMode == CentralContentMode.None },
            onSelect = { it.copy(centralContentMode = CentralContentMode.None) }
        )
    }
}

@Composable
private fun CollapsingTitleSettings(toolbarDemoSettingsState: MutableState<ToolbarDemoSettings>) {
    with(toolbarDemoSettingsState) {
        ToolbarDemoSettingsTitle("Collapsing title")
        ToolbarDemoSettingsRadio(
            name = "Section title",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SectionTitle },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SectionTitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Subsection title",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SubsectionTitle },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SubsectionTitle)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Section title multiline",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SectionTitleMultiLine },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SectionTitleMultiLine)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "Subsection title multiline",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.SubsectionTitleMultiLine },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.SubsectionTitleMultiLine)
            }
        )
        ToolbarDemoSettingsRadio(
            name = "None",
            isSelected = { it.collapsingTitleMode == CollapsingTitleMode.None },
            onSelect = {
                it.copy(collapsingTitleMode = CollapsingTitleMode.None)
            }
        )
    }
}

private fun LazyListScope.scrollableItemsForSample() {
    for (i in 0..100) {
        item("scroll_test_$i") {
            Text(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                text = "Item for scroll testing #$i"
            )
        }
    }
}

private enum class BackNavigationMode {
    BackArrow, None
}

private enum class ActionMode {
    Icon, Text, None
}

private enum class CentralContentMode {
    ProgressBar, Title, TitleSubtitle, None
}

private enum class CollapsingTitleMode {
    SectionTitle, SubsectionTitle, SectionTitleMultiLine, SubsectionTitleMultiLine, None
}

private enum class AdditionalContentMode {
    Tabs, None
}

@Serializable
private data class ToolbarDemoSettings(
    val backNavigationMode: BackNavigationMode = BackNavigationMode.BackArrow,
    val actionMode: ActionMode = ActionMode.Icon,
    val centralContentMode: CentralContentMode = CentralContentMode.None,
    val collapsingTitleMode: CollapsingTitleMode = CollapsingTitleMode.SectionTitle,
    val additionalContentMode: AdditionalContentMode = AdditionalContentMode.None,
)