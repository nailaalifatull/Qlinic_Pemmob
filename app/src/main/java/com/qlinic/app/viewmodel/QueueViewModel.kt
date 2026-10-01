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
    val clinicList: List<ClinicItem> = QueueRepository.clinicList
    val patient: Patient = QueueRepository.currentPatient

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
    }

    fun cancelQueue() {
        _registrationConfirmed.value = false
        _selectedClinicId.value = null
        QueueRepository.setStatus(QueueStatus.WAITING)
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
