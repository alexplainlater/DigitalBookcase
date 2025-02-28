package com.alexplainl8ter.digitalbookcase

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

// Google Books API Specific Data Classes
@Serializable
data class BookSearchResult(
    val items: List<BookItem>? = null // List can be null if no results
)

@Serializable
data class BookItem(
    val id: String? = null, // Property for Google Books Volume ID
    val volumeInfo: VolumeInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val pageCount: Int? = null,
    val imageLinks: ImageLinks? = null,
    val industryIdentifiers: List<IndustryIdentifier>? = null
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = null,
    val thumbnail: String? = null
)

@Serializable
data class IndustryIdentifier(
    val type: String? = null,
    val identifier: String? = null
)

// Google Books API Service Implementation
class GoogleBookAPIService(private val client: HttpClient) : BookAPIService { // Implement interface
    override suspend fun searchBooks(query: String): BookSearchResponse { // Override interface method
        return try {
            val googleResult: BookSearchResult = client.get("https://www.googleapis.com/books/v1/volumes") {
                parameter("q", query)
                parameter("key", BuildConfig.GOOGLE_BOOKS_API_KEY)
            }.body()

            val genericBookDataList = googleResult.items?.mapNotNull { googleBookItem ->
                val volumeInfo = googleBookItem.volumeInfo ?: return@mapNotNull null
                val isbn13Identifier = volumeInfo.industryIdentifiers?.find { it.type == "ISBN_13" }
                val isbn13 = isbn13Identifier?.identifier

                GenericBookData( // Create GenericBookData
                    source = "google_books",
                    bookId = googleBookItem.id,
                    title = volumeInfo.title,
                    authors = volumeInfo.authors,
                    publishedDate = volumeInfo.publishedDate,
                    description = volumeInfo.description,
                    pageCount = volumeInfo.pageCount,
                    isbn13 = isbn13,
                    coverImageUrl = volumeInfo.imageLinks?.thumbnail,
                    thumbnailImageURL = volumeInfo.imageLinks?.smallThumbnail
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
                    description = genericData.description,
                    userRating = null,
                    isbn13 = genericData.isbn13,
                    coverImageUrl = genericData.coverImageUrl?.replace(
                        "http://",
                        "https://"
                    ),
                    thumbnailImageURL = genericData.thumbnailImageURL?.replace(
                        "http://",
                        "https://"
                    )
                    , insertionTimestamp = System.currentTimeMillis()
                )
            }

            Log.d("BookSearchService", "Google Books API Search Result Count: ${books.size}")
            BookSearchResponse.Success(books)

        } catch (e: Exception) {
            return BookSearchResponse.OtherError // Or handle more specifically if needed
        }
    }
}