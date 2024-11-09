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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.yaabelozerov.sharik.data.User
import okhttp3.internal.toImmutableList

@Composable
fun AddActivityidget(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    userList: List<User>,
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var sum by remember { mutableStateOf(1000f) }
    var userAmount by remember { mutableStateOf(mapOf<String, Float>()) }


    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
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
                    }, shape = MaterialTheme.shapes.medium
                )

                Column {
                    OutlinedTextField(
                        value = sum.toString(),
                        onValueChange = { sum = it.toFloat() }

                    )
                    userList.forEach {
                        Row() {
                            Checkbox(
                                onCheckedChange = {},
                                checked = false
                            )
                            Text(it.firstName + " " + it.lastName)
                            Spacer(Modifier.weight(1f))
                            OutlinedTextField(
                                onValueChange = { it1 ->
                                    userAmount =
                                        userAmount.plus(it.username to (it1.toFloatOrNull() ?: 0f))

                                },
                                value = userAmount[it.username]?.toString() ?: "0",


                                )
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
                        onClick = { onConfirmation(name) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Создать")
                    }
                }


            }
        }

    }
}