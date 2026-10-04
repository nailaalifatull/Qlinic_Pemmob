package com.qlinic.app.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qlinic.app.ui.theme.BackgroundGray
import com.qlinic.app.ui.theme.BlueDark
import com.qlinic.app.ui.theme.BluePrimary
import com.qlinic.app.ui.theme.PinkPrimary
import com.qlinic.app.ui.theme.RedError
import com.qlinic.app.ui.theme.RedLight
import com.qlinic.app.ui.theme.TextMuted
import com.qlinic.app.ui.theme.TextPrimary
import com.qlinic.app.ui.theme.TextSecondary
import com.qlinic.app.ui.theme.White
import com.qlinic.app.viewmodel.AuthResult
import com.qlinic.app.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmError by remember { mutableStateOf("") }

    val authResult by viewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        if (authResult is AuthResult.Success) {
            viewModel.resetResult()
            onRegisterSuccess()
        }
    }

    fun validate(): Boolean {
        nameError = ""; emailError = ""; passwordError = ""; confirmError = ""
        var valid = true
        if (name.isBlank()) { nameError = "Nama wajib diisi."; valid = false }
        if (email.isBlank()) {
            emailError = "Email wajib diisi."; valid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            emailError = "Format email tidak valid."; valid = false
        }
        if (password.length < 6) { passwordError = "Password minimal 6 karakter."; valid = false }
        if (confirmPassword != password) { confirmError = "Konfirmasi password tidak cocok."; valid = false }
        return valid
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ──────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(BlueDark, BluePrimary)))
                .padding(horizontal = 20.dp, vertical = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.size(72.dp).clip(CircleShape).background(PinkPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Q", color = White, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp)
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "Daftar Akun Baru",
                    color = White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Text("Bergabung dengan Qlinic", color = White.copy(0.7f), fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Form card ────────────────────────────────────────────────────────────
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Buat Akun",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = TextPrimary
                )
                Text(
                    "Isi data di bawah untuk mendaftar.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Spacer(Modifier.height(24.dp))

                // Nama Lengkap
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = "" },
                    label = { Text("Nama Lengkap") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    isError = nameError.isNotEmpty(),
                    supportingText = {
                        if (nameError.isNotEmpty()) Text(nameError, color = RedError, fontSize = 11.sp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = "" },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.AlternateEmail,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    isError = emailError.isNotEmpty(),
                    supportingText = {
                        if (emailError.isNotEmpty()) Text(emailError, color = RedError, fontSize = 11.sp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; passwordError = "" },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    isError = passwordError.isNotEmpty(),
                    supportingText = {
                        if (passwordError.isNotEmpty()) {
                            Text(passwordError, color = RedError, fontSize = 11.sp)
                        } else {
                            Text("Minimal 6 karakter", fontSize = 11.sp, color = TextMuted)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                // Konfirmasi Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it; confirmError = "" },
                    label = { Text("Konfirmasi Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmVisible = !confirmVisible }) {
                            Icon(
                                if (confirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (confirmVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    isError = confirmError.isNotEmpty(),
                    supportingText = {
                        if (confirmError.isNotEmpty()) Text(confirmError, color = RedError, fontSize = 11.sp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BluePrimary,
                        focusedLabelColor = BluePrimary
                    ),
                    singleLine = true
                )

                // Auth error banner
                if (authResult is AuthResult.Error) {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(RedLight, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            (authResult as AuthResult.Error).message,
                            color = RedError,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Register button
                Button(
                    onClick = { if (validate()) viewModel.register(name, email, password) },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                    enabled = authResult !is AuthResult.Loading
                ) {
                    if (authResult is AuthResult.Loading) {
                        CircularProgressIndicator(
                            color = White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Daftar Sekarang", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                Spacer(Modifier.height(20.dp))
                HorizontalDivider(color = BackgroundGray)
                Spacer(Modifier.height(16.dp))

                // Login link
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sudah punya akun?", fontSize = 13.sp, color = TextSecondary)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Masuk",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BluePrimary,
                        modifier = Modifier.clickable {
                            viewModel.resetResult()
                            onNavigateToLogin()
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(40.dp))
    }
}
