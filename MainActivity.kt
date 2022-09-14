package com.example.hdmicecdemoapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hdmicectestapp.HdmiHelper

import com.example.hdmicectestapp.ui.theme.HDMICECTESTAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hdmiHelper  = HdmiHelper(this@MainActivity)

        setContent {
            HDMICECTESTAPPTheme {
                MainScreen(hdmiHelper, this@MainActivity)
            }
        }
    }
}

@Composable
fun MainScreen( hdmiHelper : HdmiHelper, context: Context){
    Scaffold(topBar = { AppBar() }) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column {
                Volume_UP()
                POWER_OFF(hdmiHelper, context )
                POWER_ON(hdmiHelper, context)
                Volume_DOWN()

            }
        }
    }
}

@Composable
fun AppBar() {
    TopAppBar(
        navigationIcon = {
            Icon(
                Icons.Default.Home,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 14.dp),
            )
        },
        title = { Text("HDMI CEC DEMO TEST APP") }
    )
}
@Composable
fun Volume_UP() {
    Card(
        modifier = Modifier
            .padding(top = 160.dp, bottom = 40.dp, start = 135.dp, end = 16.dp)
            .fillMaxWidth(0.5f)
            .wrapContentHeight(align = Alignment.CenterVertically),
        elevation = 8.dp,
        backgroundColor = Color.Green
    ){
        Icon(
            Icons.Default.KeyboardArrowUp,
            contentDescription = "Volume UP")
    }
}

@Composable
fun POWER_OFF(hdmiHelper : HdmiHelper, context: Context) {
    Button(
        onClick = { hdmiHelper.PowerOFFTargetTv(context) },
        modifier = Modifier.padding(top = 40.dp, bottom = 20.dp, start = 135.dp, end = 16.dp),
        contentPadding = PaddingValues(
            start = 10.dp,
            top = 12.dp,
            end = 40.dp,
            bottom = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
    ) {
        Icon(
            Icons.Filled.AccountBox,
            contentDescription = "Power OFF",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Power OFF")
    }
}

@Composable
fun POWER_ON(hdmiHelper : HdmiHelper, context: Context) {
    Button(
        onClick = {  hdmiHelper.PowerONTargetTv(context)},
        modifier = Modifier.padding(top = 40.dp, bottom = 20.dp, start = 135.dp, end = 16.dp),
        contentPadding = PaddingValues(
            start = 10.dp,
            top = 12.dp,
            end = 40.dp,
            bottom = 12.dp
        ),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
    ) {
        Icon(
            Icons.Filled.AccountBox,
            contentDescription = "Power ON",
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Power ON")
    }
}
@Composable
fun Volume_DOWN(){
    Card(
        modifier = Modifier
            .padding(top = 80.dp, bottom = 40.dp, start = 135.dp, end = 16.dp)
            .fillMaxWidth(0.5f)
            .wrapContentHeight(align = Alignment.Top),
        elevation = 8.dp,
        backgroundColor = Color.Green
    ){
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = "Volume DOWN")
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HDMICECTESTAPPTheme() {
//        MainScreen()
    }
}
