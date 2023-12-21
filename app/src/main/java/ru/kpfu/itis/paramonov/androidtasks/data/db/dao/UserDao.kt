package ru.kpfu.itis.paramonov.androidtasks.data.db.dao

import ru.kpfu.itis.paramonov.androidtasks.data.db.entity.UserEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.paramonov.androidtasks.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userData: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<UserEntity>?

    @Query("SELECT * FROM users WHERE user_id = :userId")
    fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE user_email = :userEmail")
    fun getUserByEmail(userEmail: String): UserEntity?


    @Query("DELETE FROM users WHERE user_id = :id")
    fun deleteUserById(id: Int)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun saveUser(userEntity: UserEntity)

    @Query("SELECT * FROM users WHERE user_phone_number = :phoneNumber")
    fun getUserByPhoneNumber(phoneNumber: String): UserEntity?

    @Query("SELECT * FROM users WHERE user_email = :email AND user_password = :password")
    fun getUser(email: String, password: String): UserEntity?
}