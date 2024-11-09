package com.yaabelozerov.sharik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.compose.onTertiaryLightMediumContrast
import com.yaabelozerov.sharik.R
import com.yaabelozerov.sharik.domain.MainVM

@Composable
fun SettingPage(
    mainVM: MainVM
) {
    val placeholderPainter = painterResource(R.drawable.avatar_placeholder)
    val user = mainVM.userState.collectAsState().value
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        user?.avatarUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = "pon",
                placeholder = placeholderPainter,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(128.dp).clickable { mainVM.onPickMedia() },
                )
        } ?: IconButton(onClick = { mainVM.onPickMedia() }) { Icon(Icons.Default.Add, contentDescription = null) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            user?.firstName?.let { Text(it, fontSize = 28.sp) }
            user?.lastName?.let { Text(it, fontSize = 28.sp) }
        }
    }
}