package com.alexplainl8ter.digitalbookcase.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.alexplainl8ter.digitalbookcase.Book
import com.alexplainl8ter.digitalbookcase.BookDatabase
import com.alexplainl8ter.digitalbookcase.R.drawable.ic_book_placeholder
import com.alexplainl8ter.digitalbookcase.R.drawable.ic_error_image
import com.alexplainl8ter.digitalbookcase.ui.theme.DigitalBookcaseTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@Composable
fun BookcaseScreen(navController: NavController) {
    val context = LocalContext.current
    val bookDao = remember { BookDatabase.getDatabase(context).bookDao() }
    val booksFlow: Flow<List<Book>> = bookDao.getAllBooks()
    val books by booksFlow.collectAsState(initial = emptyList())

    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Delay for 3 seconds (adjust as needed)
            if (books.isNotEmpty()) {
                val nextIndex = (lazyListState.firstVisibleItemIndex + 1) % books.size
                lazyListState.animateScrollToItem(index = nextIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    )
    {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "My Bookcase",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(620.dp)
                .padding(horizontal = 16.dp)
                .background(Color.Black),
            horizontalArrangement = Arrangement.spacedBy(4.dp), // Spacing between book covers

            state = lazyListState,
            contentPadding = PaddingValues(start = 90.dp, end = 386.dp)
        ) {
            items(count = books.size) { index ->
                val book = books[index]
                val isFocusedItem = index == lazyListState.firstVisibleItemIndex // Determine if it's the "focused" item

                if (isFocusedItem) {
                    Log.d(
                        "BookcaseScreen",
                        "Showing main image book with:\n " +
                                "Title: ${book.title}\n" +
                                "Authors: ${book.authors ?: "Not found"}\n" +
                                "Published Date: ${book.publishedDate ?: "Not found"}\n" +
                                "ISBN: ${book.isbn13 ?: "Not found"}\n" +
                                "Cover Image URL: ${book.coverImageUrl ?: "Not found"}"
                    )
                }

                AsyncImage(
                    model = book.coverImageUrl,
                    contentDescription = "Book Cover of ${book.title}",
                    modifier = Modifier
                        .width(if (isFocusedItem) 386.dp else 258.dp)
                        .height(if (isFocusedItem) 600.dp else 400.dp)
                        .absoluteOffset( y= if(isFocusedItem) 10.dp else 105.dp )
                        .animateContentSize(
                            animationSpec = tween(
                                durationMillis = 500, // Animation duration in milliseconds
                                easing = LinearOutSlowInEasing // Easing function for smooth start and end
                            )
                        )
                        .clip(MaterialTheme.shapes.medium)
                        .shadow(elevation = if (isFocusedItem) 8.dp else 4.dp)
                        .graphicsLayer { // Apply rotation based on focus - No rotation for focused, 30f for left, -30f for right
                            rotationY = if (isFocusedItem)
                                            0f
                                        else if (index < lazyListState.firstVisibleItemIndex)
                                            30f
                                        else
                                            -30f
                        }
                        .clickable {
                            navController.navigate("bookDetails/${book.bookId}")
                        },
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = ic_book_placeholder),
                    error = painterResource(id = ic_error_image)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun BookcaseScreenPreview() {
    DigitalBookcaseTheme {
        BookcaseScreen(navController = NavController(LocalContext.current))
    }
}