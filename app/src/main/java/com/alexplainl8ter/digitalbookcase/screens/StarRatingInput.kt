package com.alexplainl8ter.digitalbookcase.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingInput(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row {
        (1..5).forEach { starIndex ->
            Icon(
                imageVector = if (starIndex <= rating) Icons.Filled.Star else Icons.TwoTone.Star,
                contentDescription = "Star $starIndex",
                modifier = Modifier
                    .clickable { onRatingChanged(starIndex) }
                    .padding(4.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}