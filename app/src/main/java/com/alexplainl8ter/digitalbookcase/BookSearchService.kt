package com.alexplainl8ter.digitalbookcase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

sealed class BookSearchResponse {
    data class Success(val result: List<Book>) : BookSearchResponse()
    object NetworkError : BookSearchResponse()
    object OtherError : BookSearchResponse()
}

enum class ApiType {
    GOOGLE_BOOKS,
    OPEN_LIBRARY,
    SMART_MERGE // Placeholder for future smart merge
}

// Intermediate, more generic data structure (not tied to any specific API or database entity)
data class GenericBookData(
    val source: String, // "google_books", "open_library", etc.
    val bookId: String?, // API-specific ID if available
    val title: String?,
    val authors: List<String>?, // List of authors
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val isbn13: String?,
    val coverImageUrl: String?, // Generic cover image URL (could be from Google or OpenLibrary)
    val thumbnailImageURL: String? //Smaller image to show in search results
    // Add other potentially common fields here as needed (e.g., pageCount, publisher, etc.)
)

class BookSearchService {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    fun createApiService(apiType: ApiType): BookAPIService { // Factory method
        return when (apiType) {
            ApiType.GOOGLE_BOOKS -> GoogleBookAPIService(client)
            ApiType.OPEN_LIBRARY -> OpenLibraryBookAPIService(client)
            ApiType.SMART_MERGE -> SmartMergeBookApiService() // Placeholder - see next step
        }
    }

    private fun handleSearchError(e: Exception): BookSearchResponse {
        return if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
            BookSearchResponse.NetworkError
        } else {
            Log.e("BookSearchService", "Search Error", e)
            BookSearchResponse.OtherError
        }
    }
}

// Placeholder for Smart Merge Service - Create a new file SmartMergeBookApiService.kt if you plan to implement this
class SmartMergeBookApiService : BookAPIService {
    override suspend fun searchBooks(query: String): BookSearchResponse {
        Log.d("SmartMergeService", "Smart Merge Search Not Implemented Yet")
        return BookSearchResponse.OtherError
    }
}