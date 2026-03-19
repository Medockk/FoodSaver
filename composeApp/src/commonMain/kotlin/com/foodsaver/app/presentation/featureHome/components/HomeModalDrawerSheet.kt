package com.foodsaver.app.presentation.featureHome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.foodsaver.app.navigationModule.Route
import com.foodsaver.app.ui.FoodSaverTheme
import com.foodsaver.app.presentation.featureEnterprise.MapKit
import foodsaver.composeapp.generated.resources.Res
import foodsaver.composeapp.generated.resources.ic_personal_info_icon
import foodsaver.composeapp.generated.resources.profile
import foodsaver.composeapp.generated.resources.settings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeModalDrawerSheet(
    navController: NavController,
    modalDrawerState: DrawerState,
    roles: List<String>,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val closeDrawer = {
        coroutineScope.launch {
            modalDrawerState.close()
        }
    }

    ModalDrawerSheet(
        modifier = modifier,
        drawerContainerColor = FoodSaverTheme.colorScheme.background,
        drawerContentColor = FoodSaverTheme.colorScheme.onBackground
    ) {
        Column(Modifier.padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "General",
                modifier = Modifier
                    .padding(16.dp),
                color = FoodSaverTheme.colorScheme.onBackground
            )
            HorizontalDivider()

            NavigationDrawerItem(
                shape = RoundedCornerShape(5.dp),
                label = {
                    Text(
                        text = stringResource(Res.string.profile),
                    )
                },
                icon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                            closeDrawer()
                        }
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_personal_info_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                selected = false,
                onClick = {
                    navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                    closeDrawer()
                }
            )
            NavigationDrawerItem(
                label = {
                    Text(
                        text = stringResource(Res.string.settings),
                    )
                },
                icon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                            closeDrawer()
                        }
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_personal_info_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                selected = false,
                onClick = {
                    navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                    closeDrawer()
                }
            )

            if (MapKit.isMapKitSupported) {
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "MapKit",
                        )
                    },
                    icon = {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.MainGraph.MapScreen())
                                closeDrawer()
                            }
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_personal_info_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(Route.MainGraph.MapScreen())
                        closeDrawer()
                    }
                )
            }

            if (roles.contains("ADMIN")) {
                Text(text = "Additional", Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Edit products",
                        )
                    },
                    icon = {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.ProfileGraph.ProfileMenuScreen)
                                closeDrawer()
                            }
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_personal_info_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    selected = false,
                    onClick = {
                        closeDrawer()
                    }
                )

                HorizontalDivider()

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Add product",
                        )
                    },
                    icon = {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.MainGraph.AddProductScreen)
                                closeDrawer()
                            }
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.ic_personal_info_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(Route.MainGraph.AddProductScreen)
                        closeDrawer()
                    }
                )
            }
        }
    }
}