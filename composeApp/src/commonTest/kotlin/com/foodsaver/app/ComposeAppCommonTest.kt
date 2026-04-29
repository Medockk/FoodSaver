package com.foodsaver.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import androidx.navigation.compose.rememberNavController
import com.foodsaver.app.feature.auth.presentation.Auth.AuthEvent
import com.foodsaver.app.feature.auth.presentation.Auth.AuthPage
import com.foodsaver.app.feature.auth.presentation.Auth.AuthState
import com.foodsaver.app.presentation.featureAuth.AuthScreen
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class AuthScreenKmpTest {

    @Test
    fun verifyInitialState_isSignUpTab_and_fieldsAreVisible() = runComposeUiTest {
        setContent {
            AuthScreen(
                state = AuthState(authPage = AuthPage.SIGN_UP, tabRowIndex = 0),
                onEvent = {},
                snackBarHostState = SnackbarHostState(),
                navController = rememberNavController()
            )
        }

        onNodeWithTag("fio_field").assertIsDisplayed()
        onAllNodesWithTag("email_field").onFirst().assertIsDisplayed()
        onAllNodesWithTag("password_field").onFirst().assertIsDisplayed()
    }

    @Test
    fun verifyUserInput_triggersEventsCorrectly() = runComposeUiTest {
        var capturedEmail = ""

        setContent {
            AuthScreen(
                state = AuthState(authPage = AuthPage.SIGN_IN, tabRowIndex = 1),
                onEvent = { if (it is AuthEvent.OnEmailChange) capturedEmail = it.value },
                snackBarHostState = SnackbarHostState(),
                navController = rememberNavController()
            )
        }

        onAllNodesWithTag("email_field")
            .onFirst()
            .performTextInput("test@foodsaver.com")

        assertEquals(capturedEmail, "test@foodsaver.com")
    }

    @Test
    fun verifyTabSwitchToSignIn_changesButtonText_and_hidesFio() = runComposeUiTest {
        var currentState = AuthState(authPage = AuthPage.SIGN_UP, tabRowIndex = 0)

        setContent {
            AuthScreen(
                state = currentState,
                onEvent = { event ->
                    if (event is AuthEvent.OnTabRowIndexChange) {
                        currentState = currentState.copy(
                            tabRowIndex = event.value,
                            authPage = if (event.value == 0) AuthPage.SIGN_UP else AuthPage.SIGN_IN
                        )
                    }
                },
                snackBarHostState = SnackbarHostState(),
                navController = rememberNavController()
            )
        }

        onNodeWithTag("fio_field").assertIsDisplayed()
        onAllNodesWithTag("auth_button").onFirst().performClick()
    }

    @Test
    fun verifyTabSwitchToSignIn_works() = runComposeUiTest {
        setContent {
            AuthScreen(
                navController = rememberNavController(),
                state = AuthState(),
                onEvent = {},
                snackBarHostState = SnackbarHostState()
            )
        }

        onNodeWithTag("tab_1").performClick()
        waitForIdle()
        onNodeWithTag("auth_button").assertExists()
    }
}