package com.qlinic.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderMedium
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.TextMuted
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(BlueDark, BluePrimary))).padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(PinkPrimary), contentAlignment = Alignment.Center) {
                        Text("Q", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Qlinic", color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Klinik Digital Terpercaya", color = White.copy(0.7f), fontSize = 11.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Selamat Datang,", color = White.copy(0.8f), fontSize = 14.sp)
                Text("Astria Rahmawati", color = White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickActionCard(Icons.Default.Assignment, "Daftar\nAntrean", BluePrimary, Modifier.weight(1f))
            QuickActionCard(Icons.Default.History, "Riwayat\nKunjungan", PinkPrimary, Modifier.weight(1f))
            QuickActionCard(Icons.Default.LocalHospital, "Info\nPoliklinik", GreenSuccess, Modifier.weight(1f))
        }
        Spacer(Modifier.height(20.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = White)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = BluePrimary)
                Spacer(Modifier.width(12.dp))
                Text("Jam operasional klinik: Senin-Jumat, 08.00-14.00 WIB", fontSize = 13.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun QuickActionCard(icon: ImageVector, label: String, color: Color, modifier: Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f))) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = color, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun HistoryScreen() {
    Box(modifier = Modifier.fillMaxSize().background(BackgroundGray), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.History, contentDescription = null, tint = BorderMedium, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(12.dp))
            Text("Belum ada riwayat kunjungan", color = TextMuted, fontSize = 15.sp)
        }
    }
}

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize().background(BackgroundGray).padding(20.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = White)) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(72.dp).clip(CircleShape).background(BluePrimary), contentAlignment = Alignment.Center) {
                    Text("AR", color = White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text("Astria Rahmawati", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("NIK: 3201**********", color = TextSecondary, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.background(GreenLight, RoundedCornerShape(20.dp)).padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Pasien BPJS Terverifikasi", color = GreenSuccess, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
