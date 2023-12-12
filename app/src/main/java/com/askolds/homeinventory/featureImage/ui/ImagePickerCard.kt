package com.askolds.homeinventory.featureImage.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ImagePickerCard(
    imageText: String,
    image: @Composable () -> Unit,
    imageAction: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        shape = CardDefaults.outlinedShape,
        modifier = modifier
            .fillMaxWidth()
    ) {
//        val cameraLauncher = rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.TakePicture()
//        ) {test, uri: Uri? ->
//            if (uri != null)
//                imageAction(uri)
//        }
        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null)
                imageAction(uri)
        }
        Row(
            modifier
                .padding(8.dp)
                .height(IntrinsicSize.Min)
        ) {
            image.invoke()
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(7f)
            ) {
                Text(
                    imageText,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    data class IconItem(
                        val onClick: () -> Unit,
                        val imageVector: ImageVector,
                        val contentDescription: String?
                    )
                    val items = listOf(
                        IconItem(
                            { /* TODO */ },
                            Icons.Outlined.CameraAlt,
                            "Take picture"
                        ),
                        IconItem(
                            { galleryLauncher.launch("image/*") },
                            Icons.Outlined.Image,
                            "Pick image from gallery"
                        ),
                        IconItem(
                            { imageAction(null) },
                            Icons.Outlined.Delete,
                            "Delete image"
                        ),
                    )
                    items.forEach {item ->
                        IconButton(
                            onClick = item.onClick,
                        ) {
                            Icon(
                                imageVector = item.imageVector,
                                contentDescription = item.contentDescription,
                                Modifier
                                    .size(24.dp)
                            )
                        }
                    }

                }
            }
        }
    }
}