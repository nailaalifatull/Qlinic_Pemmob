package com.qlinic.app.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.data.model.QueueStatus
import com.qlinic.app.ui.components.SimulatorPanel
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDeep
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.BorderMedium
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.OrangeWarning
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.TextMuted
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.ui.theme.YellowLight
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun MonitoringScreen(
    viewModel: QueueViewModel,
    onNavigateToCalled: () -> Unit,
    onNavigateToMissed: () -> Unit
) {
    val ticket by viewModel.ticket.collectAsState()

    // Navigate based on status changes
    LaunchedEffect(ticket.status) {
        when (ticket.status) {
            QueueStatus.CALLED -> onNavigateToCalled()
            QueueStatus.MISSED -> onNavigateToMissed()
            else -> {}
        }
    }

    // Pulse animation for sync dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(BlueLight), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // Sync indicator
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(GreenSuccess.copy(alpha = alpha)))
                        Spacer(Modifier.width(6.dp))
                        Text("SINKRONISASI LANGSUNG", color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                    Text("21:23 WIB", color = TextSecondary, fontSize = 11.sp)
                }
                HorizontalDivider(color = BorderLight)
            }

            // Status card
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
                        // Status chip
                        Box(
                            modifier = Modifier
                                .background(BlueLight, RoundedCornerShape(20.dp))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(14.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Menunggu Giliran (WAITING)", color = BluePrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        Spacer(Modifier.height(8.dp))
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

                        Spacer(Modifier.height(16.dp))

                        // People ahead + estimate
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Card(
                                modifier = Modifier.weight(1f).padding(end = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundGray)
                            ) {
                                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PeopleAlt, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(28.dp))
                                    Spacer(Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            "${ticket.peopleAhead} Orang Lagi",
                                            fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary
                                        )
                                        Text("Menunggu sebelum giliran Anda", fontSize = 10.sp, color = TextSecondary)
                                    }
                                }
                            }
                            Card(
                                modifier = Modifier.weight(0.5f).padding(start = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundGray)
                            ) {
                                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Estimasi", fontSize = 10.sp, color = TextSecondary)
                                    Text("~${ticket.estimatedWaitMin}", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = PinkPrimary)
                                    Text("Menit", fontSize = 10.sp, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }

            // Current queue status bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                        QueueStatusItem("Sedang Di\nRuang", ticket.currentServing, "Ruang 01", BluePrimary, true)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.align(Alignment.CenterVertically))
                        QueueStatusItem("Berikutnya", ticket.nextNumber, "Bersiap", TextSecondary, false)
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.align(Alignment.CenterVertically))
                        QueueStatusItem("Nomor Anda", ticket.ticketNumber, "Antrean Pasien", PinkPrimary, false)
                    }
                }
            }

            // Progress stepper
            item {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Route, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Progres Layanan Antrean", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Text("Langkah 2 dari 4", color = BluePrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(16.dp))
                        StepItem(stepNum = 1, isDone = true, isActive = false, title = "Pendaftaran Terbit (U-040)", subtitle = "Terverifikasi sistem • 09:45 WIB")
                        StepConnector(isDone = true)
                        StepItem(stepNum = 2, isDone = false, isActive = true, title = "Menunggu Giliran (Saat ini)", subtitle = "2 pasien di depan Anda sedang berlangsung")
                        StepConnector(isDone = false)
                        StepItem(stepNum = 3, isDone = false, isActive = false, title = "Panggilan ke Ruang Poli", subtitle = "Siap masuk ke Poli Umum")
                        StepConnector(isDone = false)
                        StepItem(stepNum = 4, isDone = false, isActive = false, title = "Pelayanan Medis Selesai", subtitle = "Konsultasi dokter & pengambilan resep")
                    }
                }
            }

            // Important info
            item {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = YellowLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = OrangeWarning, modifier = Modifier.size(20.dp).offset(y = 2.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Informasi Penting Ruang Tunggu", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Harap tiba di ruang tunggu klinik sebelum nomor Anda dipanggil. Sistem akan memberikan notifikasi suara & getar saat giliran Anda mendekat.",
                                fontSize = 12.sp, color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Ticket detail
            item {
                Spacer(Modifier.height(12.dp))
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
                            Text("Pasien BPJS / Umum Terverifikasi", fontSize = 12.sp, color = GreenSuccess)
                            Spacer(Modifier.weight(1f))
                            Text("Lihat QB", color = BluePrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Cancel button
            item {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = RedError),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedError)
                ) {
                    Icon(Icons.Default.Cancel, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Batalkan Antrean", fontWeight = FontWeight.Medium)
                }
            }

            // Simulator panel
            item {
                Spacer(Modifier.height(12.dp))
                SimulatorPanel(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun QueueStatusItem(label: String, number: String, sub: String, numberColor: Color, isMain: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
        Text(number, fontSize = if (isMain) 24.sp else 18.sp, fontWeight = FontWeight.ExtraBold, color = numberColor)
        Text(sub, fontSize = 9.sp, color = TextSecondary)
    }
}

@Composable
fun StepItem(stepNum: Int, isDone: Boolean, isActive: Boolean, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> GreenSuccess
                        isActive -> BluePrimary
                        else -> BorderMedium
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(Icons.Default.Check, contentDescription = null, tint = White, modifier = Modifier.size(16.dp))
            } else if (isActive) {
                Icon(Icons.Default.Person, contentDescription = null, tint = White, modifier = Modifier.size(15.dp))
            } else {
                Text("$stepNum", color = White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.padding(top = 2.dp)) {
            Text(title, fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp, color = if (isActive) TextPrimary else TextSecondary)
            Text(subtitle, fontSize = 11.sp, color = TextMuted)
        }
    }
}

@Composable
fun StepConnector(isDone: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = 13.dp)
            .width(2.dp)
            .height(24.dp)
            .background(if (isDone) GreenSuccess else BorderLight)
    )
}
