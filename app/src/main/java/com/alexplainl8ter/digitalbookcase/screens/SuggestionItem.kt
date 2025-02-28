package com.alexplainl8ter.digitalbookcase.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alexplainl8ter.digitalbookcase.BookItem

@Composable
fun SuggestionItem(suggestion: BookItem, onSuggestionClick: (BookItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onSuggestionClick(suggestion) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = suggestion.volumeInfo?.imageLinks?.smallThumbnail?.replace(
            "http://",
            "https://"
        )
        Log.d("SuggestionItem", "ImageURL: ${imageUrl}")
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Book Thumbnail",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(48.dp)
            )
        }
        else {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .height(48.dp)
            )
        }
        Column{
            Text(text = suggestion.volumeInfo?.title ?: "N/A",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = "by: ${suggestion.volumeInfo?.authors?.joinToString(", ") ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

    }
}