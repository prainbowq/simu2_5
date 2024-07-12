package rainbow.test.data

import androidx.room.*

@Entity("users")
data class User(
    val name: String,
    @PrimaryKey val email: String,
    val password: String
) {
    @androidx.room.Dao
    interface Dao {
        @Insert
        fun insert(user: User)

        @Query("SELECT * FROM users WHERE email = :email")
        fun select(email: String): User?

        @Update
        fun update(user: User)

        @Query("DELETE FROM users")
        fun deleteAll()
    }
}
