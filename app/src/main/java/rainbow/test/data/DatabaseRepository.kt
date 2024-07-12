package rainbow.test.data

import android.content.Context
import androidx.room.Room

class DatabaseRepository(context: Context) {
    val database = Room.databaseBuilder(context, MainDatabase::class.java, "main.db")
        .allowMainThreadQueries()
        .build()

    companion object {
        lateinit var database: MainDatabase
    }

    init {
        Companion.database = database
    }
}
