package com.polendina.knounce.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.polendina.knounce.ui.theme.KnounceTheme

sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object HOME: NavigationItem(route = "home", icon = Icons.Outlined.Home, title = "Home")
    object PROFILE: NavigationItem(route = "profile", icon = Icons.Outlined.Person, title = "Profile")
}

private val navigationItems = listOf(
    NavigationItem.PROFILE,
    NavigationItem.HOME
)
@Composable
fun BottomNavigationBar() {
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        navigationItems.forEach {
            BottomNavigationItem(
                label = {
                    Text(text = it.title)
                },
                alwaysShowLabel = false,
                selected = false,
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.title
                    )
                },
                onClick = { /*TODO*/ },
            )
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    KnounceTheme {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            BottomNavigationBar()
        }
    }
}