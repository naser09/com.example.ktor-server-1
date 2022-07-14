package com.example.auth_area

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.authArea(){
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
    install(Authentication){
        basic ("basic-auth"){
            realm = "what is realm"
            validate { cred ->
                if (cred.name == "naser" && cred.password=="123"){
                    UserIdPrincipal(cred.name)
                }else{
                    null
                }
            }
        }

        jwt("jwt-auth"){
            realm = myRealm
            this.verifier { JWT.require(Algorithm.HMAC256(secret)).withAudience(audience).withIssuer(issuer).build() }
            validate {cred ->
                val user = cred.payload.getClaim("user").asArray(String::class.java)
                if (user[0] == "naser" && user[1]=="123"){
                    JWTPrincipal(cred.payload)
                }else{
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respondText { "got ${defaultScheme} and reaalm = $realm" }
            }
        }
    }
    routing {
        authenticate("basic-auth"){
            get("/auth"){
                call.respondText { "hello authenticated area :) " }
            }
        }
        post("/login"){
            val user = call.receive<mUser>()
            if (user.username=="naser" && user.password=="123"){
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withArrayClaim("user", arrayOf(user.username,user.password))
                    .withExpiresAt(Date(System.currentTimeMillis()+(1000*60*5)))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(mapOf("token" to token))
            }else{
                call.respondText { "failed" }
            }
        }
        authenticate("jwt-auth"){
            get("/jwt"){
                call.respondText { "jwt auth area" }
            }
        }
    }
}
@kotlinx.serialization.Serializable
data class mUser(val email:String,val username:String, val password:String)