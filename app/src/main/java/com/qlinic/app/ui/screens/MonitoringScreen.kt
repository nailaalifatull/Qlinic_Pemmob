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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.data.model.QueueStatus
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDeep
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.RedLight
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.ui.theme.YellowAccent
import com.qlinic.app.ui.theme.YellowLight
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun MonitoringScreen(
    viewModel: QueueViewModel,
    onCancelled: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    val ticket by viewModel.ticket.collectAsState()
    val clinics by viewModel.clinics.collectAsState()
    val registrationConfirmed by viewModel.registrationConfirmed.collectAsState()
    val ticketEndState by viewModel.ticketEndState.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(clinics, ticket.ticketNumber) {
        if (!registrationConfirmed) return@LaunchedEffect
        if (ticketEndState != null) return@LaunchedEffect          
        if (ticket.status != QueueStatus.WAITING) return@LaunchedEffect
        if (ticket.ticketNumber.isBlank()) return@LaunchedEffect

        val ticketPrefix = ticket.ticketNumber.substringBefore("-")
        val clinic = clinics.find { it.queueCode == ticketPrefix } ?: return@LaunchedEffect

        // admin tutup klinik
        if (!clinic.isOpen) {
            viewModel.expireTicket()
            return@LaunchedEffect
        }

        // nomor sudah dilewati admin
        val ticketNum = ticket.ticketNumber.substringAfter("-").toIntOrNull() ?: return@LaunchedEffect
        val servingNum = clinic.currentServing.substringAfter("-").toIntOrNull() ?: return@LaunchedEffect
        if (ticketNum < servingNum) {
            viewModel.completeTicket()
        }
    }

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

            
            if (ticketEndState != null) {
                item {
                    Spacer(Modifier.height(32.dp))
                    val isExpired = ticketEndState == "KADALUARSA"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(if (isExpired) RedLight else GreenLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isExpired) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (isExpired) RedError else GreenSuccess,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        Text(
                            text = if (isExpired) "Tiket Hangus" else "Kunjungan Selesai",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = TextPrimary
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = if (isExpired)
                                "Tiket kamu sudah hangus karena klinik sudah tutup, silakan ambil tiket baru sesuai jadwal pelayanan."
                            else
                                "Kunjungan Anda telah selesai. Terima kasih telah menggunakan layanan Qlinic.",
                            fontSize = 14.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(Modifier.height(28.dp))
                        Button(
                            onClick = {
                                viewModel.resetTicketEndState()
                                onNavigateToHistory()
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isExpired) RedError else BluePrimary
                            )
                        ) {
                            Icon(Icons.Default.History, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Lihat Riwayat", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
                return@LazyColumn // don't render ticket below
            }

            // ── Active ticket view ────────────────────────────────────────────

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

            // Action buttons
            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.completeTicket() },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Selesai", fontWeight = FontWeight.Bold)
                }

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
