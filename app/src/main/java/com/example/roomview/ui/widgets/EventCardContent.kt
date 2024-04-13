package com.example.roomview.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.roomview.R
import com.example.roomview.model.Event
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun EventCardContent(
    event: Event, modifier: Modifier = Modifier
) {
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    val date = inputFormat.parse(event.eventDate)?.let { outputFormat.format(it) }

    Row {
        if (event.imageUrl != "") {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(event.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Image(
                painterResource(id = R.drawable.placeholder),
                contentDescription = "Placeholder",
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .size(100.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Text(
                text = event.title,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column {
                Text(
                    text = event.eventLocation,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (event.isDeleted) {
                    Text(
                        text = "Cancelled",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        color = MaterialTheme.colorScheme.error
                    )
                } else  {
                    if (date != null) {
                        Text(
                            text = date,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun HouseTypeCard(
    houseType: HouseType
) {
    Row(
        modifier = Modifier.padding(all = 12.dp)
    ) {
        Icon(
            houseType.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            houseType.description,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
    }
}