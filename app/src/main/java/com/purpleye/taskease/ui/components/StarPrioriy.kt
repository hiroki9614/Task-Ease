package com.purpleye.taskease.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.purpleye.taskease.R

@Composable
fun StarPriority(
    priority: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(modifier) {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= priority) Icons.Default.Star else Icons.Default.Star,
                contentDescription = "Star $i",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { onValueChange(i) },
                tint = if (i <= priority) colorResource(id = R.color.star_yellow) else Color.Gray
            )
        }
    }
}