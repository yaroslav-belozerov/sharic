package com.yaabelozerov.sharik.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.sharik.data.ExpenseDTO
import com.yaabelozerov.sharik.data.RandanDTO
import com.yaabelozerov.sharik.domain.MainVM
import kotlinx.coroutines.launch

@Composable
fun RCard(
    randan: RandanDTO,
    mainVM: MainVM
) {
    val scope = rememberCoroutineScope()
    val expenses = randan.expenses
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = randan.name,
                modifier = Modifier
                    .padding(
                        16.dp,
                        12.dp,
                        0.dp,
                        0.dp
                    ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Column(
                Modifier.padding(16.dp)
            )  {
                expenses.forEach {
                    var name: String = "pipec"
                    scope.launch {
                        name = mainVM.getUserById(it.paidByUserId).name
                    }
                    ExpenseCard(
                        name = it.name,
                        paidBy = name,
                        needToPay = it.users,
                        sum = it.sum
                    )
                    Spacer(Modifier.size(4.dp))

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
    needToPay: List<Pair<String, Float>>
) {
    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    sum.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.size(4.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ) {
                Text("Оплачено:", fontSize = 18.sp,) //Todo res
                Text(paidBy, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            Spacer(Modifier.size(4.dp))
            LazyRow {
                items(needToPay) {
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("${it.first}:", fontSize = 18.sp,)
                        Text(it.second.toString()+", ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(Modifier.size(2.dp))
                    }
                }

            }

        }
    }
}