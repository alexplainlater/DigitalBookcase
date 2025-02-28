package com.alexplainl8ter.digitalbookcase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Ignore if book with same bookId already exists
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM books ORDER BY title ASC") // Query to get all books, ordered by title
    fun getAllBooks(): Flow<List<Book>> // Use Flow for asynchronous stream of data

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books WHERE bookId = :bookId") // Query to find book by bookId
    suspend fun getBookByBookId(bookId: String): Book?

}