package com.qlinic.app.data.model

data class DoctorInfo(
    val name: String = "",
    val specialization: String = "",
    val photoUrl: String = "",
    val status: String = "Siap menerima pasien"
)

data class ClinicItem(
    val id: String = "",
    val name: String = "",
    val icon: String = "general",         // "general" | "dental"
    val queueCode: String = "",           // e.g. "U-xxx"
    val isOpen: Boolean = true,
    val doctor: DoctorInfo = DoctorInfo(),
    val schedule: String = "",            // e.g. "Senin-Jumat • 08.00-14.00 WIB"
    val currentServing: String = "",      // e.g. "U-038"
    val nextNumber: String = "",          // e.g. "U-039"
    val totalQueue: Int = 0,
    val room: String = "Ruang 01",
    val estimatedWaitMin: Int = 10        // estimated wait in minutes
)

data class Patient(
    val name: String = "Astria Rahmawati",
    val nik: String = "3201**********",
    val phone: String = "0812-3456-7890",
    val bpjsVerified: Boolean = true
)

enum class QueueStatus { WAITING, CALLED, MISSED, COMPLETED }

data class QueueTicket(
    val ticketNumber: String = "U-040",
    val clinicName: String = "Poli Umum",
    val doctorName: String = "dr. Andini",
    val doctorInfo: DoctorInfo = DoctorInfo(
        name = "dr. Andini Kusumawardani",
        specialization = "Spesialis Kedokteran Keluarga",
        photoUrl = "",
        status = "Siap menerima pasien"
    ),
    val room: String = "Ruang Periksa 1",
    val status: QueueStatus = QueueStatus.WAITING,
    val peopleAhead: Int = 2,
    val estimatedWaitMin: Int = 15,
    val registrationTime: String = "09:45 WIB",
    val callTime: String = "09:42 WIB",
    val practiceSchedule: String = "Senin, 24 Mei 2025",
    val facility: String = "Klinik Pratama Qlinic",
    val ticketCode: String = "QLN-U040-6524",
    val currentServing: String = "U-038",
    val nextNumber: String = "U-039",
    val reQueueUsed: Boolean = false,
    val missedCount: Int = 0
)
