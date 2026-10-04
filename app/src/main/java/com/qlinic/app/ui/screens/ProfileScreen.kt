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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BlueLight
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.GreenLight
import com.qlinic.app.ui.theme.GreenSuccess
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.RedLight
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.AuthResult
import com.qlinic.app.viewmodel.AuthViewModel
import com.qlinic.app.viewmodel.QueueViewModel

@Composable
fun ProfileScreen(
    viewModel: QueueViewModel,
    onLogout: () -> Unit
) {
    // Get AuthViewModel for change-password and logout
    val authViewModel: AuthViewModel = viewModel()

    val name by viewModel.profileName.collectAsState()
    val phone by viewModel.profilePhone.collectAsState()
    val email by viewModel.profileEmail.collectAsState()
    val address by viewModel.profileAddress.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var editName by remember(name) { mutableStateOf(name) }
    var editPhone by remember(phone) { mutableStateOf(phone) }
    var editAddress by remember(address) { mutableStateOf(address) }

    // Change-password fields
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var changePasswordError by remember { mutableStateOf("") }

    val changePasswordResult by authViewModel.changePasswordResult.collectAsState()

    // Reset change-password fields on success
    LaunchedEffect(changePasswordResult) {
        if (changePasswordResult is AuthResult.Success) {
            oldPassword = ""; newPassword = ""; confirmNewPassword = ""; changePasswordError = ""
        }
    }

    // Compute initials from name
    val initials = name.trim().split(" ").take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                Text(name.ifEmpty { "Pengguna" }, color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
                                editAddress = address
                                isEditing = true
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BlueLight)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = BluePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    } else {
                        IconButton(
                            onClick = { isEditing = false },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(BackgroundGray)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Batal",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(Modifier.height(16.dp))

                if (!isEditing) {
                    // ── View mode ──
                    ProfileInfoRow(Icons.Default.Person, "Nama Lengkap", name.ifEmpty { "—" })
                    ProfileInfoRow(Icons.Default.Phone, "No. Telepon", phone.ifEmpty { "—" })
                    ProfileInfoRow(Icons.Default.AlternateEmail, "Email", email.ifEmpty { "—" })
                    ProfileInfoRow(Icons.Default.LocationOn, "Alamat", address.ifEmpty { "—" })
                } else {
                    // ── Edit mode ──
                    ProfileEditField(
                        label = "Nama Lengkap",
                        value = editName,
                        icon = Icons.Default.Person,
                        onValueChange = { editName = it }
                    )
                    Spacer(Modifier.height(12.dp))
                    ProfileEditField(
                        label = "No. Telepon",
                        value = editPhone,
                        icon = Icons.Default.Phone,
                        onValueChange = { editPhone = it }
                    )
                    Spacer(Modifier.height(12.dp))
                    // Email — read-only, displayed but not editable
                    ProfileInfoRow(Icons.Default.AlternateEmail, "Email (tidak dapat diubah)", email.ifEmpty { "—" })
                    Spacer(Modifier.height(12.dp))
                    ProfileEditField(
                        label = "Alamat",
                        value = editAddress,
                        icon = Icons.Default.LocationOn,
                        onValueChange = { editAddress = it }
                    )
                    Spacer(Modifier.height(20.dp))

                    // Save button
                    Button(
                        onClick = {
                            viewModel.updateProfile(editName, editPhone, editAddress)
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

        // ── Change Password Card ──────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(34.dp).clip(CircleShape).background(BlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.VpnKey,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text("Ganti Password", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(Modifier.height(16.dp))

                // Password Lama
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = {
                        oldPassword = it
                        changePasswordError = ""
                        authViewModel.resetChangePasswordResult()
                    },
                    label = { Text("Password Lama") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // Password Baru
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        changePasswordError = ""
                        authViewModel.resetChangePasswordResult()
                    },
                    label = { Text("Password Baru") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )
                Spacer(Modifier.height(12.dp))

                // Konfirmasi Password Baru
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = {
                        confirmNewPassword = it
                        changePasswordError = ""
                        authViewModel.resetChangePasswordResult()
                    },
                    label = { Text("Konfirmasi Password Baru") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = BluePrimary, modifier = Modifier.size(20.dp))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                // Feedback messages
                when (changePasswordResult) {
                    is AuthResult.Error -> {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(RedLight, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                (changePasswordResult as AuthResult.Error).message,
                                color = RedError,
                                fontSize = 13.sp
                            )
                        }
                    }
                    is AuthResult.Success -> {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GreenLight, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                "Password berhasil diperbarui.",
                                color = GreenSuccess,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    else -> {}
                }

                if (changePasswordError.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RedLight, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(changePasswordError, color = RedError, fontSize = 13.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Ganti Password button
                Button(
                    onClick = {
                        // Client-side validation before calling Firebase
                        when {
                            oldPassword.isBlank() -> changePasswordError = "Password lama wajib diisi."
                            newPassword.length < 6 -> changePasswordError = "Password baru minimal 6 karakter."
                            newPassword != confirmNewPassword -> changePasswordError = "Konfirmasi password baru tidak cocok."
                            else -> authViewModel.changePassword(oldPassword, newPassword)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    enabled = changePasswordResult !is AuthResult.Loading
                ) {
                    if (changePasswordResult is AuthResult.Loading) {
                        CircularProgressIndicator(
                            color = White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.VpnKey, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ganti Password", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Logout Button ────────────────────────────────────────────────────────
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedError)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = White)
            Spacer(Modifier.width(8.dp))
            Text("Keluar dari Akun", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = White)
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── Reusable composables (unchanged) ────────────────────────────────────────

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
