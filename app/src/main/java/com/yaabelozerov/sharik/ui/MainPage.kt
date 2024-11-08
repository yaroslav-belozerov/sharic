package com.yaabelozerov.sharik.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.sharik.domain.MainVM

@Composable
fun MainPage(
    mainVM: MainVM
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Долги", // TODO String res
                modifier = Modifier
                    .padding(16.dp, 4.dp, 8.dp, 4.dp),
                fontSize = 18.sp
            )
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 8.dp, 0.dp)
                    .height(5.dp)
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            )

            IconButton(
                onClick = {}
            ) {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, Modifier.size(32.dp))
            }
        }

        Row(
            Modifier
                .height(128.dp)
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DebtCard(
                MaterialTheme.colorScheme.tertiaryContainer,
                "Мне",
                mainVM.state.collectAsState().value.totalProfit.toString()
            )
            DebtCard(
                MaterialTheme.colorScheme.errorContainer,
                "Я",
                mainVM.state.collectAsState().value.totalDebt.toString()
            )
        }
    }

}

@Composable
fun RowScope.DebtCard(
    color: Color,
    text: String,
    value: String,
) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedCard(
        Modifier
            .fillMaxHeight().weight(1f),
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = value,
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            // IconButton(modifier = Modifier.padding(8.dp), onClick = {}) { Icon(Icons.Default.KeyboardArrowDown, null) }
        }
    }
}