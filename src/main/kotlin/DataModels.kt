package com.example

import kotlinx.serialization.Serializable

@Serializable
data class Movie(val id: Int, val title: String, val director: String, val releaseYear: Int)

@Serializable
data class Review(val id: Int, val movieID: Int, val reviewerName: String, val rating: Int, val comment: String)

@Serializable
data class ReviewsWithAvg(val reviews: List<Review>, val averageRating: Double)
