package com.qlinic.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance(
        "https://algifariprojects-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    private val _authResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val authResult: StateFlow<AuthResult> = _authResult.asStateFlow()

    private val _changePasswordResult = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val changePasswordResult: StateFlow<AuthResult> = _changePasswordResult.asStateFlow()

    // ── Login ──────────────────────────────────────────────────────────────────

    fun login(email: String, password: String) {
        _authResult.value = AuthResult.Loading
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { _authResult.value = AuthResult.Success }
            .addOnFailureListener { _authResult.value = AuthResult.Error(mapAuthError(it.message)) }
    }

    // ── Register ───────────────────────────────────────────────────────────────

    fun register(name: String, email: String, password: String) {
        _authResult.value = AuthResult.Loading
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid != null) {
                    // Save nama to RTDB; succeed either way
                    database.getReference("users/$uid/nama").setValue(name.trim())
                        .addOnCompleteListener { _authResult.value = AuthResult.Success }
                } else {
                    _authResult.value = AuthResult.Success
                }
            }
            .addOnFailureListener { _authResult.value = AuthResult.Error(mapAuthError(it.message)) }
    }

    // ── Change password (re-auth first) ────────────────────────────────────────

    fun changePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser ?: return
        val email = user.email ?: return
        _changePasswordResult.value = AuthResult.Loading
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        user.reauthenticate(credential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener { _changePasswordResult.value = AuthResult.Success }
                    .addOnFailureListener {
                        _changePasswordResult.value = AuthResult.Error(mapAuthError(it.message))
                    }
            }
            .addOnFailureListener {
                _changePasswordResult.value = AuthResult.Error(mapAuthError(it.message))
            }
    }

    // ── Reset state ─────────────────────────────────────────────────────────────

    fun resetResult() { _authResult.value = AuthResult.Idle }
    fun resetChangePasswordResult() { _changePasswordResult.value = AuthResult.Idle }

    // ── Error mapping ───────────────────────────────────────────────────────────

    private fun mapAuthError(msg: String?): String = when {
        msg == null -> "Terjadi kesalahan, coba lagi."
        "email address is already in use" in msg -> "Email sudah terdaftar."
        "no user record" in msg
                || "password is invalid" in msg
                || "INVALID_LOGIN_CREDENTIALS" in msg -> "Email atau password salah."
        "badly formatted" in msg -> "Format email tidak valid."
        "network" in msg.lowercase() -> "Tidak ada koneksi internet."
        "wrong-password" in msg || "WRONG_PASSWORD" in msg -> "Password lama salah."
        else -> "Terjadi kesalahan. Coba lagi."
    }
}
