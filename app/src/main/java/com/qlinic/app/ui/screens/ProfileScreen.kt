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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun ProfileScreen(viewModel: QueueViewModel) {
    val name by viewModel.profileName.collectAsState()
    val phone by viewModel.profilePhone.collectAsState()
    val email by viewModel.profileEmail.collectAsState()
    val address by viewModel.profileAddress.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editName by remember(name) { mutableStateOf(name) }
    var editPhone by remember(phone) { mutableStateOf(phone) }
    var editEmail by remember(email) { mutableStateOf(email) }
    var editAddress by remember(address) { mutableStateOf(address) }

    // Compute initials from name
    val initials = name.trim().split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }.joinToString("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Header gradient ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BlueDark, BluePrimary)))
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(84.dp)
                        .clip(CircleShape)
                        .background(White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = initials.ifEmpty { "?" },
                        color = White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    )
                }
                Spacer(Modifier.height(12.dp))
                Text(name, color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(viewModel.patient.nik, color = White.copy(0.7f), fontSize = 13.sp)
                Spacer(Modifier.height(10.dp))
                // BPJS badge
                Row(
                    modifier = Modifier
                        .background(White.copy(0.2f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = GreenSuccess, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Pasien BPJS Terverifikasi", color = White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Info / Edit Card ----
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (isEditing) "Edit Profil" else "Informasi Profil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    if (!isEditing) {
                        IconButton(
                            onClick = {
                                editName = name
                                editPhone = phone
                                editEmail = email
                                editAddress = address
                                isEditing = true
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlueLight)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BluePrimary, modifier = Modifier.size(18.dp))
                        }
                    } else {
                        IconButton(
                            onClick = { isEditing = false },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BackgroundGray)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Batal", tint = TextSecondary, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(Modifier.height(16.dp))

                if (!isEditing) {
                    // View mode
                    ProfileInfoRow(Icons.Default.Person, "Nama Lengkap", name)
                    ProfileInfoRow(Icons.Default.Badge, "NIK", viewModel.patient.nik)
                    ProfileInfoRow(Icons.Default.Phone, "No. Telepon", phone)
                    ProfileInfoRow(Icons.Default.AlternateEmail, "Email", email)
                    ProfileInfoRow(Icons.Default.Home, "Alamat", address)
                    ProfileInfoRow(Icons.Default.Shield, "Status BPJS", "Aktif / Terverifikasi", valueColor = GreenSuccess)
                } else {
                    // Edit mode
                    ProfileEditField(label = "Nama Lengkap", value = editName, icon = Icons.Default.Person, onValueChange = { editName = it })
                    Spacer(Modifier.height(12.dp))
                    ProfileEditField(label = "No. Telepon", value = editPhone, icon = Icons.Default.Phone, onValueChange = { editPhone = it })
                    Spacer(Modifier.height(12.dp))
                    ProfileEditField(label = "Email", value = editEmail, icon = Icons.Default.AlternateEmail, onValueChange = { editEmail = it })
                    Spacer(Modifier.height(12.dp))
                    ProfileEditField(label = "Alamat", value = editAddress, icon = Icons.Default.Home, onValueChange = { editAddress = it })
                    Spacer(Modifier.height(20.dp))

                    // Save button
                    Button(
                        onClick = {
                            viewModel.updateProfile(editName, editPhone, editEmail, editAddress)
                            isEditing = false
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Simpan Perubahan", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ---- Keanggotaan / Status card ----
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Fasilitas & Layanan", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(Modifier.height(14.dp))
                ProfileInfoRow(Icons.Default.LocalHospital, "Klinik Terdaftar", "Klinik Pratama Qlinic")
                ProfileInfoRow(Icons.Default.Shield, "Jenis Asuransi", "BPJS Kesehatan")
                ProfileInfoRow(Icons.Default.VerifiedUser, "Status Keanggotaan", "Aktif", valueColor = GreenSuccess)
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String, valueColor: Color = TextPrimary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(BlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextSecondary)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = valueColor)
        }
    }
    HorizontalDivider(color = BackgroundGray, modifier = Modifier.padding(start = 46.dp))
}

@Composable
fun ProfileEditField(label: String, value: String, icon: ImageVector, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BluePrimary,
            unfocusedBorderColor = BackgroundGray,
            focusedLabelColor = BluePrimary
        ),
        singleLine = label != "Alamat",
        maxLines = if (label == "Alamat") 2 else 1
    )
}
