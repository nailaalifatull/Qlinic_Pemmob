package com.qlinic.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.data.model.QueueStatus
import com.qlinic.app.ui.theme.BlueDeep
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.White
import com.qlinic.app.ui.theme.YellowAccent
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun SimulatorPanel(viewModel: QueueViewModel) {
    var expanded by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BlueDeep),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BugReport, contentDescription = null, tint = YellowAccent, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("SIMULATOR PENGUJI ALUR PRQ", color = White, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.3.sp)
                        Text("MVP State Demo", color = White.copy(0.6f), fontSize = 9.sp)
                    }
                }
                IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = White.copy(0.7f)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SimButton(
                            label = "Normal\n(WAITING)",
                            color = BluePrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setSimulatedState(QueueStatus.WAITING) }
                        )
                        SimButton(
                            label = "Panggilan\n(CALLED)",
                            color = GreenSuccess,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setSimulatedState(QueueStatus.CALLED) }
                        )
                        SimButton(
                            label = "Terlewat\n(MISSED)",
                            color = RedError,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.setSimulatedState(QueueStatus.MISSED) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = White, textAlign = TextAlign.Center)
    }
}
