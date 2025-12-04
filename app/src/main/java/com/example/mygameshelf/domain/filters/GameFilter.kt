package com.example.mygameshelf.domain.filters

data class GameFilter(
    val nameQuery: String? = null,
    val genre: String? = null,
    val platform: String? = null,
    val minRating: Double? = null,
    val maxPrice: Double? = null
)

class GameFilterBuilder {

    private var nameQuery: String? = null
    private var genre: String? = null
    private var platform: String? = null
    private var minRating: Double? = null
    private var maxPrice: Double? = null

    fun setNameQuery(query: String?): GameFilterBuilder = apply {
        nameQuery = query?.takeIf { it.isNotBlank() }
    }

    fun setGenre(value: String?): GameFilterBuilder = apply {
        genre = value?.takeIf { it.isNotBlank() }
    }

    fun setPlatform(value: String?): GameFilterBuilder = apply {
        platform = value?.takeIf { it.isNotBlank() }
    }

    fun setMinRating(value: Double?): GameFilterBuilder = apply {
        minRating = value
    }

    fun setMaxPrice(value: Double?): GameFilterBuilder = apply {
        maxPrice = value
    }

    fun build(): GameFilter =
        GameFilter(
            nameQuery = nameQuery,
            genre = genre,
            platform = platform,
            minRating = minRating,
            maxPrice = maxPrice
        )
}
