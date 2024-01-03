package com.askolds.homeinventory.featureParameter.ui.listItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SelectableParameterSetListItemRow(
    text: String,
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
        Text(
            text,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(Modifier.weight(1f))
        Checkbox(
            checked = isSelected,
            onCheckedChange = null
        )
    }
}