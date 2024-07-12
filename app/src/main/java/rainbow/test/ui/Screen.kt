package rainbow.test.ui

import androidx.compose.runtime.Composable

interface Screen {
    @Composable
    operator fun invoke()
}
