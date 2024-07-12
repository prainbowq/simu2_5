package rainbow.test.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database([User::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract val userDao: User.Dao
}
