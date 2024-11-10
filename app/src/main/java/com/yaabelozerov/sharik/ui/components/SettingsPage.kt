package com.yaabelozerov.sharik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.compose.onTertiaryLightMediumContrast
import com.yaabelozerov.sharik.R
import com.yaabelozerov.sharik.data.User
import com.yaabelozerov.sharik.domain.MainVM
import kotlinx.coroutines.launch

@Composable
fun SettingPage(
    mainVM: MainVM
) {
    val placeholderPainter = painterResource(R.drawable.avatar_placeholder)
    val user = mainVM.userState.collectAsState().value
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors()
                .copy(containerColor = MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    user?.avatarUrl?.let {
                        AsyncImage(model = it,
                            contentScale = ContentScale.Crop,
                            contentDescription = "pon",
                            placeholder = placeholderPainter,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .clip(shape = CircleShape)
                                .clickable { mainVM.onPickMedia() }
                                .size(98.dp))
                    } ?: IconButton(onClick = { mainVM.onPickMedia() }) {
                        Icon(
                            Icons.Default.Add, contentDescription = null
                        )
                    }
                    Column {
                        user?.firstName?.let { name ->
                            var edit by remember { mutableStateOf(false) }
                            if (!edit) Row(modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { edit = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(name, fontSize = 22.sp)
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            else {
                                var newName by remember { mutableStateOf(name) }
                                OutlinedTextField(newName,
                                    { newName = it },
                                    shape = MaterialTheme.shapes.medium,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                mainVM.editUser(user.copy(firstName = newName))
                                            }
                                            edit = false
                                        }) { Icon(Icons.Default.Check, contentDescription = null) }
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 22.sp,
                                    )
                                )
                            }
                        }
                        user?.lastName?.let { name ->
                            var edit by remember { mutableStateOf(false) }
                            if (!edit) Row(modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .clickable { edit = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically) {
                                Text(name, fontSize = 22.sp)
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            else {
                                var newName by remember { mutableStateOf(name) }
                                OutlinedTextField(newName,
                                    { newName = it },
                                    shape = MaterialTheme.shapes.medium,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            scope.launch {
                                                mainVM.editUser(user.copy(lastName = newName))
                                            }
                                            edit = false
                                        }) { Icon(Icons.Default.Check, contentDescription = null) }
                                    },
                                    textStyle = LocalTextStyle.current.copy(
                                        fontSize = 22.sp,
                                    )
                                )
                            }
                        }
                        user?.username?.let {
                            Text(
                                it,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
        TextButton(
            onClick = { mainVM.logout() }, modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Выйти",
                modifier = Modifier.padding(8.dp),
                color = MaterialTheme.colorScheme.error,
                fontSize = 18.sp
            )
        }
    }
}