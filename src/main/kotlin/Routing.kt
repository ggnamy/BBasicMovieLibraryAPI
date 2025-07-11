package com.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        route("/movies") {
            get("/search") {
                val title = call.request.queryParameters["title"]?.lowercase()
                val director = call.request.queryParameters["director"]?.lowercase()
                val results = Movies.movies.filter { movie ->
                    (title != null && movie.title.lowercase().contains(title)) ||
                            (director != null && movie.director.lowercase().contains(director))
                }
                if (results.isEmpty()) call.respondText("No movies found", HttpStatusCode.NotFound)
                else call.respond(results)
            }

            get { call.respond(Movies.movies) }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val movie = Movies.movies.find { it.id == id }
                if (movie != null) call.respond(movie)
                else call.respondText("Movie not found", HttpStatusCode.NotFound)
            }

            post {
                val request = call.receive<Movie>()
                val newId = (Movies.movies.maxByOrNull { it.id }?.id ?: 0) + 1
                Movies.movies.add(request.copy(id = newId))
                call.respondText("Movie created", HttpStatusCode.Created)
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val index = id?.let { Movies.movies.indexOfFirst { it.id == it } } ?: -1
                if (index >= 0) {
                    val request = call.receive<Movie>()
                    Movies.movies[index] = request.copy(id = id!!)
                    call.respondText("Movie updated", HttpStatusCode.OK)
                } else call.respondText("Movie not found", HttpStatusCode.NotFound)
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null && Movies.movies.removeIf { it.id == id }) {
                    call.respondText("Movie deleted", HttpStatusCode.OK)
                } else call.respondText("Movie not found", HttpStatusCode.NotFound)
            }

            get("/{movieId}/reviews") {
                val movieId = call.parameters["movieId"]?.toIntOrNull()
                val reviews = Reviews.reviews.filter { it.movieID == movieId }
                val avg = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0
                call.respond(ReviewsWithAvg(reviews, avg))
            }
        }

        route("/reviews") {
            get { call.respond(Reviews.reviews) }
            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val review = Reviews.reviews.find { it.id == id }
                if (review != null) call.respond(review)
                else call.respondText("Review not found", HttpStatusCode.NotFound)
            }
            post {
                val req = call.receive<Review>()
                if (!Movies.movies.any { it.id == req.movieID }) {
                    call.respondText("Movie not found", HttpStatusCode.BadRequest)
                } else {
                    val newId = (Reviews.reviews.maxByOrNull { it.id }?.id ?: 0) + 1
                    Reviews.reviews.add(req.copy(id = newId))
                    call.respondText("Review created", HttpStatusCode.Created)
                }
            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                val idx = id?.let { Reviews.reviews.indexOfFirst { it.id == it } } ?: -1
                if (idx >= 0) {
                    val req = call.receive<Review>()
                    Reviews.reviews[idx] = req.copy(id = id!!)
                    call.respondText("Review updated", HttpStatusCode.OK)
                } else call.respondText("Review not found", HttpStatusCode.NotFound)
            }
            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null && Reviews.reviews.removeIf { it.id == id }) {
                    call.respondText("Review deleted", HttpStatusCode.OK)
                } else call.respondText("Review not found", HttpStatusCode.NotFound)
            }
        }
    }
}
