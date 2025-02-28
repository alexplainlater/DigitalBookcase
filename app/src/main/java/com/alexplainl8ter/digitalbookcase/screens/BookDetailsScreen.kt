package com.alexplainl8ter.digitalbookcase.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.alexplainl8ter.digitalbookcase.Book
import com.alexplainl8ter.digitalbookcase.BookDatabase
import com.alexplainl8ter.digitalbookcase.R
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(
    bookId: String?,
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    val context = LocalContext.current
    val bookDao = remember { BookDatabase.getDatabase(context).bookDao() }
    var bookState by remember { mutableStateOf<Book?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var userRatingState by remember { mutableStateOf(0) }

    LaunchedEffect(bookId) {
        if (bookId != null) {
            coroutineScope.launch {
                val book = bookDao.getBookByBookId(bookId)
                bookState = book
                userRatingState = book?.userRating ?: 0
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Book Details",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (bookState != null) {
                val book = bookState!!

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Book Cover Image (Left side in Row)
                    AsyncImage(
                        model = book.googleCoverImageUrl,
                        contentDescription = "Book Cover of ${book.title}",
                        modifier = Modifier
                            .width(150.dp)
                            .height(220.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .padding(end = 24.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_book_placeholder),
                        error = painterResource(id = R.drawable.ic_error_image)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "By: ${book.authors ?: "Unknown"}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    bookState?.let { bookToDelete ->
                                        bookDao.deleteBook(bookToDelete)

                                        snackbarHostState.showSnackbar(
                                            message = "${bookToDelete.title} removed from bookcase!",
                                            duration = SnackbarDuration.Short
                                        )

                                        navController.popBackStack()
                                    }
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Book")
                            Text("Remove from Bookcase")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Published:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = book.publishedDate ?: "Unknown",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Description:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = book.description ?: "No description available",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .heightIn( 0.dp, 150.dp )
                        .verticalScroll(rememberScrollState())
                )

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // User Star Rating Input
                Text(text = "Your Rating:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                StarRatingInput(
                    rating = userRatingState,
                    onRatingChanged = { newRating ->
                        userRatingState = newRating
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = {
                    coroutineScope.launch {
                        bookState?.let { existingBook ->
                            val updatedBook = existingBook.copy(
                                userRating = userRatingState
                            )
                            bookDao.insertBook(updatedBook)

                            snackbarHostState.showSnackbar(
                                message = "${updatedBook.title} rating saved!",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }) {
                    Text("Save Changes")
                }

            } else {
                Text(text = "Loading book details...", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}