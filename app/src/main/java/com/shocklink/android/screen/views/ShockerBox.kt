package com.shocklink.android.screen.views

import CircularSlider
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.shocklink.android.R
import com.shocklink.android.api.models.Hub.ControlType
import com.shocklink.android.api.models.Shocker
import com.shocklink.android.ui.theme.ShockLinkAndroidTheme
import java.text.DecimalFormat


@Composable
fun ShockerBox(
    context: Context,
    shocker: Shocker,
    ownShocker: Boolean,
    onEventClicked: (shocker: Shocker, mode: ControlType, duration: UInt, intensity: Byte) -> Unit,
    onPauseClicked: (id: String, pause: Boolean) -> Unit) {
    val displayMetrics = context.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density
    var expanded by remember { mutableStateOf(false) }
    var duration: UInt = 300u
    var intensity: Byte = 1
    if(shocker != null) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(4.dp),
                    text = shocker.name,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        "Menu"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.align(
                        Alignment.TopEnd
                    ),
                    offset = DpOffset((-500).dp, 0.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Share") },
                        onClick = { /*TODO*/ },
                        leadingIcon = { Icon(Icons.Filled.Share, "Share", tint = Color.White) })
                    DropdownMenuItem(
                        text = { if (shocker.isPaused) Text("Unpause") else Text("Pause") },
                        onClick = {
                            onPauseClicked(shocker.id, !shocker.isPaused); expanded = false
                        },
                        leadingIcon = { Icon(Icons.Filled.PlayArrow, "Pause", tint = Color.White) })
                    DropdownMenuItem(
                        text = { Text("Logs") },
                        onClick = { /*TODO*/ },
                        leadingIcon = { Icon(Icons.Filled.List, "Show Logs", tint = Color.White) })
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { /*TODO*/ },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Edit,
                                "Edit Shocker",
                                tint = Color.White
                            )
                        })
                    DropdownMenuItem(
                        text = { Text("Remove") },
                        onClick = { /*TODO*/ },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Delete,
                                "Remove Shocker/Share",
                                tint = Color.White
                            )
                        })
                }
            }
            if (!shocker.isPaused) {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .background(color = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Column() {
                                CircularSlider(
                                    modifier = Modifier
                                        .size(((dpWidth / 2) - 8).dp),
                                    maxValue = 100f,
                                    minValue = 1f,
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    progressColor = MaterialTheme.colorScheme.primary,
                                    onChange = { fl: Float ->
                                        intensity = fl.toInt().toByte()
                                    }
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    text = "INTENSITY",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column() {
                                CircularSlider(
                                    modifier = Modifier
                                        .size(((dpWidth / 2) - 8).dp)
                                        .align(Alignment.End),
                                    maxValue = 30f,
                                    minValue = 0.3f,
                                    numbersAfterComma = 1,
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    progressColor = MaterialTheme.colorScheme.primary,
                                    onChange = { fl: Float ->
                                        duration = roundNumberAndReturnUInt(fl)
                                    }
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    text = "DURATION",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .wrapContentWidth()
                                .align(Alignment.CenterHorizontally)
                        ) {

                            IconButton(
                                onClick = {
                                    onEventClicked(
                                        shocker,
                                        ControlType.Beep,
                                        duration,
                                        intensity
                                    )
                                },
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                enabled = shocker.permSound || ownShocker
                            ) {
                                Icon(
                                    painterResource(R.drawable.baseline_volume_up_24),
                                    "Beep",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEventClicked(
                                        shocker,
                                        ControlType.Vibrate,
                                        duration,
                                        intensity
                                    )
                                },
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                enabled = shocker.permVibrate || ownShocker
                            ) {
                                Icon(
                                    painterResource(R.drawable.baseline_waves_24),
                                    "Vibrate",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            IconButton(
                                onClick = {
                                    onEventClicked(
                                        shocker,
                                        ControlType.Shock,
                                        duration,
                                        intensity
                                    )
                                },
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(15.dp)
                                    ),
                                enabled = shocker.permShock || ownShocker
                            ) {
                                Icon(
                                    painterResource(R.drawable.baseline_bolt_24),
                                    "Shock",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(4.dp),
                        text = "IS PAUSED",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

fun roundNumberAndReturnUInt(value: Float): UInt {
    val formatter = DecimalFormat("0.0")
    return (formatter.format(value).toFloat() * 1000).toUInt()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES,showBackground = true)
@Composable
fun Preview() {
    ShockLinkAndroidTheme {
        //ShockerBox(LocalContext.current,)
    }
}