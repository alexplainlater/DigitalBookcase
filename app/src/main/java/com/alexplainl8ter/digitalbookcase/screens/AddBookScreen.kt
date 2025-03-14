package com.alexplainl8ter.digitalbookcase.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alexplainl8ter.digitalbookcase.ApiType
import com.alexplainl8ter.digitalbookcase.Book
import com.alexplainl8ter.digitalbookcase.BookDatabase
import com.alexplainl8ter.digitalbookcase.BookSearchResponse
import com.alexplainl8ter.digitalbookcase.BookSearchService
import com.alexplainl8ter.digitalbookcase.R
import com.alexplainl8ter.digitalbookcase.SettingsDataStoreManager
import com.alexplainl8ter.digitalbookcase.ui.theme.DigitalBookcaseTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(snackbarHostState: SnackbarHostState) {
    var searchText by remember { mutableStateOf("") }
    var searchSuggestions by remember { mutableStateOf<List<Book>>(emptyList()) } // Changed to List<Book>
    val bookSearchService = remember { BookSearchService() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val bookDao = remember { BookDatabase.getDatabase(context).bookDao() }
    val settingsDataStoreManager = remember { SettingsDataStoreManager(context) } // Get DataStoreManager

    var selectedBook by remember { mutableStateOf<Book?>(null) } // Changed to Book?
    var selectedApiType by remember { mutableStateOf(ApiType.GOOGLE_BOOKS) } // Default, will be overridden

    // Load API type from DataStore when screen is composed
    LaunchedEffect(Unit) {
        settingsDataStoreManager.apiTypeFlow.collect { apiType ->
            selectedApiType = apiType
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Book") })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange =
                {
                    searchText = it
                    coroutineScope.launch {
                        val apiService = bookSearchService.createApiService(selectedApiType) // Use selectedApiType!
                        when (val response = apiService.searchBooks(it)) {
                            is BookSearchResponse.Success -> {
                                searchSuggestions = response.result.take(5)
                            }

                            is BookSearchResponse.NetworkError -> {
                                searchSuggestions = emptyList()
                                snackbarHostState.showSnackbar(
                                    message = "No internet connection. Book search unavailable offline.",
                                    duration = SnackbarDuration.Short
                                )
                            }

                            is BookSearchResponse.OtherError -> {
                                searchSuggestions = emptyList()
                                Toast.makeText(context, "Book search error.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                },
                label = { Text("Search for Book Title or Author") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Suggestions:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                if (searchSuggestions.isEmpty()) {
                    Text("No suggestions yet. Start typing to search.")
                } else {
                    searchSuggestions.forEach { suggestion ->
                        SuggestionItem(
                            suggestion = suggestion, // SuggestionItem now takes Book
                            onSuggestionClick = { clickedBook ->
                                selectedBook = clickedBook
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Book Details Preview Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(1.dp, Color.LightGray, MaterialTheme.shapes.medium)
                    .padding(16.dp)
            ) {
                Text(text = "Book Details Preview", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                if (selectedBook != null) {
                    val book = selectedBook!!

                    // Book Cover Image in Preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = book.coverImageUrl,
                            contentDescription = "Book Cover Preview",
                            modifier = Modifier
                                .width(100.dp)
                                .height(150.dp)
                                .clip(MaterialTheme.shapes.small)
                                .padding(end = 16.dp),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_book_placeholder),
                            error = painterResource(id = R.drawable.ic_error_image)
                        )
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            val annotatedStringTitle = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Title:")
                                }
                                append(" ${book.title ?: "N/A"}") // Use generic Book properties
                            }
                            val annotatedStringAuthor = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Author(s):")
                                }
                                append(" ${book.authors ?: "N/A"}") // Use generic Book properties
                            }
                            Text(text = annotatedStringTitle)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = annotatedStringAuthor)
                        }
                    }

                } else {
                    Text(text = "Select a book from suggestions to see details preview.")
                }
            }

            Button(onClick = {
                coroutineScope.launch {
                    if (selectedBook != null) {
                        val bookToAdd = selectedBook!! // selectedBook is now Book

                        Log.d(
                            "AddBookScreen",
                            "Inserting book with:\n " +
                                    "Source: ${bookToAdd.source ?: "Not found"}\n"+
                                    "Book ID: ${bookToAdd.bookId}\n" +
                                    "Title: ${bookToAdd.title}\n" +
                                    "Authors: ${bookToAdd.authors ?: "Not found"}\n" +
                                    "Published Date: ${bookToAdd.publishedDate ?: "Not found"}\n" +
                                    "Page Count: ${bookToAdd.pageCount ?: "Not found"}\n" +
                                    "Description: ${bookToAdd.description ?: "Not found"}\n" +
                                    "User Rating: ${bookToAdd.userRating ?: "Not found"}\n" +
                                    "ISBN: ${bookToAdd.isbn13 ?: "Not found"}\n" +
                                    "Cover Image URL: ${bookToAdd.coverImageUrl ?: "Not found"}\n" +
                                    "Thumbnail Cover Image URL: ${bookToAdd.thumbnailImageURL ?: "Not found"}\n " +
                                    "Insertion Timestamp: ${bookToAdd.insertionTimestamp ?: "Not found"}"
                        )

                        bookDao.insertBook(bookToAdd)

                        snackbarHostState.showSnackbar(
                            message = "${bookToAdd.title} added to bookcase!",
                            duration = SnackbarDuration.Short
                        )
                        searchText = ""
                        searchSuggestions = emptyList()
                        selectedBook = null
                    } else {
                        Toast.makeText(context, "No book selected to add.", Toast.LENGTH_SHORT).show()
                    }
                }
            }) {
                Text("Add Book")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddBookScreenPreview() {
    DigitalBookcaseTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        AddBookScreen(snackbarHostState = snackbarHostState)
    }
}