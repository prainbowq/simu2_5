package rainbow.test.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeScreen(private val viewModel: MainViewModel) : Screen {
    private lateinit var scope: CoroutineScope
    private val newsList = viewModel.newsRepository.newsList.take(5)
    private val scaffoldState = ScaffoldState(DrawerState(DrawerValue.Closed), SnackbarHostState())

    private fun openDrawer() {
        scope.launch {
            scaffoldState.drawerState.open()
        }
    }

    private fun closeDrawer() {
        scope.launch {
            scaffoldState.drawerState.close()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun invoke() {
        scope = rememberCoroutineScope()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(::openDrawer, Modifier.testTag("open_drawer")) {
                            Icon(Icons.Default.Menu, null)
                        }
                    },
                    title = {},
                    actions = {
                        IconButton(
                            onClick = { viewModel.push(UserScreen(viewModel)) },
                            modifier = Modifier.testTag("go_user")
                        ) {
                            Icon(Icons.Default.Person, null)
                        }
                    }
                )
            },
            drawerContent = {
                Surface({ viewModel.push(NewsListScreen(viewModel)) }, Modifier.testTag("drawer")) {
                    ListItem {
                        Text("最新消息")
                    }
                }
                Spacer(Modifier.weight(1f))
                IconButton(::closeDrawer, Modifier.testTag("close_drawer")) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        ) {
            Column(Modifier.padding(it)) {
                Text("News")
                newsList.forEach {
                    Surface({ viewModel.push(NewsScreen(viewModel, it)) }) {
                        ListItem {
                            Text(
                                text = "${it.id}. ${it.title}",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}
