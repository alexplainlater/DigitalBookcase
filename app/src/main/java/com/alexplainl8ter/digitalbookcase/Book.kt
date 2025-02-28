package com.alexplainl8ter.digitalbookcase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "books", indices = [Index(value = ["bookId"], unique = true)]) // Define table name and unique index
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Auto-generated primary key
    @ColumnInfo(name = "source") val source: String?,
    @ColumnInfo(name = "bookId") val bookId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "authors") val authors: String?, // Comma-separated authors
    @ColumnInfo(name = "publishedDate") val publishedDate: String?,
    @ColumnInfo(name = "pageCount") val pageCount: Int?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "userRating") val userRating: Int?,
    @ColumnInfo(name = "isbn13") val isbn13: String?,
    @ColumnInfo(name = "coverImageUrl") val coverImageUrl: String?,
    @ColumnInfo(name = "thumbnailImageURL") val thumbnailImageURL: String?,
    @ColumnInfo(name = "insertionTimestamp") val insertionTimestamp: Long?
)