package coder.behzod.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coder.behzod.R
import de.charlex.compose.SpeedDialData
import de.charlex.compose.SpeedDialFloatingActionButton


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpeedDialFAB(
    modifier: Modifier,
    onSave: () -> Unit,
    onShare: () -> Unit
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
            speedDialData = listOf(
                SpeedDialData(
                    label = stringResource(R.string.save),
                    painterResource = R.drawable.ic_save,
                    onClick = {
                        onSave()
                    }
                ),
                SpeedDialData(
                    label = stringResource(R.string.share),
                    painterResource = R.drawable.ic_share,
                    onClick = { onShare() }
                )
            )
        )
    }
}