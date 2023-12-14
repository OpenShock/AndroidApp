package com.shocklink.android.screen.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shocklink.android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDialog (
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Create Share",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { /* This will absorb clicks and prevent them from dismissing the dialog */ }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp)
                    ) {
                        // Images above checkboxes
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_bolt_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )

                            Image(
                                painter = painterResource(id = R.drawable.baseline_waves_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )

                            Image(
                                painter = painterResource(id = R.drawable.baseline_volume_up_24),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(MaterialTheme.colorScheme.primary)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Checkboxes
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            CheckboxInRow(text = "Shock")
                            CheckboxInRow(text = "Vibrate")
                            CheckboxInRow(text = "Beep")
                        }

                        // Text boxes
                        OutlinedTextField(
                            value = "",
                            onValueChange = { },
                            label = { Text("Intensity") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = "",
                            onValueChange = { },
                            label = { Text("Duration") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            singleLine = true
                        )

                        // Buttons
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    onConfirm()
                                    onCancel()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Confirm")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = onCancel,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckboxInRow(text: String) {
    var checked by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable { checked = !checked }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            modifier = Modifier.size(36.dp)
        )
    }
}
