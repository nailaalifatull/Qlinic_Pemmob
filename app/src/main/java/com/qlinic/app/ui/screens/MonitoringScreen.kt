package com.qlinic.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDeep
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun MonitoringScreen(
    viewModel: QueueViewModel,
    onCancelled: () -> Unit = {}
) {
    val ticket by viewModel.ticket.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    // Confirmation dialog for cancellation
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Batalkan Antrean?", fontWeight = FontWeight.Bold) },
            text = { Text("Antrean Anda akan dibatalkan dan tidak dapat dikembalikan. Yakin ingin membatalkan?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        viewModel.cancelQueue()
                        onCancelled()
                    }
                ) {
                    Text("Ya, Batalkan", color = RedError, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Tidak", color = BluePrimary)
                }
            }
        )
    }

    Scaffold(containerColor = BackgroundGray) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // App bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(PinkPrimary),
                                contentAlignment = Alignment.Center
                            ) { Text("Q", color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp) }
                            Spacer(Modifier.width(8.dp))
                            Text("Qlinic", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.width(4.dp))
                            Text("Antrean", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            }

            // Ticket number card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("NOMOR ANTREAN ANDA", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp)
                        Text(
                            ticket.ticketNumber,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BlueDeep,
                            letterSpacing = 2.sp
                        )

                        Box(
                            modifier = Modifier.background(BackgroundGray, RoundedCornerShape(10.dp)).padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = PinkPrimary, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("${ticket.clinicName} • ${ticket.doctorName}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                            }
                        }
                    }
                }
            }

            // Ticket detail
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("RINCIAN TIKET", fontWeight = FontWeight.Bold, fontSize = 13.sp, letterSpacing = 0.5.sp)
                            Text(ticket.ticketCode, color = TextSecondary, fontSize = 11.sp)
                        }
                        Spacer(Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Jadwal Praktik", fontSize = 11.sp, color = TextSecondary)
                                Text(ticket.practiceSchedule, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Fasilitas Medis", fontSize = 11.sp, color = TextSecondary)
                                Text(ticket.facility, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Pasien Terverifikasi", fontSize = 12.sp, color = GreenSuccess)
                            Spacer(Modifier.weight(1f))
                            Text("Kode: ${ticket.ticketCode}", color = BluePrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Cancel button
            item {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RedError),
                    border = BorderStroke(1.dp, RedError)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Batalkan Antrean", fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
