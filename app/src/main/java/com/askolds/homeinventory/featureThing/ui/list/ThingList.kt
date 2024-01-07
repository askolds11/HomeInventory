package com.askolds.homeinventory.featureThing.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.getSelectableClickModifier
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem

fun LazyListScope.thingItems(
    thingList: List<ThingListItem>,
    isAnySelected: Boolean,
    selectItem: (id: Int, selected: Boolean, index: Int) -> Unit,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    itemsIndexed(items = thingList, key = { _, thing -> thing.id }) { index, thing ->
        val clickModifier = Modifier.getSelectableClickModifier(
            isAnySelected = isAnySelected,
            onLongClick = { selectItem(thing.id, !thing.selected, index) },
            onClickWhenSelected = { selectItem(thing.id, !thing.selected, index) },
            onClickWhenNotSelected = { navigateToThing(thing.homeId, thing.id)}
        )
        ThingListItemRow(
            imageUri = thing.imageUri,
            text = thing.name,
            isContainer = thing.isContainer,
            isSelected = thing.selected,
            modifier = modifier.then(clickModifier)
        )
        if (index < thingList.lastIndex) {
            Divider(color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun ThingListItemRow(
    imageUri: String?,
    text: String,
    isContainer: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val selectedModifier =
        if (isSelected)
            Modifier.background(selectedColor)
        else
            Modifier

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(selectedModifier)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(44.dp),
    ) {
        val imageModifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                RoundedCornerShape(8.dp)
            )
        if (imageUri == null) {
            Image(
                imageVector =
                if (isContainer)
                    ImageVector.vectorResource(R.drawable.package_2_outlined)
                else
                    Icons.Outlined.Hardware,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = imageModifier.padding(8.dp)
            )
        } else {
            AsyncImage(
                model = imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
                modifier = imageModifier
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Text(
            text,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}