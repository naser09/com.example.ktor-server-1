package com.example.auth_area

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.authArea(){
    install(Authentication){
        basic {
        }
    }
    
}