package com.qlinic.app.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.BorderMedium
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkLight
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun CalledScreen(
    viewModel: QueueViewModel,
    onCheckIn: () -> Unit
) {
    val ticket by viewModel.ticket.collectAsState()

    // Pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "ring")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // Green urgent banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(listOf(GreenSuccess, Color(0xFF1B8B4B)))
                )
                .padding(20.dp)
        ) {
            Column {
                // Back arrow
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = White.copy(0.7f))
                    Spacer(Modifier.width(8.dp))
                    Text("Brand logo. - Primary color:", color = White.copy(0.5f), fontSize = 10.sp)
                    Spacer(Modifier.weight(1f))
                    Text("D...", color = White.copy(0.5f), fontSize = 10.sp)
                    IconButton(onClick = {}) { Icon(Icons.Default.Help, contentDescription = null, tint = White.copy(0.7f)) }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(White.copy(0.2f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = White, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("PEMBERITAHUAN LANGSUNG", color = White.copy(0.8f), fontSize = 10.sp, letterSpacing = 1.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = White, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "NOMOR ANDA SEDANG\nDIPANGGIL!",
                        color = White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        // Sound indicator
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Lonceng pemanggilan berbunyi di speaker ...", fontSize = 12.sp, color = TextSecondary)
                }
                Box(
                    modifier = Modifier.background(GreenLight, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Aktif", color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Ticket number card
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("NOMOR ANTREAN ANDA", color = TextSecondary, fontSize = 11.sp, letterSpacing = 1.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    ticket.ticketNumber,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BluePrimary,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BlueLight, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Silakan segera menuju ke Ruang Poli\nUmum (dr. Andini)",
                        color = BluePrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Info grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoCell(label = "Poli", value = ticket.clinicName, icon = Icons.Default.LocalHospital, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    InfoCell(label = "Loket / Ruang", value = ticket.room, icon = Icons.Default.MeetingRoom, highlight = true, modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoCell(label = "Dokter Jaga", value = ticket.doctorName, icon = Icons.Default.Person, modifier = Modifier.weight(1f))
                    Spacer(Modifier.width(12.dp))
                    InfoCell(label = "Jam Panggilan", value = ticket.callTime, icon = Icons.Default.AccessTime, modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(Modifier.height(16.dp))

                // Doctor info card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundGray, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(52.dp).clip(CircleShape).background(PinkLight),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Person, contentDescription = null, tint = PinkPrimary, modifier = Modifier.size(30.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(ticket.doctorInfo.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(ticket.doctorInfo.specialization, fontSize = 12.sp, color = TextSecondary)
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(GreenSuccess))
                            Spacer(Modifier.width(4.dp))
                            Text(ticket.doctorInfo.status, fontSize = 11.sp, color = GreenSuccess)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Action buttons
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = onCheckIn,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Saya Sudah di Klinik (Check-in)", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(10.dp))
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderMedium)
            ) {
                Icon(Icons.Default.AccessTime, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Belum Tiba di Klinik (Tandai Hadir Nanti)", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Tolerance rule info
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = White)
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Icon(Icons.Default.Info, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp).offset(y = 2.dp))
                Spacer(Modifier.width(8.dp))
                Column {
                    Text("Aturan Toleransi Kehadiran", fontWeight = FontWeight.Medium, fontSize = 12.sp)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        "Batas toleransi kehadiran adalah 3 nomor panggilan. Bila terlewat, nomor Anda akan beralih ke status MISSED namun dapat di-Re-Queue 1x saat Anda tiba di klinik.",
                        fontSize = 11.sp, color = TextSecondary
                    )
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun InfoCell(
    label: String,
    value: String,
    icon: ImageVector,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(BackgroundGray, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(icon, contentDescription = null, tint = if (highlight) BluePrimary else TextSecondary, modifier = Modifier.size(16.dp).offset(y = 2.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 10.sp, color = TextSecondary)
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = if (highlight) BluePrimary else TextPrimary)
        }
    }
}
