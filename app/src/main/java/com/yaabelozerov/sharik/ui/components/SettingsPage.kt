package com.yaabelozerov.sharik.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.yaabelozerov.sharik.R
import com.yaabelozerov.sharik.domain.MainVM

@Composable
fun SettingPage(
    mainVM: MainVM
) {
    val painter = painterResource(R.drawable.avatar_placeholder)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            "http://igw.isntrui.ru:1401/res/prod_avatar_1511392686_9.png",
            contentDescription = "pon",
            placeholder = painter,
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(200.dp),

        )
        Row {
            mainVM.userState.collectAsState().value.let {
            it.name?.let { it1 -> Text(it1) }
                Spacer(Modifier.size(4.dp))
            it.surname?.let { it1 -> Text(it1) }
            }
        }

    }
}