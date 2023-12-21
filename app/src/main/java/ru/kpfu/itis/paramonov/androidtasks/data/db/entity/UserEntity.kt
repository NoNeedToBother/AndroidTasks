package ru.kpfu.itis.paramonov.androidtasks.data.db.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.kpfu.itis.paramonov.androidtasks.model.User

@Entity(tableName = "users",
    indices = [Index(value = ["user_phone_number", "user_email"], unique = true)])
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id")
    val id: Int = 0,
    @ColumnInfo(name = "user_name")
    val name: String,
    @ColumnInfo(name = "user_email")
    val emailAddress: String,
    @ColumnInfo(name = "user_phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "user_password")
    val password: String,
) {
    companion object {
        fun getEntity(user: User): UserEntity {
            return UserEntity(
                name = user.name,
                emailAddress = user.email,
                phoneNumber = user.phoneNumber,
                password = user.password
            )
        }
    }
}