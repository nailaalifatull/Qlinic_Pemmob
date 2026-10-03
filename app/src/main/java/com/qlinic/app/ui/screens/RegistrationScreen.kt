package com.qlinic.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.data.model.ClinicItem
import com.qlinic.app.data.model.Patient
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDeep
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.BorderLight
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.PinkLight
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.ui.theme.YellowAccent
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun RegistrationScreen(
    viewModel: QueueViewModel,
    onConfirmed: () -> Unit
) {
    val selectedId by viewModel.selectedClinicId.collectAsState()
    var showConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = BackgroundGray
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(BlueDark, BluePrimary)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(PinkPrimary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Q", color = White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("Qlinic", color = White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Row {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.Help, contentDescription = null, tint = White.copy(alpha = 0.8f))
                                }
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(White.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = White, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "ANTREAN RAWAT JALAN HARI INI",
                            color = White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            "Halo, ${viewModel.patient.name}",
                            color = White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Pilih layanan poliklinik untuk pendaftaran antrean kunjungan hari ini.",
                            color = White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(White.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = YellowAccent, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Dokter ditetapkan otomatis sesuai jadwal praktik", color = White, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Section title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Pilih Layanan Poliklinik",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        "${viewModel.clinicList.size} Layanan Tersedia",
                        color = BluePrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium
                    )
                }
            }

            // Clinic cards
            items(viewModel.clinicList) { clinic ->
                ClinicCard(
                    clinic = clinic,
                    isSelected = selectedId == clinic.id,
                    onClick = {
                        viewModel.selectClinic(clinic.id)
                        showConfirmation = true
                    }
                )
                Spacer(Modifier.height(12.dp))
            }

            // Confirmation card
            item {
                AnimatedVisibility(
                    visible = showConfirmation && selectedId != null,
                    enter = fadeIn() + expandVertically()
                ) {
                    val clinic = viewModel.clinicList.find { it.id == selectedId }
                    if (clinic != null) {
                        ConfirmationCard(
                            clinic = clinic,
                            patient = viewModel.patient,
                            onConfirm = {
                                viewModel.confirmRegistration()
                                onConfirmed()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClinicCard(clinic: ClinicItem, isSelected: Boolean, onClick: () -> Unit) {
    val borderColor = if (isSelected) BluePrimary else BorderLight
    val bgColor = if (isSelected) BlueLight else White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (clinic.icon == "dental") Icons.Default.Spa else Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(clinic.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                        Text("Kode antrean: ${clinic.queueCode}", fontSize = 11.sp, color = TextSecondary)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(GreenLight, RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(GreenSuccess)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Buka", color = GreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    if (isSelected) {
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(24.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))

            // Doctor info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PinkLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PinkPrimary, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(clinic.doctor.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextPrimary)
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(BlueLight, RoundedCornerShape(10.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("Dokter Bertugas", color = BluePrimary, fontSize = 9.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    Text(clinic.schedule, fontSize = 11.sp, color = TextSecondary)
                }
            }

            Spacer(Modifier.height(12.dp))

            // Queue numbers row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundGray, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    QueueStat("Sedang Dilayani", clinic.currentServing, BluePrimary)
                    QueueStat("Berikutnya", clinic.nextNumber, TextSecondary)
                    QueueStat("Total Berjalan", clinic.totalQueue.toString(), TextSecondary)
                    QueueStat("Estimasi", "~${clinic.estimatedWaitMin} mnt", YellowAccent)
                }
            }
        }
    }
}

@Composable
fun QueueStat(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, color = TextSecondary, textAlign = TextAlign.Center)
        Spacer(Modifier.height(2.dp))
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

@Composable
fun ConfirmationCard(
    clinic: ClinicItem,
    patient: Patient,
    onConfirm: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Ringkasan Konfirmasi Antrean", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
                Box(
                    modifier = Modifier
                        .background(BlueLight, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Hari Ini", color = BluePrimary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Ticket number display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(BlueDeep, BluePrimary)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Estimasi Nomor Antrean Anda", color = White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("Siap langsung didaftarkan", color = White.copy(alpha = 0.6f), fontSize = 10.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("U–040", color = White, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Detail rows
            ConfirmDetailRow("Nama Pasien", patient.name)
            ConfirmDetailRow("Nomor Identitas (NIK)", patient.nik)
            ConfirmDetailRow("Poli Tujuan", clinic.name, valueColor = BluePrimary)
            ConfirmDetailRow("Dokter Praktik", "${clinic.doctor.name.split(" ").take(2).joinToString(" ")} (Umum)")
            ConfirmDetailRow("Tanggal Kunjungan", "Hari Ini (Senin, 24 Mei)")
            ConfirmDetailRow("Jam Operasional", "08.00 - 14.00 WIB")

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueLight, RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Shield, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Sistem Qlinic mengalokasikan dokter secara adil & langsung terkoneksi dengan rekam medis Anda tanpa biaya administrasi tambahan.",
                    fontSize = 11.sp,
                    color = BluePrimary
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
            ) {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Konfirmasi & Ambil Tiket U-040", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(Modifier.height(8.dp))
            Text(
                "Tiket antrean digital akan diterbitkan otomatis setelah konfirmasi.",
                fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ConfirmDetailRow(label: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = TextSecondary)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = valueColor)
    }
    HorizontalDivider(color = BorderLight)
}
