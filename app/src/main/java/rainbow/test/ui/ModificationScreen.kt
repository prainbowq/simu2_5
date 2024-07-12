package rainbow.test.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import rainbow.test.R

class ModificationScreen(private val viewModel: MainViewModel) : Screen {
    private var password by mutableStateOf("")
    private var passwordHidden by mutableStateOf(true)
    private var confirmation by mutableStateOf("")
    private var confirmationHidden by mutableStateOf(true)
    private var error by mutableStateOf<String?>(null)

    private fun modify() {
        error = when {
            password.run {
                length < 8
                        || !contains(Regex("\\d"))
                        || !contains(Regex("[A-Z]"))
                        || !contains(Regex("[a-z]"))
                        || length > 15
            } -> viewModel.errorsRepository.getError(2, 4)
            confirmation.run {
                length < 8
                        || !contains(Regex("\\d"))
                        || !contains(Regex("[A-Z]"))
                        || !contains(Regex("[a-z]"))
                        || length > 15
                        || !equals(password)
            } -> viewModel.errorsRepository.getError(2, 1)
            else -> null
        }
        if (error != null) return
        viewModel.user = viewModel.user!!.copy(password = password)
        viewModel.databaseRepository.database.userDao.update(viewModel.user!!)
        viewModel.pop()
    }

    private fun dismiss() {
        error = null
    }

    private fun togglePassword() {
        passwordHidden = !passwordHidden
    }

    private fun toggleConfirmation() {
        confirmationHidden = !confirmationHidden
    }

    private fun getVisualTransformation(predicate: Boolean) =
        if (predicate) PasswordVisualTransformation()
        else VisualTransformation.None

    private fun getIconId(predicate: Boolean) =
        if (predicate) R.drawable.visibility
        else R.drawable.visibility_off

    @Composable
    override fun invoke() {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(viewModel::pop) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    },
                    title = { Text("修改密碼") }
                )
            },
            modifier = Modifier.testTag("modification")
        ) {
            Column(Modifier.padding(it)) {
                TextField(
                    value = password,
                    onValueChange = ::password::set,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = getVisualTransformation(passwordHidden),
                    placeholder = { Text("密碼") },
                    trailingIcon = {
                        IconButton(::togglePassword, Modifier.testTag("password_toggle")) {
                            Icon(painterResource(getIconId(passwordHidden)), null)
                        }
                    },
                    modifier = Modifier.testTag("password")
                )
                TextField(
                    value = confirmation,
                    onValueChange = ::confirmation::set,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = getVisualTransformation(confirmationHidden),
                    placeholder = { Text("再次輸入密碼") },
                    trailingIcon = {
                        IconButton(::toggleConfirmation, Modifier.testTag("confirmation_toggle")) {
                            Icon(painterResource(getIconId(confirmationHidden)), null)
                        }
                    },
                    modifier = Modifier.testTag("confirmation")
                )
                Button(::modify) {
                    Text("修改")
                }
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
