package com.qlinic.app.data.repository

import com.qlinic.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object QueueRepository {

    // Mock clinic list (simulates Firebase data)
    val clinicList: List<ClinicItem> = listOf(
        ClinicItem(
            id = "poli_umum",
            name = "Poli Umum",
            icon = "general",
            queueCode = "U-xxx",
            isOpen = true,
            doctor = DoctorInfo(
                name = "dr. Andini Kusumawardani",
                specialization = "Spesialis Kedokteran Keluarga",
                photoUrl = "",
                status = "Siap menerima pasien"
            ),
            schedule = "Senin-Jumat • 08.00-14.00 WIB",
            currentServing = "U-038",
            nextNumber = "U-039",
            totalQueue = 39,
            room = "Ruang 01",
            estimatedWaitMin = 15
        ),
        ClinicItem(
            id = "poli_gigi",
            name = "Poli Gigi",
            icon = "dental",
            queueCode = "G-xxx",
            isOpen = true,
            doctor = DoctorInfo(
                name = "drg. Raka Pradipta",
                specialization = "Dokter Gigi",
                photoUrl = "",
                status = "Siap menerima pasien"
            ),
            schedule = "Senin, Rabu, Jumat • 09.00-15.00 WIB",
            currentServing = "G-021",
            nextNumber = "G-022",
            totalQueue = 22,
            room = "Ruang 02",
            estimatedWaitMin = 30
        )
    )

    val currentPatient = Patient(
        name = "Iqbal Ramadhan",
        nik = "3201**********",
        bpjsVerified = true
    )

    private val _ticket = MutableStateFlow(
        QueueTicket(
            ticketNumber = "U-040",
            clinicName = "Poli Umum",
            doctorName = "dr. Andini",
            doctorInfo = DoctorInfo(
                name = "dr. Andini Kusumawardani",
                specialization = "Spesialis Kedokteran Keluarga",
                photoUrl = "",
                status = "Siap menerima pasien"
            ),
            room = "Ruang Periksa 1",
            status = QueueStatus.WAITING,
            peopleAhead = 2,
            estimatedWaitMin = 15,
            currentServing = "U-038",
            nextNumber = "U-039"
        )
    )
    val ticket: StateFlow<QueueTicket> = _ticket

    fun setStatus(status: QueueStatus) {
        _ticket.value = _ticket.value.copy(status = status)
    }

    fun resetTicket() {
        _ticket.value = QueueTicket(
            ticketNumber = "U-040",
            clinicName = "Poli Umum",
            doctorName = "dr. Andini",
            doctorInfo = DoctorInfo(
                name = "dr. Andini Kusumawardani",
                specialization = "Spesialis Kedokteran Keluarga",
                photoUrl = "",
                status = "Siap menerima pasien"
            ),
            room = "Ruang Periksa 1",
            status = QueueStatus.WAITING,
            peopleAhead = 2,
            estimatedWaitMin = 15,
            currentServing = "U-038",
            nextNumber = "U-039"
        )
    }

    fun activateReQueue() {
        _ticket.value = _ticket.value.copy(
            status = QueueStatus.WAITING,
            reQueueUsed = true
        )
    }
}
