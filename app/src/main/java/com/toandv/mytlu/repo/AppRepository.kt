package com.toandv.mytlu.repo

interface AppRepository {
    suspend fun login(userName: String, password: String): Boolean
    suspend fun refresh()
}