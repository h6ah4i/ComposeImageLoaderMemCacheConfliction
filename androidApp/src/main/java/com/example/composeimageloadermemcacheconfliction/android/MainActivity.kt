package com.example.composeimageloadermemcacheconfliction.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val imageUrl = "https://avatars.githubusercontent.com/u/2552365?v=4"
    val showScaledImage = remember { mutableStateOf(false) }

    val originalRequest = remember { ImageRequest { data(imageUrl) } }
    val originalPainter = rememberImagePainter(originalRequest)

    val scaledRequest = remember(showScaledImage.value) {
        ImageRequest(originalRequest) {
            // if (showScaledImage.value) data("https://avatars.githubusercontent.com/u/2552365?v=5")
            if (showScaledImage.value) options { maxImageSize = 10 }
        }
    }
    val scaledPainter = rememberImagePainter(scaledRequest)

    LaunchedEffect(Unit) {
        delay(2000)
        showScaledImage.value = true // Update flag after the original sized image is loaded
    }

    Row(modifier = Modifier.padding(24.dp)) {
        // Excepted: Clear image
        Image(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray),
            painter = originalPainter,
            contentDescription = "original",
            contentScale = ContentScale.FillWidth,
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Expected: Blurry image
        Image(
            modifier = Modifier
                .size(150.dp)
                .background(Color.LightGray),
            painter = if (showScaledImage.value) scaledPainter else ColorPainter(Color.Transparent),
            contentDescription = "scaled",
            contentScale = ContentScale.FillWidth,
        )
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainContent()
    }
}
