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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yaabelozerov.sharik.data.Who
import com.yaabelozerov.sharik.domain.MainVM
import com.yaabelozerov.sharik.ui.components.RCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    mainVM: MainVM, onClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var arrowDeg = animateFloatAsState(if (expanded) 180f else 0f, animationSpec = tween(400))
    val pullRefreshState = rememberPullToRefreshState()
    val lst = mainVM.state.collectAsState().value.randans

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(pullRefreshState.nestedScrollConnection)
    ) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, start = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Долги", // TODO String res
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .height(4.dp)
                            .weight(1f)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            modifier = Modifier
                                .rotate(arrowDeg.value)
                                .size(32.dp)
                        )
                    }
                }
            }

            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val totalState = mainVM.totalState.collectAsState().value
                    DebtCard(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        "Мне",
                        totalState.totalProfit.toString(),
                        expanded,
                        totalState.peopleProfit
                    )
                    DebtCard(
                        MaterialTheme.colorScheme.errorContainer,
                        "Мои",
                        totalState.totalDebt.toString(),
                        expanded,
                        totalState.peopleDebt
                    )
                }
            }

            items(lst) {
                RCard(it, mainVM, { onClick(it) })
                Spacer(Modifier.height(8.dp))
            }
        }

        if (pullRefreshState.isRefreshing) {
            LaunchedEffect(null) {
                mainVM.refreshAll()
            }
        }

        val isRefreshing = mainVM.isRefreshing.collectAsState().value
        LaunchedEffect(isRefreshing) {
            if (isRefreshing) {
                pullRefreshState.startRefresh()
            } else {
                pullRefreshState.endRefresh()
            }
        }
        PullToRefreshContainer(
            pullRefreshState, modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}


@Composable
fun RowScope.DebtCard(
    color: Color, text: String, value: String, expanded: Boolean, people: Map<Who, Long>
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
            text = (value.toFloat() / 100).toString(),
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
                            it.key.username,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        Spacer(Modifier.width(8.dp))
                        Text((it.value / 100).toString())
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}