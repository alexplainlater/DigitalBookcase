package com.alexplainl8ter.digitalbookcase

interface BookAPIService {
    suspend fun searchBooks(query: String): BookSearchResponse
}