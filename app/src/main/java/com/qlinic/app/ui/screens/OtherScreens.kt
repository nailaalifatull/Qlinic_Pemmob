package com.qlinic.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderMedium
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.TextMuted
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.ui.theme.YellowAccent
import com.qlinic.app.ui.theme.YellowLight
import com.qlinic.app.viewmodel.QueueViewModel

// ========== HOME SCREEN ==========

@Composable
fun HomeScreen(
    viewModel: QueueViewModel,
    onNavigateToQueue: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val name by viewModel.profileName.collectAsState()
    val initials = name.trim().split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")

    val poliList = listOf(
        Triple("Poli Umum", "dr. Andini Kusumawardani", Icons.Default.MedicalServices),
        Triple("Poli Gigi", "drg. Raka Pradipta", Icons.Default.Medication)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundGray),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ---- Header ----
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(BlueDark, BluePrimary)))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(PinkPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Q", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Qlinic", color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("Klinik Digital Terpercaya", color = White.copy(0.7f), fontSize = 11.sp)
                        }
                        Spacer(Modifier.weight(1f))
                        Box(
                            modifier = Modifier.size(38.dp).clip(CircleShape).background(White.copy(0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(initials, color = White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("Selamat Datang,", color = White.copy(0.8f), fontSize = 14.sp)
                    Text(name, color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Semoga lekas sehat \uD83D\uDC99", color = White.copy(0.7f), fontSize = 12.sp)
                }
            }
        }

        // ---- Quick Actions ----
        item {
            Spacer(Modifier.height(20.dp))
            Text(
                "Layanan Utama",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Default.Assignment,
                    label = "Daftar\nAntrean",
                    color = BluePrimary,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToQueue
                )
                QuickActionCard(
                    icon = Icons.Default.History,
                    label = "Riwayat\nKunjungan",
                    color = PinkPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToHistory
                )
            }
        }

        // ---- Info Operasional ----
        item {
            Spacer(Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Jam Operasional", fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                        Text("Senin \u2013 Jumat, 08.00 \u2013 14.00 WIB", fontSize = 12.sp, color = TextSecondary)
                    }
                }
            }
        }

        // ---- Info Klinik ----
        item {
            Spacer(Modifier.height(20.dp))
            Text(
                "Informasi Klinik",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ClinicInfoRow(Icons.Default.LocalHospital, "Nama Klinik", "Klinik Pratama Qlinic")
                    ClinicInfoRow(Icons.Default.Place, "Alamat", "Jl. Kesehatan No. 1, Bogor")
                    ClinicInfoRow(Icons.Default.Phone, "Telepon", "(0251) 123-4567")
                    ClinicInfoRow(Icons.Default.CalendarMonth, "Hari Layanan", "Senin \u2013 Jumat")
                    ClinicInfoRow(Icons.Default.Schedule, "Jam Buka", "08.00 \u2013 14.00 WIB", isLast = true)
                }
            }
        }

        // ---- Info Poliklinik ----
        item {
            Spacer(Modifier.height(20.dp))
            Text(
                "Poliklinik Tersedia",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
        }

        items(poliList) { (poliName, doctor, icon) ->
            PoliCard(name = poliName, doctor = doctor, icon = icon)
            Spacer(Modifier.height(8.dp))
        }


    }
}

@Composable
fun QuickActionCard(icon: ImageVector, label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ClinicInfoRow(icon: ImageVector, label: String, value: String, isLast: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, fontSize = 12.sp, color = TextSecondary, modifier = Modifier.width(100.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
    }
    if (!isLast) HorizontalDivider(color = BackgroundGray)
}

@Composable
fun PoliCard(name: String, doctor: String, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(42.dp).clip(CircleShape).background(GreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(doctor, fontSize = 12.sp, color = TextSecondary)
            }
            Row(
                modifier = Modifier.background(GreenLight, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GreenSuccess))
                Spacer(Modifier.width(4.dp))
                Text("Buka", color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BorderMedium, modifier = Modifier.size(18.dp))
        }
    }
}

// ========== HISTORY SCREEN ==========

data class HistoryEntry(
    val ticketNumber: String,
    val clinicName: String,
    val doctorName: String,
    val date: String,
    val status: String
)

@Composable
fun HistoryScreen() {
    val historyList = listOf(
        HistoryEntry(
            ticketNumber = "U-027",
            clinicName = "Poli Umum",
            doctorName = "dr. Andini Kusumawardani",
            date = "Senin, 20 Oktober 2025",
            status = "Selesai"
        ),
        HistoryEntry(
            ticketNumber = "G-014",
            clinicName = "Poli Gigi",
            doctorName = "drg. Raka Pradipta",
            date = "Rabu, 8 Oktober 2025",
            status = "Selesai"
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BackgroundGray),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                "Riwayat Kunjungan",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }
        items(historyList) { entry ->
            HistoryCard(entry = entry)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun HistoryCard(entry: HistoryEntry) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(GreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.clinicName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(entry.doctorName, fontSize = 12.sp, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(entry.date, fontSize = 11.sp, color = TextMuted)
            }
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(GreenLight, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(entry.status, color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(4.dp))
                Text(entry.ticketNumber, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = BluePrimary)
            }
        }
    }
}
