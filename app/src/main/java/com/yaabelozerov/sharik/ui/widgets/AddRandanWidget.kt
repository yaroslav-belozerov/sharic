package com.yaabelozerov.sharik.ui.widgets

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun AddRandanWidget(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    link: String
) {
    var name by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onDismissRequest },

        ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("Создать Кутёж", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text("Название")
                    },
                    modifier = Modifier.padding(16.dp)

                )

                Text(buildAnnotatedString {
                    withLink(LinkAnnotation.Url(url = link)) {
                        append("Ссылка для добавления")
                    }
                })
                Row(

                ) {
                    Button(
                        onClick = onDismissRequest
                    ) {
                        Text("Отменить")
                    }
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onConfirmation
                    ) {
                        Text("Создать")
                    }
                }

            }
        }

    }
}