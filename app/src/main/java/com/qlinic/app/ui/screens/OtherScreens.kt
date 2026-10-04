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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
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
import com.qlinic.app.data.model.HistoryEntry
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.RedLight
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
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit = {}
) {
    val name by viewModel.profileName.collectAsState()
    val clinics by viewModel.clinics.collectAsState()
    val initials = name.trim().split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")

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
                            modifier = Modifier.size(38.dp).clip(CircleShape).background(White.copy(0.2f)).clickable { onNavigateToProfile() },
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

        items(clinics) { clinic ->
            val icon = if (clinic.icon == "dental") Icons.Default.Medication else Icons.Default.MedicalServices
            PoliCard(name = clinic.name, doctor = clinic.doctor.name, icon = icon, isOpen = clinic.isOpen)
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
fun PoliCard(name: String, doctor: String, icon: ImageVector, isOpen: Boolean = true) {
    val badgeBg = if (isOpen) GreenLight else RedLight
    val badgeText = if (isOpen) GreenSuccess else RedError
    val dotColor = if (isOpen) GreenSuccess else RedError
    val label = if (isOpen) "Buka" else "Tutup"

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
                modifier = Modifier.size(42.dp).clip(CircleShape).background(if (isOpen) GreenLight else RedLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = if (isOpen) GreenSuccess else RedError, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(doctor, fontSize = 12.sp, color = TextSecondary)
            }
            Row(
                modifier = Modifier.background(badgeBg, RoundedCornerShape(20.dp)).padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(dotColor))
                Spacer(Modifier.width(4.dp))
                Text(label, color = badgeText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ========== HISTORY SCREEN ==========

@Composable
fun HistoryScreen(viewModel: QueueViewModel) {
    val historyList by viewModel.historyList.collectAsState()

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

        if (historyList.isEmpty()) {
            item {
                Spacer(Modifier.height(60.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = TextSecondary.copy(0.4f),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Belum ada riwayat kunjungan.",
                        fontSize = 14.sp,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(historyList) { entry ->
                HistoryCard(entry = entry)
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun HistoryCard(entry: HistoryEntry) {
    val isExpired = entry.status == "Kadaluarsa"
    val iconBg = if (isExpired) RedLight else GreenLight
    val iconTint = if (isExpired) RedError else GreenSuccess
    val badgeBg = if (isExpired) RedLight else GreenLight
    val badgeText = if (isExpired) RedError else GreenSuccess

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
                modifier = Modifier.size(44.dp).clip(CircleShape).background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isExpired) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(entry.clinicName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                Text(entry.doctorName, fontSize = 12.sp, color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(
                    if (entry.time.isNotEmpty()) "${entry.date} · ${entry.time}" else entry.date,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(entry.status, color = badgeText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(Modifier.height(4.dp))
                Text(entry.ticketNumber, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = BluePrimary)
            }
        }
    }
}
