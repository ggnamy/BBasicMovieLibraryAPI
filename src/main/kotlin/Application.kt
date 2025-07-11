package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.Serializable
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        movieRoutes()
    }
}

@Serializable
data class Movie(val id: Int, val title: String, val director: String, val releaseYear: Int)

val movies = mutableListOf<Movie>()
val idCounter = AtomicInteger(1)

fun Route.movieRoutes() {
    route("/movies") {
        get {
            call.respond(movies)
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val movie = movies.find { it.id == id }
            if (movie == null) {
                call.respond("Movie not found")
            } else {
                call.respond(movie)
            }
        }
        post {
            val movie = call.receive<Movie>()
            val movieWithId = movie.copy(id = idCounter.getAndIncrement())
            movies.add(movieWithId)
            call.respond(movieWithId)
        }
        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond("Invalid id")
                return@put
            }
            val updatedMovie = call.receive<Movie>()
            val index = movies.indexOfFirst { it.id == id }
            if (index == -1) {
                call.respond("Movie not found")
            } else {
                movies[index] = updatedMovie.copy(id = id)
                call.respond(movies[index])
            }
        }
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val removed = movies.removeIf { it.id == id }
            if (removed) {
                call.respond("Deleted")
            } else {
                call.respond("Movie not found")
            }
        }
    }
}
