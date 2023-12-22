package ru.kpfu.itis.paramonov.androidtasks.data.db.dao

import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.paramonov.androidtasks.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    fun getUserByEmail(userEmail: String): List<UserEntity>

    @Query("DELETE FROM users WHERE user_id = :id")
    fun deleteUserById(id: Int)

    @Query("SELECT * FROM users WHERE user_password = :userPassword AND user_id = :userId")
    fun getUserWithPassword(userId: Int, userPassword: String): List<UserEntity>

    @Query("UPDATE users SET user_password = :password WHERE user_id = :userId")
    fun updateUserPassword(password: String, userId: Int)

    @Query("UPDATE users SET user_phone_number = :phoneNumber WHERE user_id = :userId")
    fun updateUserPhoneNumber(phoneNumber: String, userId: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE user_phone_number = :phoneNumber")
    fun getUserByPhoneNumber(phoneNumber: String): List<UserEntity>

    @Query("SELECT * FROM users WHERE user_email = :email AND user_password = :password")
    fun getUser(email: String, password: String): UserEntity?
}