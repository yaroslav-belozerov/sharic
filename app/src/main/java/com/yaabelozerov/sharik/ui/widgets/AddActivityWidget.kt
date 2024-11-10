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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.key
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
import androidx.compose.ui.text.input.KeyboardType
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
import okhttp3.internal.toImmutableMap

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
    var sum by remember { mutableStateOf<Float?>(null) }
    val even = sum?.div(userList.size)
    var userAmount by remember { mutableStateOf(userList.associate { it.username to 0f }.toImmutableMap()) }
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
                    if (userList.size > 1) Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        userAmount = userAmount.mapValues { even ?: 0f }.toImmutableMap()
                    }) { Text("Разделить поровну") }
                    OutlinedTextField(value = sum?.toString() ?: "",
                        onValueChange = {
                            try {
                                sum = it.toFloat()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Cумма") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
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
                                            try {
                                                userAmount = userAmount.plus(
                                                    it.username to it1.toFloat()
                                                )
                                            }catch (e: Exception) { e.printStackTrace() }
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        value = userAmount[it.username]!!.toString(),
                                        modifier = Modifier.width(72.dp),
                                        shape = MaterialTheme.shapes.medium,
                                        singleLine = true
                                    )
                                }
                            }
                        }

                    }
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
                            mainVM.sendActivity(
                                request = CreateActivityRequest(name = name,
                                    sum = sum?.times(100)?.toInt() ?: 0,
                                    randanId = randan.id,
                                    debts = userAmount.map {
                                        DebtRequest(
                                            it.key, (it.value * 100).toInt()
                                        )
                                    })
                            )
                        }, modifier = Modifier.weight(1f)
                    ) {
                        Text("Создать")
                    }
                }


            }
        }

    }
}