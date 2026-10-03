package com.qlinic.app.viewmodel

import androidx.lifecycle.ViewModel
import com.qlinic.app.data.model.ClinicItem
import com.qlinic.app.data.model.Patient
import com.qlinic.app.data.model.QueueStatus
import com.qlinic.app.data.model.QueueTicket
import com.qlinic.app.data.repository.QueueRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueViewModel : ViewModel() {

    // Clinic list from repository
    val clinicList: List<ClinicItem>
        get() = QueueRepository.clinicList
    val patient: Patient = QueueRepository.currentPatient

    // Editable profile state
    private val _profileName = MutableStateFlow(QueueRepository.currentPatient.name)
    val profileName: StateFlow<String> = _profileName.asStateFlow()

    private val _profilePhone = MutableStateFlow("0812-3456-7890")
    val profilePhone: StateFlow<String> = _profilePhone.asStateFlow()

    private val _profileEmail = MutableStateFlow("iqbal.ramadhan@email.com")
    val profileEmail: StateFlow<String> = _profileEmail.asStateFlow()

    private val _profileAddress = MutableStateFlow("Jl. Mawar No. 12, Bogor")
    val profileAddress: StateFlow<String> = _profileAddress.asStateFlow()

    fun updateProfile(name: String, phone: String, email: String, address: String) {
        _profileName.value = name
        _profilePhone.value = phone
        _profileEmail.value = email
        _profileAddress.value = address
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

    /**
     * Simulator: force a specific queue state for testing
     */
    fun setSimulatedState(status: QueueStatus) {
        QueueRepository.setStatus(status)
    }

    /**
     * Re-Queue: one-time activation after MISSED state
     */
    fun activateReQueue() {
        if (!ticket.value.reQueueUsed) {
            QueueRepository.activateReQueue()
        }
    }
}
