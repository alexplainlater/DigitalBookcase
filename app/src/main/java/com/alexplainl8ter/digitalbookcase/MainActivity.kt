package com.alexplainl8ter.digitalbookcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alexplainl8ter.digitalbookcase.screens.AddBookScreen
import com.alexplainl8ter.digitalbookcase.screens.BookDetailsScreen
import com.alexplainl8ter.digitalbookcase.screens.BookcaseScreen
import com.alexplainl8ter.digitalbookcase.screens.HomeScreen
import com.alexplainl8ter.digitalbookcase.screens.SettingsScreen
import com.alexplainl8ter.digitalbookcase.ui.theme.DigitalBookcaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DigitalBookcaseTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem( // Home navigation item
                                selected = false,
                                onClick = { navController.navigate("home") },
                                icon = { Icon(imageVector = Icons.Outlined.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem( // Add Book navigation item
                                selected = false,
                                onClick = { navController.navigate("addBook") },
                                icon = { Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add Books") },
                                label = { Text("Add Books") }
                            )
                            NavigationBarItem( // Bookcase navigation item
                                selected = false,
                                onClick = { navController.navigate("bookcase") },
                                icon = { Icon(imageVector = Icons.AutoMirrored.Outlined.List, contentDescription = "Bookcase") },
                                label = { Text("Bookcase") }
                            )
                            NavigationBarItem( // Settings navigation item
                                selected = false,
                                onClick = { navController.navigate("settings") },
                                icon = { Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") }
                            )
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        startDestination = "home"
                    )
                    {
                        composable("home")
                        {
                            HomeScreen()
                        }
                        composable("bookcase")
                        {
                            BookcaseScreen(navController)
                        }
                        composable(
                            "bookDetails/{bookId}",
                            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
                        )
                        { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId")
                            BookDetailsScreen(
                                bookId = bookId,
                                snackbarHostState = snackbarHostState,
                                navController = navController
                            )
                        }
                        composable("addBook") {
                            AddBookScreen(snackbarHostState = snackbarHostState)
                        }
                        composable("settings") {
                            SettingsScreen()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DigitalBookcaseTheme {
        HomeScreen()
    }
}