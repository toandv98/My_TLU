package com.toandv.mytlu.data

interface AppRepository {
    suspend fun login(userName: String, password: String): Boolean
    suspend fun refresh()
}