package com.alexplainl8ter.digitalbookcase

import android.util.Log

class SmartMergeBookAPIService : BookAPIService {
    override suspend fun searchBooks(query: String): BookSearchResponse {
        Log.d("SmartMergeService", "Smart Merge Search Not Implemented Yet")
        return BookSearchResponse.OtherError
    }
}