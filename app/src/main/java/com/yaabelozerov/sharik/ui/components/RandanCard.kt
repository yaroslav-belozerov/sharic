package com.yaabelozerov.sharik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yaabelozerov.sharik.data.Owe
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.domain.MainVM
import com.yaabelozerov.sharik.ui.widgets.AddActivityidget
import kotlinx.coroutines.launch

@Composable
fun RCard(
    randan: Randan,
    mainVM: MainVM,
    onClickAdd: () -> Unit
) {
    val scope = rememberCoroutineScope()
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
                    onClick = onClickAdd,
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(32.dp))
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            )  {
                activities.forEach {
                        ExpenseCard(
                            name = it.name,
                            paidBy = it.pays.firstName,
                            needToPay = it.owes,
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
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row {
                Text(
                    name,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Spacer(Modifier.size(12.dp))
                Text(
                    sum.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AttachMoney, contentDescription = null, Modifier.size(24.dp), tint = MaterialTheme.colorScheme.tertiary)
                Text(paidBy, fontSize = 18.sp, color = MaterialTheme.colorScheme.tertiary)
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(needToPay) {
                    Column {
                        Text(it.user.firstName, fontSize = 18.sp)
                        Text((it.amount / 100).toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}