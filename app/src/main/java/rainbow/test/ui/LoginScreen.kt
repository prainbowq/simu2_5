package rainbow.test.ui

import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

class LoginScreen(private val viewModel: MainViewModel) : Screen {
    private var email by mutableStateOf("")
    private var password by mutableStateOf("")
    private var error by mutableStateOf<String?>(null)

    private fun logIn() {
        val user = viewModel.databaseRepository.database.userDao.select(email)
        error = when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                viewModel.errorsRepository.getError(1, 1)
            password != user?.password ->
                viewModel.errorsRepository.getError(1, 2)
            else -> null
        }
        if (error != null) return
        viewModel.user = user
        viewModel.pop()
        viewModel.push(HomeScreen(viewModel))
    }

    private fun dismiss() {
        error = null
    }

    @Composable
    override fun invoke() {
        Column {
            Text("Login")
            TextField(
                value = email,
                onValueChange = ::email::set,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                placeholder = { Text("Email") },
                modifier = Modifier.testTag("email")
            )
            TextField(
                value = password,
                onValueChange = ::password::set,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("密碼") },
                modifier = Modifier.testTag("password")
            )
            Button(::logIn) {
                Text("登入")
            }
            TextButton({ viewModel.push(SignupScreen(viewModel)) }) {
                Text("註冊")
            }
        }
        if (error != null) AlertDialog(
            onDismissRequest = ::dismiss,
            text = { Text(error ?: "") },
            confirmButton = {
                TextButton(::dismiss) {
                    Text("確認")
                }
            }
        )
    }
}
