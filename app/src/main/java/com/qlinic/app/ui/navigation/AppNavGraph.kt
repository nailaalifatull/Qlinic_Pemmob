package com.qlinic.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.qlinic.app.ui.screens.HistoryScreen
import com.qlinic.app.ui.screens.HomeScreen
import com.qlinic.app.ui.screens.LoginScreen
import com.qlinic.app.ui.screens.MonitoringScreen
import com.qlinic.app.ui.screens.ProfileScreen
import com.qlinic.app.ui.screens.RegistrationScreen
import com.qlinic.app.ui.screens.SignUpScreen
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.viewmodel.AuthViewModel
import com.qlinic.app.viewmodel.QueueViewModel

sealed class BottomNavRoute(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavRoute("home", "Beranda", Icons.Default.Home)
    object Queue : BottomNavRoute("queue", "Antrean", Icons.Default.Assignment)
    object History : BottomNavRoute("history", "Riwayat", Icons.Default.History)
    object Profile : BottomNavRoute("profile", "Profil", Icons.Default.Person)
}

val bottomNavItems = listOf(
    BottomNavRoute.Home,
    BottomNavRoute.Queue,
    BottomNavRoute.History,
    BottomNavRoute.Profile
)

// ═══════════════════════════════════════════════════════════════════════════════
// Top-level nav host: handles auth check + auth flow + main app
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun QlinicNavHost() {
    val authViewModel: AuthViewModel = viewModel()
    val topNavController = rememberNavController()

    // Splash/auto-login: check Firebase Auth session synchronously at startup
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "main" else "login"

    NavHost(navController = topNavController, startDestination = startDestination) {

        // ── Login ──────────────────────────────────────────────────────────────
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    topNavController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    topNavController.navigate("register")
                }
            )
        }

        // ── Register ───────────────────────────────────────────────────────────
        composable("register") {
            SignUpScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    topNavController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    topNavController.popBackStack()
                }
            )
        }

        // ── Main App ───────────────────────────────────────────────────────────
        composable("main") {
            MainAppScreen(
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    topNavController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Main app: bottom-nav scaffold (unchanged structure, plus onLogout threading)
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun MainAppScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val viewModel: QueueViewModel = viewModel()

    // Queue sub-navigation state
    var queueRoute by remember { mutableStateOf("registration") }

    Scaffold(
        bottomBar = {
            val backStack by navController.currentBackStackEntryAsState()
            val current = backStack?.destination?.route
            NavigationBar(containerColor = Color.White) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = current == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = BluePrimary.copy(alpha = 0.15f))
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavRoute.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavRoute.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToQueue = {
                        queueRoute = "registration"
                        navController.navigate(BottomNavRoute.Queue.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToHistory = {
                        navController.navigate(BottomNavRoute.History.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(BottomNavRoute.Profile.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            composable(BottomNavRoute.Queue.route) {
                // Queue sub-flow: registration → monitoring
                when (queueRoute) {
                    "registration" -> RegistrationScreen(
                        viewModel = viewModel,
                        onConfirmed = { queueRoute = "monitoring" },
                        onNavigateToProfile = {
                            navController.navigate(BottomNavRoute.Profile.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    "monitoring" -> MonitoringScreen(
                        viewModel = viewModel,
                        onCancelled = { queueRoute = "registration" },
                        onNavigateToHistory = {
                            queueRoute = "registration"
                            viewModel.resetTicketEndState()
                            navController.navigate(BottomNavRoute.History.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
            composable(BottomNavRoute.History.route) { HistoryScreen(viewModel = viewModel) }
            composable(BottomNavRoute.Profile.route) {
                ProfileScreen(viewModel = viewModel, onLogout = onLogout)
            }
        }
    }
}
