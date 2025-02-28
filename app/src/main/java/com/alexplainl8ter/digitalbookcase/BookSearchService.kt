package com.alexplainl8ter.digitalbookcase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed class BookSearchResponse
{
    data class Success( val result: BookSearchResult ) : BookSearchResponse()
    object NetworkError : BookSearchResponse()
    object OtherError : BookSearchResponse()
}

class BookSearchService
{
    private val client = HttpClient( Android )
    {
        install( ContentNegotiation )
        {
            json( Json
            {
                ignoreUnknownKeys = true // Important: To handle potentially missing fields in API response
                isLenient = true // To handle relaxed JSON syntax if needed
            }
            )
        }
    }

    suspend fun searchBooks( query: String ) : BookSearchResponse
    {
        return try
        {
            val result: BookSearchResult = client.get( "https://www.googleapis.com/books/v1/volumes" )
            {
                parameter( "q", query )
                parameter( "key", BuildConfig.GOOGLE_BOOKS_API_KEY )
            }.body()

            // Extract ISBN-13 from Google Books API Response - ADDED
            val isbn13 = result.items?.firstOrNull()?.volumeInfo?.industryIdentifiers?.find {
                it.type == "ISBN_13" // Find IndustryIdentifier with type "ISBN_13"
            }?.identifier // Get the 'identifier' (ISBN-13 value) if found

            Log.d("BookSearchService", "Google Books API ISBN-13: ${isbn13 ?: "Not found"}") // Log ISBN-13 for debugging - ADDED

            BookSearchResponse.Success( result ) // Return Success with BookSearchResult if API call successful
        }
        catch( e: Exception )
        {
            // Check for network exceptions
            return if ( e is java.net.UnknownHostException || e is java.net.ConnectException )
            {
                BookSearchResponse.NetworkError // Return NetworkError for network exceptions
            } else
            {
                BookSearchResponse.OtherError // Return OtherError for other exceptions
            }
        }
    }
}

@Serializable
data class BookSearchResult
    (
    val items: List<BookItem>? = null // List can be null if no results
)

@Serializable
data class BookItem
    (
    val id: String? = null, // Property for Google Books Volume ID
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo
    (
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val imageLinks: ImageLinks? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null
)

@Serializable
data class ImageLinks
    (
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)

@Serializable
data class IndustryIdentifier
    (
    val type: String? = null,
    val identifier: String? = null
)

enum class ApiType {
    GOOGLE_BOOKS,
    OPEN_LIBRARY,
    SMART_MERGE
}