package com.alexplainl8ter.digitalbookcase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

// OpenLibrary API Specific Data Classes
@Serializable
data class OpenLibrarySearchResult(
    val docs: List<OpenLibraryBookDoc>? = null
)

@Serializable
data class OpenLibraryBookDoc(
    val key: String? = null, // e.g., "/works/OL27473W" - Used to construct work URL if needed
    val title: String? = null,
    val author_name: List<String>? = null,
    val publish_date: List<String>? = null,
    val isbn: List<String>? = null, // Could be ISBN-10 or ISBN-13, or both
    val cover_i: Int? = null // Cover ID - Used to fetch cover image
)
// OpenLibrary API Service Implementation
class OpenLibraryBookAPIService(private val client: HttpClient) : BookAPIService { // Implement interface
    override suspend fun searchBooks(query: String): BookSearchResponse { // Override interface method
        return try {
            val openLibraryResult: OpenLibrarySearchResult = client.get("https://openlibrary.org/search.json") {
                parameter("q", query)
            }.body()

            val genericBookDataList = openLibraryResult.docs?.mapNotNull { openLibraryDoc ->
                val isbn13 = openLibraryDoc.isbn?.find { it.length == 13 } ?: openLibraryDoc.isbn?.firstOrNull()
                val openLibraryCoverURL = openLibraryDoc.cover_i?.let { coverId ->
                    "https://covers.openlibrary.org/b/id/$coverId-L.jpg"
                }
                val openLibraryThumbnailURL = openLibraryDoc.cover_i?.let{ coverId ->
                    "https://covers.openlibrary.org/b/id/$coverId-S.jpg"
                }
                val publishedDate = openLibraryDoc.publish_date?.firstOrNull()

                GenericBookData( // Create GenericBookData
                    source = "open_library",
                    bookId = openLibraryDoc.key,
                    title = openLibraryDoc.title,
                    authors = openLibraryDoc.author_name,
                    publishedDate = publishedDate,
                    description = null, // Description not readily available from search
                    pageCount = null, // Page count not readily available from search
                    isbn13 = isbn13,
                    coverImageUrl = openLibraryCoverURL,
                    thumbnailImageURL = TODO()
                )
            } ?: emptyList()

            // Now map GenericBookData to Book entity
            val books = genericBookDataList.map { genericData ->
                Book(
                    source = genericData.source,
                    bookId = genericData.bookId ?: genericData.title ?: "unknown_id", // Fallback for bookId
                    title = genericData.title ?: "Unknown Title",
                    authors = genericData.authors?.joinToString(", "),
                    publishedDate = genericData.publishedDate,
                    pageCount = genericData.pageCount,
                    description = genericData.description, // Will be null for OpenLibrary search in this example
                    userRating = null,
                    isbn13 = genericData.isbn13,
                    coverImageUrl = null, // Not applicable for OpenLibrary results fetched this way
                    thumbnailImageURL = genericData.coverImageUrl,
                    insertionTimestamp = System.currentTimeMillis()
                )
            }

            Log.d("BookSearchService", "OpenLibrary API Search Result Count: ${books.size}")
            BookSearchResponse.Success(books)

        } catch (e: Exception) {
            return BookSearchResponse.OtherError // Or handle more specifically if needed
        }
    }
}