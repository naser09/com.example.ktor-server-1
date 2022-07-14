package com.example

import com.example.auth_area.authArea
import com.example.auth_area.mUser
import com.example.database.MyDatabase
import io.ktor.server.application.*
import com.example.plugins.*
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import data.User
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import sqldelight.data.RemoteDataBase

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val driver :SqlDriver = JdbcSqliteDriver("jdbc:sqlite:server.db")
//    RemoteDataBase.Schema.create(driver)
    val database = MyDatabase(RemoteDataBase(driver))
    val scope = CoroutineScope(this.coroutineContext)
    routing {
        post("/test/create"){
            val user = call.receive<mUser>()
            database.insertUser(user)
            call.respond(HttpStatusCode.OK)
        }
        get("/test"){
//            val json = Json.encodeToString(database.getAllUser())
            call.respond(status = HttpStatusCode.OK,database.getAllUser())
        }
    }
    authArea()
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureSecurity()
}
fun User.tomUser():mUser{
    return mUser(email, username, password)
}
