package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Diversity1
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AbleyCoral
import com.example.ui.theme.AbleyCoralLight
import com.example.ui.theme.AbleyInk
import com.example.ui.theme.AbleyIvory
import com.example.viewmodel.NavigationTab

@Composable
fun AbleysBottomNavigationBar(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    showSupportTab: Boolean = true,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = AbleyInk,
        windowInsets = WindowInsets.navigationBars,
        modifier = modifier.testTag("bottom_navigation_bar")
    ) {
        // Tab 1: Grow (Skills)
        NavigationBarItem(
            selected = currentTab == NavigationTab.GROW,
            onClick = { onTabSelected(NavigationTab.GROW) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.GROW) Icons.Filled.Spa else Icons.Outlined.Spa,
                    contentDescription = "Grow Skills",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Grow",
                    fontSize = 12.sp,
                    fontWeight = if (currentTab == NavigationTab.GROW) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AbleyCoral,
                selectedTextColor = AbleyCoral,
                indicatorColor = AbleyCoralLight,
                unselectedIconColor = AbleyInk.copy(alpha = 0.5f),
                unselectedTextColor = AbleyInk.copy(alpha = 0.6f)
            ),
            modifier = Modifier.testTag("nav_tab_grow")
        )

        // Tab 2: Move (Physical Activities)
        NavigationBarItem(
            selected = currentTab == NavigationTab.MOVE,
            onClick = { onTabSelected(NavigationTab.MOVE) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.MOVE) Icons.Filled.DirectionsRun else Icons.Outlined.DirectionsRun,
                    contentDescription = "Move Activities",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Move",
                    fontSize = 12.sp,
                    fontWeight = if (currentTab == NavigationTab.MOVE) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AbleyCoral,
                selectedTextColor = AbleyCoral,
                indicatorColor = AbleyCoralLight,
                unselectedIconColor = AbleyInk.copy(alpha = 0.5f),
                unselectedTextColor = AbleyInk.copy(alpha = 0.6f)
            ),
            modifier = Modifier.testTag("nav_tab_move")
        )

        // Tab 3: Story (Memories)
        NavigationBarItem(
            selected = currentTab == NavigationTab.STORY,
            onClick = { onTabSelected(NavigationTab.STORY) },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavigationTab.STORY) Icons.Filled.AutoStories else Icons.Outlined.AutoStories,
                    contentDescription = "My Story Memories",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(
                    text = "Story",
                    fontSize = 12.sp,
                    fontWeight = if (currentTab == NavigationTab.STORY) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AbleyCoral,
                selectedTextColor = AbleyCoral,
                indicatorColor = AbleyCoralLight,
                unselectedIconColor = AbleyInk.copy(alpha = 0.5f),
                unselectedTextColor = AbleyInk.copy(alpha = 0.6f)
            ),
            modifier = Modifier.testTag("nav_tab_story")
        )

        // Tab 4: Support (Additional Support) - if enabled
        if (showSupportTab) {
            NavigationBarItem(
                selected = currentTab == NavigationTab.SUPPORT,
                onClick = { onTabSelected(NavigationTab.SUPPORT) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == NavigationTab.SUPPORT) Icons.Filled.Diversity1 else Icons.Outlined.Diversity1,
                        contentDescription = "Foundational Support",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Support",
                        fontSize = 12.sp,
                        fontWeight = if (currentTab == NavigationTab.SUPPORT) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AbleyCoral,
                    selectedTextColor = AbleyCoral,
                    indicatorColor = AbleyCoralLight,
                    unselectedIconColor = AbleyInk.copy(alpha = 0.5f),
                    unselectedTextColor = AbleyInk.copy(alpha = 0.6f)
                ),
                modifier = Modifier.testTag("nav_tab_support")
            )
        }
    }
}
