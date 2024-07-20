package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_THEME_STATUS
import coder.behzod.presentation.utils.helpers.restartApp
import coder.behzod.presentation.viewModels.SettingsViewModel
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.ProgressButton
import coder.behzod.presentation.views.SingleChoiceButtonRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CommitPrefEdits")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel(),
    sharedPrefs: SharedPreferenceInstance,
) {
    val prefsFontSize = sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)
    val state = remember {
        mutableFloatStateOf(
            if (prefsFontSize == 18) {
                0f
            } else if (prefsFontSize == 22) {
                1f
            } else {
                2f
            }
        )
    }

    val context = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    val themeStatus = remember { mutableStateOf(true) }
    val themeIndex = remember { mutableIntStateOf(0) }
    val isChanged = remember { mutableStateOf(false) }
    val isExpanded = remember { mutableStateOf(false) }

    val localeOptions = mapOf(
        stringResource(R.string.default_language) to "default",
        stringResource(R.string.english_language) to "en",
        stringResource(R.string.russian_language) to "ru",
        stringResource(R.string.uzbek_language) to "uz"
    ).mapKeys { it.key }

    LaunchedEffect(key1 = Int) {
        delay(100L)
        viewModel.getIndex()
        themeIndex.intValue = sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)
    }

    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    if (colorTheme == Color.Black) {
        themeColor.value = Color.Black
    } else {
        themeColor.value = Color.White
    }

    val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
    val fontColor = remember { mutableStateOf(colorFont) }

    if (colorFont == Color.White) {
        fontColor.value = Color.White
    } else {
        fontColor.value = Color.Black
    }

    val themeColors = listOf(
        Pair(stringResource(R.string.dark), R.drawable.ic_dark), Pair(
            stringResource(R.string.light), R.drawable.ic_light
        )
    )

    val initialFontSize = remember {
        mutableIntStateOf(
            sharedPrefs.sharedPreferences.getInt(
                KEY_FONT_SIZE, 18
            )
        )
    }
    val fontSize = remember { mutableIntStateOf(18) }
    val experimentalFontSize = remember { mutableStateOf(18.sp) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColor.value)
    ) {
        TopAppBar(
            modifier = Modifier
                .padding(2.dp)
                .border(width = 1.dp, color = fontColor.value, shape = RoundedCornerShape(10.dp)),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = themeColor.value
            ),
            title = {
                Text(
                    text = stringResource(R.string.settings),
                    fontSize = initialFontSize.intValue.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk),
                    color = fontColor.value
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate(ScreensRouter.MainScreenRoute.route)
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back to main",
                        tint = fontColor.value
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 10.dp)
                .background(color = fontColor.value)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.theme),
                color = fontColor.value,
                fontSize = initialFontSize.intValue.sp,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
            SingleChoiceButtonRow(
                items = themeColors,
                onItemSelected = {
                    viewModel.setIndex(it)
                    if (it == 0) {
                        coroutineScope.launch(Dispatchers.Main) {
                            themeStatus.value = true
                            themeIndex.intValue = 0
                            themeColor.value = Color.Black
                            fontColor.value = Color.White
                            sharedPrefs.sharedPreferences.edit()
                                .putInt(KEY_INDEX, themeIndex.intValue).apply()
                        }
                    } else {
                        coroutineScope.launch(Dispatchers.Main) {
                            themeStatus.value = false
                            themeIndex.intValue = 1
                            themeColor.value = Color.White
                            fontColor.value = Color.Black
                            sharedPrefs.sharedPreferences.edit()
                                .putInt(KEY_INDEX, themeIndex.intValue).apply()
                            sharedPrefs.sharedPreferences.edit()
                                .putBoolean(KEY_THEME_STATUS, themeStatus.value).apply()
                        }
                    }
                },
                themeColor = themeColor.value,
                fontColor = fontColor.value,
                sharedPrefs
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 10.dp)
                .background(color = fontColor.value)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.language),
                color = fontColor.value,
                fontFamily = FontFamily(fontAmidoneGrotesk),
                fontSize = initialFontSize.intValue.sp
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        isExpanded.value = !isExpanded.value
                    }) {
                    Icon(
                        imageVector = if (isExpanded.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "locales",
                        tint = fontColor.value
                    )
                }
                Column {
                    DropdownMenu(
                        expanded = isExpanded.value,
                        modifier = Modifier
                            .align(Alignment.End)
                            .background(themeColor.value),
                        onDismissRequest = {
                            isExpanded.value = !isExpanded.value
                        }
                    ) {
                        /* Select locale */
                        localeOptions.keys.forEach { locale ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .background(themeColor.value),
                                colors = MenuDefaults.itemColors(
                                    textColor = fontColor.value,
                                    leadingIconColor = fontColor.value,
                                ),
                                leadingIcon = {
                                    if (locale.contains(stringResource(id = R.string.default_language))) {
                                        Icon(
                                            modifier = Modifier
                                                .size(30.dp),
                                            painter = painterResource(id = R.drawable.ic_default),
                                            contentDescription = "default locale icon",
                                            tint = fontColor.value
                                        )
                                    } else {
                                        when (locale) {
                                            stringResource(id = R.string.english_language) -> {
                                                Image(
                                                    modifier = Modifier
                                                        .size(30.dp),
                                                    painter = painterResource(id = R.drawable.ic_eng_flag),
                                                    contentDescription = "english leading icon",
                                                )
                                            }

                                            stringResource(id = R.string.russian_language) -> {
                                                Image(
                                                    modifier = Modifier
                                                        .size(30.dp),
                                                    painter = painterResource(id = R.drawable.ic_ru_flag),
                                                    contentDescription = "russian leading icon",
                                                )
                                            }

                                            stringResource(id = R.string.uzbek_language) -> {
                                                Image(
                                                    modifier = Modifier
                                                        .size(30.dp),
                                                    painter = painterResource(id = R.drawable.ic_uz_flag),
                                                    contentDescription = "uzbek leading icon",
                                                )
                                            }
                                        }
                                    }
                                },
                                text = {
                                    Text(
                                        text = locale,
                                        fontSize = initialFontSize.intValue.sp,
                                        color = fontColor.value
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isChanged.value = true
                                    AppCompatDelegate.setApplicationLocales(
                                        LocaleListCompat.forLanguageTags(
                                            localeOptions[locale]
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 10.dp)
                .background(color = fontColor.value)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.font_size),
                color = fontColor.value,
                fontSize = initialFontSize.intValue.sp,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        }
        Slider(
            value = state.floatValue,
            onValueChange = {
                state.floatValue = it
                when (state.floatValue) {
                    0f -> {
                        if (fontSize.intValue > 18) {
                            isChanged.value = true
                        }
                        fontSize.intValue = 18
                        experimentalFontSize.value = 18.sp
                    }

                    1f -> {
                        isChanged.value = true
                        fontSize.intValue = 22
                        experimentalFontSize.value = 22.sp
                    }

                    2f -> {
                        isChanged.value = true
                        fontSize.intValue = 25
                        experimentalFontSize.value = 25.sp
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 10.dp),
            valueRange = 0f..2f,
            steps = 1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.abc_123),
                color = fontColor.value,
                fontSize = experimentalFontSize.value,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        HorizontalDivider(
            Modifier
                .padding(horizontal = 10.dp)
                .background(color = fontColor.value)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ProgressButton(
                content = {
                    sharedPrefs.sharedPreferences.edit().putInt(KEY_FONT_SIZE, fontSize.intValue)
                        .apply()
                    Handler(Looper.getMainLooper()).postDelayed({
                        restartApp(context)
                        sharedPrefs.sharedPreferences.edit()
                            .putInt(KEY_FONT_SIZE, fontSize.intValue).apply()
                    }, 1000L)
                },
                text = stringResource(R.string.apply_changes),
                color = themeColor.value,
                assetColor = fontColor.value
            )
        }
        if (isChanged.value) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.the_changes_will_be_applied_after_the_reboot),
                    fontSize = 25.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk),
                    color = Color.Red
                )
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        BottomNavigationView(
            themeColor = themeColor.value,
            fontColor = fontColor.value,
            navController = navController
        )
    }
}