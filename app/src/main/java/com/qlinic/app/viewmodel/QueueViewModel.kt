package com.qlinic.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.qlinic.app.data.model.ClinicItem
import com.qlinic.app.data.model.HistoryEntry
import com.qlinic.app.data.model.Patient
import com.qlinic.app.data.model.QueueTicket
import com.qlinic.app.data.repository.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QueueViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance(
        "https://algifariprojects-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    val clinics: StateFlow<List<ClinicItem>> = QueueRepository.clinics

    /** Backward-compat static accessor (used in ConfirmationCard) */
    val clinicList: List<ClinicItem>
        get() = QueueRepository.clinicList

    val patient: Patient
        get() = Patient(
            name = _profileName.value.ifEmpty { auth.currentUser?.displayName ?: "Pengguna" },
            phone = _profilePhone.value.ifEmpty { "—" },
            nik = "3201**********",
            bpjsVerified = true
        )

    // ── Active ticket ─────────────────────────────────────────────────────────
    val ticket: StateFlow<QueueTicket> = QueueRepository.ticket

    // ── Registration state ────────────────────────────────────────────────────
    private val _selectedClinicId = MutableStateFlow<String?>(null)
    val selectedClinicId: StateFlow<String?> = _selectedClinicId.asStateFlow()

    private val _registrationConfirmed = MutableStateFlow(false)
    val registrationConfirmed: StateFlow<Boolean> = _registrationConfirmed.asStateFlow()

    // ── Ticket end state — null = active, "SELESAI", "KADALUARSA" ─────────────
    private val _ticketEndState = MutableStateFlow<String?>(null)
    val ticketEndState: StateFlow<String?> = _ticketEndState.asStateFlow()

    // ── History ───────────────────────────────────────────────────────────────
    private val _historyList = MutableStateFlow<List<HistoryEntry>>(emptyList())
    val historyList: StateFlow<List<HistoryEntry>> = _historyList.asStateFlow()

    private var historyListener: ValueEventListener? = null
    private var historyRef: DatabaseReference? = null

    // ── Profile data — loaded from RTDB users/{uid} ───────────────────────────
    private val _profileName = MutableStateFlow("")
    val profileName: StateFlow<String> = _profileName.asStateFlow()

    private val _profilePhone = MutableStateFlow("")
    val profilePhone: StateFlow<String> = _profilePhone.asStateFlow()

    // Email is read-only from FirebaseAuth
    private val _profileEmail = MutableStateFlow(auth.currentUser?.email ?: "")
    val profileEmail: StateFlow<String> = _profileEmail.asStateFlow()

    private val _profileAddress = MutableStateFlow("")
    val profileAddress: StateFlow<String> = _profileAddress.asStateFlow()

    private var profileListener: ValueEventListener? = null
    private var profileRef: DatabaseReference? = null

    init {
        loadUserProfile()
        loadHistory()
    }

    // ── Profile ───────────────────────────────────────────────────────────────

    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        val ref = database.getReference("users/$uid")
        profileRef = ref
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _profileName.value = snapshot.child("nama").getValue(String::class.java) ?: ""
                _profilePhone.value = snapshot.child("phone").getValue(String::class.java) ?: ""
                _profileAddress.value = snapshot.child("address").getValue(String::class.java) ?: ""
            }
            override fun onCancelled(error: DatabaseError) { /* no-op */ }
        }
        profileListener = listener
        ref.addValueEventListener(listener)
    }

    /** Save editable profile fields (nama, phone, address) to Firebase RTDB. */
    fun updateProfile(name: String, phone: String, address: String) {
        val uid = auth.currentUser?.uid ?: return
        val updates = mapOf<String, Any>(
            "nama" to name.trim(),
            "phone" to phone.trim(),
            "address" to address.trim()
        )
        database.getReference("users/$uid").updateChildren(updates)
        // Optimistic local update so UI reflects immediately
        _profileName.value = name.trim()
        _profilePhone.value = phone.trim()
        _profileAddress.value = address.trim()
    }

    // ── History ───────────────────────────────────────────────────────────────

    private fun loadHistory() {
        val uid = auth.currentUser?.uid ?: return
        val ref = database.getReference("history/$uid")
        historyRef = ref
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<HistoryEntry>()
                for (child in snapshot.children) {
                    val ticketNumber = child.child("ticketNumber").getValue(String::class.java) ?: continue
                    val clinicName = child.child("clinicName").getValue(String::class.java) ?: ""
                    val doctorName = child.child("doctorName").getValue(String::class.java) ?: ""
                    val date = child.child("date").getValue(String::class.java) ?: ""
                    val time = child.child("time").getValue(String::class.java) ?: ""
                    val status = child.child("status").getValue(String::class.java) ?: ""
                    list.add(HistoryEntry(ticketNumber, clinicName, doctorName, date, time, status))
                }
                
                _historyList.value = list.reversed()
            }
            override fun onCancelled(error: DatabaseError) { /* no-op */ }
        }
        historyListener = listener
        ref.addValueEventListener(listener)
    }

    private fun archiveTicket(status: String) {
        val uid = auth.currentUser?.uid ?: return
        val t = QueueRepository.ticket.value
        if (t.ticketNumber.isEmpty()) return

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        val now = Date()
        val entry = mapOf(
            "ticketNumber" to t.ticketNumber,
            "clinicName" to t.clinicName,
            "doctorName" to t.doctorName,
            "date" to dateFormat.format(now),
            "time" to "${timeFormat.format(now)} WIB",
            "status" to status
        )
        database.getReference("history/$uid").push().setValue(entry)
        QueueRepository.clearLocalTicket()
    }

    fun completeTicket() {
        if (_ticketEndState.value != null) return // already ended
        archiveTicket("Selesai")
        _ticketEndState.value = "SELESAI"
        _registrationConfirmed.value = false
    }

    fun expireTicket() {
        if (_ticketEndState.value != null) return // already ended
        archiveTicket("Kadaluarsa")
        _ticketEndState.value = "KADALUARSA"
        _registrationConfirmed.value = false
    }

    fun resetTicketEndState() {
        _ticketEndState.value = null
    }

    // ── Queue registration ────────────────────────────────────────────────────

    fun selectClinic(clinicId: String) {
        _selectedClinicId.value = clinicId
    }

    fun confirmRegistration() {
        _registrationConfirmed.value = true
        _ticketEndState.value = null 
        _selectedClinicId.value?.let { QueueRepository.registerQueue(it) }
    }

    fun cancelQueue() {
        _registrationConfirmed.value = false
        _selectedClinicId.value = null
        QueueRepository.resetTicket()
    }

    override fun onCleared() {
        super.onCleared()
        profileListener?.let { profileRef?.removeEventListener(it) }
        historyListener?.let { historyRef?.removeEventListener(it) }
    }
}
