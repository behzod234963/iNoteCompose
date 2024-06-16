package coder.behzod.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.viewModels.SettingsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceButtonRow(
    items: List<Pair<String, Int>>,
    onItemSelected: (Int) -> Unit,
    themeColor: Color,
    fontColor: Color,
    fontSize:Int,
    sharedPrefs: SharedPreferenceInstance,
    viewModel: SettingsViewModel = hiltViewModel()
) {

    val themeIndex = remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Int) {
        delay(100L)
        viewModel.getIndex()
        themeIndex.intValue = sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)
    }
    SingleChoiceSegmentedButtonRow {
        items.forEachIndexed { index, resource ->
            SegmentedButton(

                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = themeColor,
                    activeContentColor = Color.Red,
                    inactiveContainerColor = themeColor,
                    activeBorderColor = fontColor,
                    inactiveBorderColor = fontColor
                ),
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = items.size
                ),
                onClick = {
                    themeIndex.intValue = index
                    onItemSelected(themeIndex.intValue)
                    sharedPrefs.sharedPreferences.edit().putInt(KEY_INDEX, themeIndex.intValue)
                        .apply()
                },
                selected = index == themeIndex.intValue,
            ) {
                Row(
                    modifier = Modifier
                        .width(80.dp)
                        .height(40.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = resource.second),
                        contentDescription = "",
                        tint = fontColor,
                    )
                    Text(
                        text = resource.first,
                        color = fontColor,
                        fontFamily = FontFamily(fontAmidoneGrotesk),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}