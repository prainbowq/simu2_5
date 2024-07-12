package rainbow.test.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rainbow.test.data.DatabaseRepository
import rainbow.test.data.ErrorsRepository
import rainbow.test.data.NewsRepository
import rainbow.test.data.User

class MainViewModel(
    val databaseRepository: DatabaseRepository,
    val errorsRepository: ErrorsRepository,
    val newsRepository: NewsRepository
) : ViewModel() {
    var user by mutableStateOf<User?>(null)

    private val screens = mutableStateListOf<Screen>(LoginScreen(this))
    val screen get() = screens.lastOrNull()

    fun push(value: Screen) {
        screens += value
    }

    fun pop() {
        screens.removeLast()
    }

    class Factory(
        private val databaseRepository: DatabaseRepository,
        private val errorsRepository: ErrorsRepository,
        private val newsRepository: NewsRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            MainViewModel(databaseRepository, errorsRepository, newsRepository) as T
    }
}
