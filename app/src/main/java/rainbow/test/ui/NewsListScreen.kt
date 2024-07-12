package rainbow.test.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import rainbow.test.data.News

class NewsListScreen(private val viewModel: MainViewModel) : Screen {
    private val orderList = listOf("編號", "發佈日期", "瀏覽數")
    private var keyword by mutableStateOf("")
    private var order by mutableStateOf(orderList.first())
    private var descending by mutableStateOf(true)
    private var newsList by mutableStateOf(emptyList<News>())

    init {
        updateNewsList()
    }

    private fun search() {
        newsList = newsList.filter { it.title.contains(keyword, ignoreCase = true) }
    }

    private fun changeOrder(value: String) {
        order = value
        descending = order != value || !descending
        updateNewsList()
    }

    private fun updateNewsList() {
        newsList = viewModel.newsRepository.newsList.run {
            if (descending) when (order) {
                "編號" -> sortedByDescending { it.id }
                "發佈日期" -> sortedByDescending { it.publishDate }
                "瀏覽數" -> sortedByDescending { it.views }
                else -> throw IllegalStateException("Impossible.")
            } else when (order) {
                "編號" -> sortedBy { it.id }
                "發佈日期" -> sortedBy { it.publishDate }
                "瀏覽數" -> sortedBy { it.views }
                else -> throw IllegalStateException("Impossible.")
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun invoke() {
        Scaffold(topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(viewModel::pop) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                title = { Text("最新消息") }
            )
        }) { padding ->
            Column(Modifier.padding(padding)) {
                Row {
                    TextField(
                        value = keyword,
                        onValueChange = ::keyword::set,
                        placeholder = { Text("Search") },
                        modifier = Modifier.testTag("keyword")
                    )
                    Button(::search) {
                        Icon(Icons.Default.Search, null, Modifier.testTag("search"))
                    }
                }
                Row {
                    orderList.forEach {
                        OutlinedButton(
                            onClick = { changeOrder(it) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (order == it) {
                                    MaterialTheme.colors.primary
                                } else {
                                    MaterialTheme.colors.onSurface
                                        .copy(alpha = ContentAlpha.disabled)
                                }
                            )
                        ) {
                            Text(it)
                        }
                    }
                }
                LazyColumn(Modifier.testTag("list")) {
                    items(newsList) {
                        Card({ viewModel.push(NewsScreen(viewModel, it)) }) {
                            Column {
                                Text(
                                    text = "${it.id}. ${it.title}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(it.organizer)
                                Row {
                                    Text(it.publishDate)
                                    Text("觀看次數：${it.views}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
