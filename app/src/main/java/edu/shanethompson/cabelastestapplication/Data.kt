package edu.shanethompson.cabelastestapplication

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") val uid: Long,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String
)

@Entity
data class Pet(
    @PrimaryKey val petId: Long,
    @ColumnInfo(name = "owner") val owner: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: String
)

@Dao
interface PetDao {
    @Query("SELECT * FROM pet")
    fun getAll(): List<Pet>

    @Query("SELECT * FROM pet WHERE petId IN (:petIds)")
    fun loadAllByIds(petIds: IntArray): List<Pet>

    @Insert
    fun insertAll(vararg pets: Pet)

    @Delete
    fun delete(pet: Pet)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Database(entities = [User::class, Pet::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun petDao(): PetDao
}