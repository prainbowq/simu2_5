package rainbow.test.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class UserScreen(private val viewModel: MainViewModel) : Screen {
    private fun logOut() {
        viewModel.user = null
        viewModel.pop()
        viewModel.pop()
        viewModel.push(LoginScreen(viewModel))
    }

    @Composable
    override fun invoke() {
        Scaffold(topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(viewModel::pop) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                title = { Text("個人資訊") }
            )
        }) {
            Column(Modifier.padding(it)) {
                if (viewModel.user != null) {
                    Text("姓名")
                    Text(viewModel.user!!.name)
                    Text("Email")
                    Text(viewModel.user!!.email)
                }
                Button({ viewModel.push(ModificationScreen(viewModel)) }) {
                    Text("修改密碼")
                }
                Button(::logOut) {
                    Text("登出")
                }
            }
        }
    }
}
