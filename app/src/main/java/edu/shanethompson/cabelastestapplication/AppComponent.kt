package edu.shanethompson.cabelastestapplication

import dagger.Component
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationGraph {
    fun userRepo(): UserRepository
}

@Singleton
class UserRepository @Inject constructor(private val localUserDatasource: LocalUserDatasource,
                                         private val remoteUserDatasource: UserApi) {}

class LocalUserDatasource @Inject constructor() {
    suspend fun getUsers(): List<User> = listOf()
    suspend fun getUser(id: Int): User? = null
}

class UserApi @Inject constructor() {
    suspend fun getUsers(): List<User> = listOf()
    suspend fun getUser(id: Int): User? = null
}

