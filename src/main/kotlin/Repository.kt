package com.example

object Movies {
    val movies = mutableListOf(
        Movie(1, "Interstellar", "Christopher Nolan", 2014),
        Movie(2, "Inception", "Christopher Nolan", 2010),
        Movie(3, "The Matrix", "Lana Wachowski", 1999)
    )
}

object Reviews {
    val reviews = mutableListOf(
        Review(1, 1, "Alice", 5, "Amazing movie!"),
        Review(2, 1, "Bob", 4, "Great visuals and story."),
        Review(3, 2, "Charlie", 5, "A masterpiece!")
    )
}
