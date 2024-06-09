package coder.behzod.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.charlex.compose.SpeedDialData
import de.charlex.compose.SpeedDialFloatingActionButton


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeedDialFAB(
    modifier: Modifier,
    labelFirst:String,
    labelSecond:String,
    painterFirst:Int,
    painterSecond:Int,
    onClickFirst: () -> Unit,
    onClickSecond: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SpeedDialFloatingActionButton(
            showLabels = true,
            onClick = {

            },
            /* Button Save */
            speedDialData = listOf(
                SpeedDialData(
                    label = labelFirst,
                    painterResource = painterFirst,
                    onClick = {
                        onClickFirst()
                    }
                ),

                /* Button Share and Save */
                SpeedDialData(
                    label = labelSecond,
                    painterResource = painterSecond,
                    onClick = {
                        onClickSecond()
                    }
                )
            )
        )
    }
}