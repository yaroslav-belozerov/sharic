package com.yaabelozerov.sharik.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.sharik.data.UserDTO
import com.yaabelozerov.sharik.domain.MainVM

@Composable
fun MainPage(
    mainVM: MainVM
) {
    var expanded by remember { mutableStateOf(false) }
    var arrowDeg = animateFloatAsState(if (expanded) 180f else 0f, animationSpec = tween(400))
    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, start = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Долги", // TODO String res
                fontSize = 18.sp
            )
            Box(
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .height(4.dp)
                    .weight(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
            )

            IconButton(onClick = {
                if (!expanded) mainVM.fetchCardPeople()
                expanded = !expanded
            }) {
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    Modifier
                        .size(32.dp)
                        .rotate(arrowDeg.value)
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val (profit, debt) = mainVM.state.collectAsState().value.cardPreviews
            val (profitPeople, debtPeople) = mainVM.state.collectAsState().value.cardPeople
            DebtCard(
                MaterialTheme.colorScheme.tertiaryContainer,
                "Мне",
                profit.toString(),
                expanded,
                profitPeople
            )
            DebtCard(
                MaterialTheme.colorScheme.errorContainer,
                "Мои",
                debt.toString(),
                expanded,
                debtPeople
            )
        }
    }

}

@Composable
fun RowScope.DebtCard(
    color: Color, text: String, value: String, expanded: Boolean, people: List<Pair<UserDTO, Float>>
) {
    Card(
        Modifier.weight(1f), colors = CardDefaults.cardColors(
            containerColor = color,
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = value,
            modifier = Modifier.padding(start = 16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        AnimatedVisibility(expanded, modifier = Modifier.padding(horizontal = 16.dp)) {
            Column(Modifier.padding(top = 16.dp)) {
                people.forEach {
                    Row {
                        Text(
                            it.first.username,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(it.second.toString())
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}