package rainbow.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import rainbow.test.data.DatabaseRepository
import rainbow.test.data.ErrorsRepository
import rainbow.test.data.NewsRepository
import rainbow.test.ui.MainViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<MainViewModel> {
            MainViewModel.Factory(
                DatabaseRepository(this),
                ErrorsRepository(assets.open("2.錯誤訊息庫_dev.json").readBytes().decodeToString()),
                NewsRepository(assets.open("1.最新消息.json").readBytes().decodeToString())
            )
        }
        setContent {
            Box(Modifier.semantics { testTagsAsResourceId = true }) {
                AnimatedContent(viewModel.screen) {
                    it?.invoke() ?: finish()
                }
                BackHandler(onBack = viewModel::pop)
            }
        }
    }
}
