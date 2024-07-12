package rainbow.test.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import rainbow.test.data.News

class NewsScreen(private val viewModel: MainViewModel, private val news: News) : Screen {
    @Composable
    override fun invoke() {
        Scaffold(topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(viewModel::pop, Modifier.testTag("back")) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                title = {}
            )
        }) {
            Column(Modifier.padding(it)) {
                Text("${news.id}. ${news.title}")
                Row {
                    Text(news.organizer)
                    Spacer(Modifier.weight(1f))
                    Text(news.publishDate)
                }
                Text(viewModel.newsRepository.content)
            }
        }
    }
}
