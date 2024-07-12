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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import rainbow.test.R
import rainbow.test.data.User

class SignupScreen(private val viewModel: MainViewModel) : Screen {
    private var name by mutableStateOf("")
    private var email by mutableStateOf("")
    private var password by mutableStateOf("")
    private var passwordHidden by mutableStateOf(true)
    private var confirmation by mutableStateOf("")
    private var confirmationHidden by mutableStateOf(true)
    private var error by mutableStateOf<String?>(null)

    private fun signUp() {
        error = when {
            name.run { isEmpty() || length > 10 } -> viewModel.errorsRepository.getError(2, 2)
            email.run {
                isEmpty()
                        || length > 30
                        || !Patterns.EMAIL_ADDRESS.matcher(this).matches()
            } -> viewModel.errorsRepository.getError(2, 3)
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
        viewModel.databaseRepository.database.userDao.insert(User(name, email, password))
        viewModel.pop()
    }

    private fun togglePassword() {
        passwordHidden = !passwordHidden
    }

    private fun toggleConfirmation() {
        confirmationHidden = !confirmationHidden
    }

    private fun dismiss() {
        error = null
    }

    private fun getVisualTransformation(predicate: Boolean) =
        if (predicate) PasswordVisualTransformation()
        else VisualTransformation.None

    private fun getIconId(predicate: Boolean) =
        if (predicate) R.drawable.visibility
        else R.drawable.visibility_off

    @Composable
    override fun invoke() {
        Column {
            Text("Register")
            TextField(
                value = name,
                onValueChange = ::name::set,
                placeholder = { Text("姓名") },
                modifier = Modifier.testTag("name")
            )
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
            Button(::signUp) {
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
