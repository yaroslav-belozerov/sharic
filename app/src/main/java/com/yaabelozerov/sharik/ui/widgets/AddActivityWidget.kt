package com.yaabelozerov.sharik.ui.widgets

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yaabelozerov.sharik.data.CreateActivityRequest
import com.yaabelozerov.sharik.data.DebtRequest
import com.yaabelozerov.sharik.data.Randan
import com.yaabelozerov.sharik.data.User
import com.yaabelozerov.sharik.domain.MainVM
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

@Composable
fun AddActivityidget(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    userList: List<User>,
    mainVM: MainVM,
    randan: Randan
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var sum by remember { mutableStateOf(1000) }
    var userAmount by remember { mutableStateOf(mapOf<String, Int>()) }
    var scope = rememberCoroutineScope()


    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ), modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LaunchedEffect(null) { focusRequester.requestFocus() }
                Text("Создать Трату", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text("Название")
                    },
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    userList.forEach {
                        mainVM.userState.collectAsState().value?.username.let { user ->
                            if (user != it.username) {
                                Row {
//                            Checkbox(
//                                onCheckedChange = {},
//                                checked = false
//                            )
                                    Text(
                                        it.firstName + " " + it.lastName,
                                        fontSize = 18.sp,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(
                                                top = 12.dp
                                            ),
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1

                                    )
                                    Spacer(Modifier.size(12.dp))
                                    OutlinedTextField(
                                        onValueChange = { it1 ->
                                            userAmount = userAmount.plus(
                                                it.username to (it1.toIntOrNull() ?: 0)
                                            )
                                        },
                                        value = userAmount[it.username]?.toString() ?: "0",
                                        modifier = Modifier.width(100.dp),
                                        singleLine = true,
                                        shape = MaterialTheme.shapes.medium

                                    )
                                }
                            }
                        }

                    }
                    OutlinedTextField(value = sum.toString(),
                        onValueChange = { sum = it.toInt() },
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Cумма") },
                        singleLine = true

                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text("Отменить")
                    }
                    Button(
                        onClick = {
                            onDismissRequest()
                            mainVM.sendActivity(request = CreateActivityRequest(name = name,
                                sum = sum,
                                randanId = randan.id,
                                debts = userAmount.map {
                                    DebtRequest(
                                        it.key, (it.value).toInt()
                                    )
                                }))
                        }, modifier = Modifier.weight(1f)
                    ) {
                        Text("Создать")
                    }
                }


            }
        }

    }
}