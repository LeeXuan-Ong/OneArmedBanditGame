@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.onearmedbanditgame

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.onearmedbanditgame.ui.theme.OneArmedBanditGameTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OneArmedBanditGameTheme {
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
//                }
                OneArmedBanditGameApp()
            }
        }
    }
}

@Preview
@Composable
fun OneArmedBanditGameApp(){
    GameButtonAndImage(modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameButtonAndImage(modifier: Modifier = Modifier) {

    var winCounts by remember { mutableStateOf(0) }
    var playedCounts by remember { mutableStateOf(0) }
    var enabled by remember { mutableStateOf(true) }
    var win by remember { mutableStateOf(false) }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        CenterAlignedTopAppBar(
            title = { Text(stringResource(R.string.app_name)) }
        )
        Box(modifier = Modifier) {
            Image(
                painter = painterResource(id = R.drawable.halloween_wallpaper_1),
                contentDescription = "background",
                contentScale = ContentScale.FillBounds,
                modifier = modifier.matchParentSize()
            )

            Row(modifier = Modifier.padding(top = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                Box(
                    modifier = Modifier
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                        .defaultMinSize(minWidth = 80.dp, minHeight = 30.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(stringResource(R.string.played_counts) + " " + playedCounts.toString())
                }

                Box(
                    modifier = Modifier
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                        .defaultMinSize(minWidth = 110.dp, minHeight = 30.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    if (playedCounts == 0) {
                        Text(stringResource(R.string.win_ratio) + " " + "0.00")
                    } else {
                        Text(stringResource(R.string.win_ratio) + " " + "%.2f".format(winCounts.toFloat() / playedCounts.toFloat()))
                    }
                }

                Box(
                    modifier = Modifier
                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
                        .defaultMinSize(minWidth = 60.dp, minHeight = 30.dp)
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(stringResource(R.string.win_counts) + " " + winCounts.toString())
                }
            }

            var result1 by remember { mutableStateOf(randomNumbers(1, 6)) }
            var result2 by remember { mutableStateOf(randomNumbers(1, 6)) }
            var result3 by remember { mutableStateOf(randomNumbers(1, 6)) }
            var loading by remember { mutableStateOf(true) }
            val imageResource1 = displayImage(result1)
            val imageResource2 = displayImage(result2)
            val imageResource3 = displayImage(result3)

            Column(
                modifier = modifier.wrapContentSize(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 50.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.background(
                            Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                    ) {

                        Image(
                            painter = painterResource(id = imageResource1),
                            contentDescription = result1.toString(),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Box(
                        modifier = Modifier.background(
                            Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = imageResource2),
                            contentDescription = result2.toString(),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Box(
                        modifier = Modifier.background(
                            Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                    ) {
                        Image(
                            painter = painterResource(id = imageResource3),
                            contentDescription = result3.toString(),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                    Row(
                        modifier = Modifier.size(200.dp).padding(bottom = 50.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (playedCounts > 0 && !loading) {
                        if (win) {
                            Image(
                                painter = painterResource(id = R.drawable.winner),
                                contentDescription = "winner chicken dinner",
                                modifier = Modifier.size(100.dp)
                            )

                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.game_over_border),
                                contentDescription = "loser",
                                modifier = Modifier.size(100.dp)
                            )
                        }
                    }
                }
                LaunchedEffect(enabled) {
                    if (enabled) return@LaunchedEffect
                    else delay(1000L)
                    enabled = true
                }


                Row(modifier = Modifier.width(300.dp), horizontalArrangement = Arrangement.SpaceEvenly) {


                    Button(
                        onClick = {
                            MainScope().launch {
                                loading = true
                                for (i in 1..100) {
                                    result1 = randomNumbers(1, 6)
                                    result2 = randomNumbers(1, 6)
                                    result3 = randomNumbers(1, 6)
                                    if (enabled) {
                                        break
                                    } else {
                                        delay(100)
                                    }
                                }
                                loading = false
                                if (checkIfWin(result1, result2, result3) == 1) {
                                    winCounts += 1
                                    win = true
                                } else {
                                    win = false
                                }

                            };
                            playedCounts += 1;
                            Log.d("Results", "$result1,$result2,$result3");
                            enabled = false
                            loading = true
                        }, modifier = Modifier.size(100.dp, 50.dp),
                        enabled = enabled
                    ) {
                        Text(stringResource(R.string.roll))
                    }



                        Button(modifier = Modifier.size(100.dp, 50.dp),onClick = { winCounts = 0; playedCounts = 0; enabled = true }) {
                            Text(stringResource(R.string.reset))
                        }
                    }

                }
            }
        }
    }


fun randomNumbers(x: Int,y:Int):Int{
    return(x..y).random()
}


fun checkIfWin(result1: Int, result2: Int, result3: Int): Int{
    if(result1 == result2 && result2 == result3){
        return 1
    }
    return 0
}


fun displayImage(x: Int): Int{
    val image= when(x){
        1->R.drawable.coffin
        2->R.drawable.invisible
        3->R.drawable.knife
        4->R.drawable.pumpkin
        5-> R.drawable.scythe
        else -> {R.drawable.paper}
    }
    return image
}