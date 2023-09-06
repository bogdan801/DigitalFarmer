package com.bogdan801.digitalfarmer.presentation.screens.main.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bogdan801.digitalfarmer.R
import com.bogdan801.digitalfarmer.presentation.screens.main.fields.FieldsScreen
import com.bogdan801.digitalfarmer.presentation.util.DeviceOrientation
import com.bogdan801.digitalfarmer.presentation.util.DeviceType
import com.bogdan801.digitalfarmer.presentation.util.getDeviceConfiguration

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val config = LocalConfiguration.current
    val screenConfig by remember { mutableStateOf(getDeviceConfiguration(config)) }
    val navItems = remember {
        listOf(
            NavItem(
                selectedIconID = R.drawable.ic_field_filled,
                unselectedIconID = R.drawable.ic_field_outlined,
                title = "Fields",
                page = CurrentPage.Fields
            ),
            NavItem(
                selectedIconID = R.drawable.ic_calculator_filled,
                unselectedIconID = R.drawable.ic_calculator_outlined,
                title = "Calculator",
                page = CurrentPage.Calculator
            ),
            NavItem(
                selectedIconID = R.drawable.ic_settings_filled,
                unselectedIconID = R.drawable.ic_settings_outlined,
                title = "Settings",
                page = CurrentPage.Settings
            )
        )
    }
    val subScreenNavController = rememberNavController()
    val navBackStackEntry by subScreenNavController.currentBackStackEntryAsState()
    val currentRoute by remember {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if(screenConfig.type == DeviceType.Phone){
                NavigationBar(
                    modifier = if(screenConfig.orientation == DeviceOrientation.Landscape) Modifier.height(50.dp) else Modifier
                ) {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.page.name,
                            label = if(screenConfig.orientation == DeviceOrientation.Portrait){
                                { Text(text = item.title) }
                            }
                            else null,
                            onClick = {
                                when(item.page){
                                    CurrentPage.Fields -> {
                                        if(currentRoute != CurrentPage.Fields.name)
                                            subScreenNavController.popBackStack()
                                    }
                                    else -> {
                                        subScreenNavController.navigate(route = item.page.name){
                                            popUpTo(CurrentPage.Fields.name)
                                        }
                                    }
                                }
                            },
                            icon = {
                                if(screenState.currentPage == item.page){
                                    Icon(
                                        painter = painterResource(id = item.selectedIconID),
                                        contentDescription = "Fields page icon"
                                    )
                                }
                                else{
                                    Icon(
                                        painter = painterResource(id = item.unselectedIconID),
                                        contentDescription = "Fields page icon"
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier.fillMaxSize()
        ){
            if(screenConfig.type == DeviceType.Tablet){
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    containerColor = MaterialTheme.colorScheme.surface
                ){
                    Spacer(modifier = Modifier.weight(1f))
                    navItems.forEach { item ->
                        NavigationRailItem(
                            selected = currentRoute == item.page.name,
                            label = {
                                Text(text = item.title)
                            },
                            onClick = {
                                when(item.page){
                                    CurrentPage.Fields -> {
                                        if(currentRoute != CurrentPage.Fields.name)
                                            subScreenNavController.popBackStack()
                                    }
                                    else -> {
                                        subScreenNavController.navigate(route = item.page.name){
                                            popUpTo(CurrentPage.Fields.name)
                                        }
                                    }
                                }
                            },
                            icon = {
                                if(screenState.currentPage == item.page){
                                    Icon(
                                        painter = painterResource(id = item.selectedIconID),
                                        contentDescription = "Fields page icon"
                                    )
                                }
                                else{
                                    Icon(
                                        painter = painterResource(id = item.unselectedIconID),
                                        contentDescription = "Fields page icon"
                                    )
                                }
                            }
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = subScreenNavController,
                    startDestination = CurrentPage.Fields.name
                ){
                    composable(CurrentPage.Fields.name){
                        FieldsScreen(
                            modifier = Modifier.fillMaxSize(),
                            navController = subScreenNavController
                        )
                    }

                    composable(CurrentPage.Calculator.name){
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = screenState.currentPage.name + " page")
                            Text(text = "${screenConfig.type.name} / ${screenConfig.orientation.name}")
                        }
                    }

                    composable(CurrentPage.Settings.name){

                    }
                }
            }
        }
    }
}

data class NavItem(
    val selectedIconID: Int,
    val unselectedIconID: Int,
    val title: String = "",
    val page: CurrentPage = CurrentPage.Fields
)