package com.qlinic.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.qlinic.app.data.model.ClinicItem
import com.qlinic.app.data.model.Patient
import com.qlinic.app.data.model.QueueTicket
import com.qlinic.app.data.repository.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance(
        "https://algifariprojects-default-rtdb.asia-southeast1.firebasedatabase.app"
    )

    // Clinic list from repository
    val clinicList: List<ClinicItem>
        get() = QueueRepository.clinicList
    val patient: Patient = QueueRepository.currentPatient

    // Profile data — loaded from Firebase RTDB users/{uid}
    private val _profileName = MutableStateFlow("")
    val profileName: StateFlow<String> = _profileName.asStateFlow()

    private val _profilePhone = MutableStateFlow("")
    val profilePhone: StateFlow<String> = _profilePhone.asStateFlow()

    // Email is read-only from FirebaseAuth
    private val _profileEmail = MutableStateFlow(auth.currentUser?.email ?: "")
    val profileEmail: StateFlow<String> = _profileEmail.asStateFlow()

    private val _profileAddress = MutableStateFlow("")
    val profileAddress: StateFlow<String> = _profileAddress.asStateFlow()

    // RTDB listener — kept so we can remove it in onCleared
    private var profileListener: ValueEventListener? = null
    private var profileRef: DatabaseReference? = null

    init {
        loadUserProfile()
    }

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

    // Currently active ticket - wired to repository
    val ticket: StateFlow<QueueTicket> = QueueRepository.ticket

    // Selected clinic during registration
    private val _selectedClinicId = MutableStateFlow<String?>(null)
    val selectedClinicId: StateFlow<String?> = _selectedClinicId.asStateFlow()

    // Registration confirmed
    private val _registrationConfirmed = MutableStateFlow(false)
    val registrationConfirmed: StateFlow<Boolean> = _registrationConfirmed.asStateFlow()

    fun selectClinic(clinicId: String) {
        _selectedClinicId.value = clinicId
    }

    fun confirmRegistration() {
        _registrationConfirmed.value = true
        _selectedClinicId.value?.let { clinicId ->
            QueueRepository.registerQueue(clinicId)
        }
    }

    fun cancelQueue() {
        _registrationConfirmed.value = false
        _selectedClinicId.value = null
        QueueRepository.resetTicket()
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the RTDB listener to prevent memory leaks
        profileListener?.let { profileRef?.removeEventListener(it) }
    }
}
