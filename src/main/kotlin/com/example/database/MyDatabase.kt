package com.example.database

import com.example.auth_area.mUser
import com.example.tomUser
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sqldelight.data.RemoteDataBase

class MyDatabase(dataBase: RemoteDataBase) {
    val quries = dataBase.userQueries

    fun insertUser(user: mUser){
        try {
            quries.insert_user(user.email,user.username,user.password)
        }catch (ex:Exception){
            println("ex ${ex.localizedMessage}")
        }
    }
    fun getUser(id:Long):User?{
        return try {
            quries.get_user(id).executeAsOne()
        }catch (ex:Exception){
            null
        }
    }
    fun getAllUser():List<mUser>{
        return try {
            quries.get_all_user().executeAsList().map { it.tomUser() }
        }catch (ex:Exception){
            emptyList()
        }
    }
}