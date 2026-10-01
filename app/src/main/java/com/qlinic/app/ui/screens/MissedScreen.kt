package com.qlinic.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.BorderMedium
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.RedLight
import com.qlinic.app.ui.theme.TextMuted
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun MissedScreen(
    viewModel: QueueViewModel,
    onReQueueActivated: () -> Unit
) {
    val ticket by viewModel.ticket.collectAsState()
    val reQueueUsed = ticket.reQueueUsed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // App bar
        Box(modifier = Modifier.fillMaxWidth().background(White).padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Brand logo.", color = TextSecondary, fontSize = 11.sp)
                    Spacer(Modifier.weight(1f))
                    Text("D...", color = TextSecondary, fontSize = 11.sp)
                    IconButton(onClick = {}) { Icon(Icons.Default.Help, contentDescription = null, tint = TextSecondary) }
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(BlueLight), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        // MISSED status banner
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RedLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = RedError, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.background(RedError, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("STATUS: MISSED", color = White, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text("Batas Toleransi Habis", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = RedError)
                Text("Antrean Anda Terlewat", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Spacer(Modifier.height(6.dp))
                Text(
                    buildAnnotatedString {
                        append("Nomor ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = RedError)) { append(ticket.ticketNumber) }
                        append(" telah dipanggil 3 kali di Ruang Poli Umum, namun Anda belum hadir di area tunggu.")
                    },
                    fontSize = 13.sp, color = TextSecondary
                )
            }
        }

        // Live monitoring card
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
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(GreenSuccess))
                        Spacer(Modifier.width(6.dp))
                        Text("Pantauan Antrean Langsung", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    Text("Poli Umum • R. 02", color = TextSecondary, fontSize = 11.sp)
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Sedang Masuk", fontSize = 11.sp, color = TextSecondary)
                        Text("U-041", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = BluePrimary)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(3.dp))
                            Text("Di Dalam Bilik", fontSize = 10.sp, color = GreenSuccess)
                        }
                    }
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Nomor Anda", fontSize = 11.sp, color = TextSecondary)
                        Text(ticket.ticketNumber, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = RedError)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Cancel, contentDescription = null, tint = RedError, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(3.dp))
                            Text("Terlewat (0 Check-in)", fontSize = 10.sp, color = RedError)
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Re-queue rules
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(32.dp).clip(CircleShape).background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.Autorenew, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Aturan Re-Queue Mandiri", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Hak antrean dilindungi secara otomatis", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BlueLight, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Nomor Anda ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = BluePrimary)) { append(ticket.ticketNumber) }
                            append(" ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("TIDAK DIGANTI") }
                            append(" dan ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("TIDAK HANGUS") }
                            append(". Begitu Anda mengkonfirmasi kehadiran di klinik, nomor Anda langsung disisipkan menjadi giliran tepat berikutnya setelah U-041 selesai.")
                        },
                        fontSize = 12.sp, color = TextPrimary
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text("SIMULASI URUTAN PANGGILAN BARU", fontSize = 10.sp, color = TextSecondary, letterSpacing = 0.5.sp, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(8.dp))

                QueueOrderItem(number = 1, ticket = "U-041", subtitle = "Sedang berlangsung", badge = "Sedang Dilayani", badgeColor = BluePrimary)
                Spacer(Modifier.height(8.dp))
                QueueOrderItem(number = 2, ticket = "${ticket.ticketNumber} (Anda) ✓", subtitle = "Prioritas Segera Setelah U-041", badge = "PRIORITAS", badgeColor = GreenSuccess)
                Spacer(Modifier.height(8.dp))
                QueueOrderItem(number = 3, ticket = "U-042 & Seterusnya", subtitle = "Menunggu giliran reguler", badge = "Antrean Normal", badgeColor = TextSecondary)
            }
        }

        Spacer(Modifier.height(12.dp))

        // Warning card
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = RedLight),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(modifier = Modifier.padding(14.dp)) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = RedError, modifier = Modifier.size(20.dp).offset(y = 2.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Peringatan Ketat: Kuota Re-Queue 1x", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = RedError)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        buildAnnotatedString {
                            append("Fasilitas Re-Queue ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("hanya berlaku 1 (satu) kali") }
                            append(". Jika Anda telah mengaktifkan tombol dan kembali tidak hadir saat giliran kedua ini dipanggil, tiket otomatis ")
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = RedError)) { append("HANGUS PERMANEN (EXPIRED)") }
                            append(" dan Anda wajib mengambil tiket baru dari awal.")
                        },
                        fontSize = 12.sp, color = TextPrimary
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Re-Queue button
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = {
                    if (!reQueueUsed) {
                        viewModel.activateReQueue()
                        onReQueueActivated()
                    }
                },
                enabled = !reQueueUsed,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!reQueueUsed) BluePrimary else TextMuted,
                    disabledContainerColor = BorderMedium
                )
            ) {
                Icon(if (reQueueUsed) Icons.Default.CheckCircle else Icons.Default.LocationOn, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (reQueueUsed) "Re-Queue Sudah Diaktifkan" else "Saya Sudah di Klinik — Aktifkan Re-Queue",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Cancel, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Batalkan Tiket / Ambil Tiket Baru", color = TextSecondary, fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun QueueOrderItem(number: Int, ticket: String, subtitle: String, badge: String, badgeColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderLight, RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(28.dp).clip(CircleShape).background(BlueLight),
            contentAlignment = Alignment.Center
        ) { Text("$number", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(ticket, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(subtitle, fontSize = 11.sp, color = TextSecondary)
        }
        Box(
            modifier = Modifier.background(badgeColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(badge, color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}
