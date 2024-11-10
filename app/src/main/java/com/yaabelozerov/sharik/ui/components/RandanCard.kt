package com.yaabelozerov.sharik.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.yaabelozerov.sharik.R
import com.yaabelozerov.sharik.data.Owe
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.domain.MainVM
import com.yaabelozerov.sharik.ui.widgets.AddActivityidget

@Composable
fun RCard(
    randan: Randan,
    mainVM: MainVM,
    onClickAdd: (String) -> Unit
) {
    val activities = randan.activities
    var addActivityDialogOpen by remember { mutableStateOf(false) }

    if (addActivityDialogOpen) AddActivityidget(onDismissRequest = { addActivityDialogOpen = false}, onConfirmation = {}, randan.users, mainVM, randan)

    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = randan.name,
                    modifier = Modifier
                        .weight(1f).padding(start = 8.dp),
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                )
                IconButton(
                    onClick = { onClickAdd(randan.id) },
                ) {
                    Icon(Icons.Filled.People, contentDescription = null, modifier = Modifier.size(32.dp))
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(randan.users) {
                    AsyncImage(model = it.avatarUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.size(48.dp).clip(
                        CircleShape))
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )  {
                Log.d("", activities.toString())
                activities.forEach {
                        ExpenseCard(
                            name = it.name,
                            paidBy = it.pays.firstName,
                            needToPay = it.owe,
                            sum = it.sum.toFloat() / 100
                        )
                }
                Button(
                    onClick = { addActivityDialogOpen = true
                                },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.filledTonalButtonColors (
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }

        }
    }
}

@Composable
fun ExpenseCard(
    name: String,
    sum: Float,
    paidBy: String,
    needToPay: List<Owe>
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        )
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
        ) {
            Row {
                Text(
                    name,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f).animateContentSize(),
                    maxLines = if (expanded) 5 else 1
                )
                Spacer(Modifier.size(12.dp))
                Text(
                    sum.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.tertiary)
                Text(paidBy, fontSize = 18.sp, color = MaterialTheme.colorScheme.tertiary)
            }
            Spacer(Modifier.height(16.dp))
            AnimatedVisibility(expanded) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(bottom = 16.dp)) {
                    items(needToPay) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val painter = rememberAsyncImagePainter(R.drawable.avatar_placeholder_white)
                            AsyncImage(model = it.who.avatarUrl, contentDescription = null, modifier = Modifier.size(48.dp).clip(
                                CircleShape), contentScale = ContentScale.Crop, placeholder = painter
                            )
                            Column {
                                Text("${it.who.firstName} ${it.who.lastName}", fontSize = 18.sp)
                                Text((it.amount / 100.0).toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}