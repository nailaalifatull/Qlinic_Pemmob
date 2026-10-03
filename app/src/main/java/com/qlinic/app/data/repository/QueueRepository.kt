package com.qlinic.app.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.qlinic.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object QueueRepository {

    private const val TAG = "QueueRepository"
    private const val DATABASE_URL = "https://algifariprojects-default-rtdb.asia-southeast1.firebasedatabase.app"

    private val database: FirebaseDatabase by lazy {
        try {
            FirebaseDatabase.getInstance(DATABASE_URL)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize FirebaseDatabase with URL, falling back to default", e)
            FirebaseDatabase.getInstance()
        }
    }

    // Default fallback clinics
    private val defaultClinics = listOf(
        ClinicItem(
            id = "poli_umum",
            name = "Poli Umum",
            icon = "general",
            queueCode = "U",
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
            queueCode = "G",
            isOpen = true,
            doctor = DoctorInfo(
                name = "drg. Raka Pradipta",
                specialization = "Dokter Gigi",
                photoUrl = "",
                status = "Siap menerima pasien"
            ),
            schedule = "Senin-Jumat • 08.00-14.00 WIB",
            currentServing = "G-021",
            nextNumber = "G-022",
            totalQueue = 22,
            room = "Ruang 02",
            estimatedWaitMin = 30
        )
    )

    // Observable clinics state
    private val _clinics = MutableStateFlow(defaultClinics)
    val clinics: StateFlow<List<ClinicItem>> = _clinics.asStateFlow()

    // Backward-compatible property
    val clinicList: List<ClinicItem>
        get() = _clinics.value

    val currentPatient = Patient(
        name = "Iqbal Ramadhan",
        nik = "3201**********",
        bpjsVerified = true
    )

    private val defaultTicket = QueueTicket(
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

    private val _ticket = MutableStateFlow(defaultTicket)
    val ticket: StateFlow<QueueTicket> = _ticket.asStateFlow()

    init {
        setupFirebaseListeners()
    }

    private fun setupFirebaseListeners() {
        try {
            // 1. Listen to clinics
            database.getReference("clinics").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ClinicItem>()
                    for (child in snapshot.children) {
                        child.toClinicItem()?.let { list.add(it) }
                    }
                    if (list.isNotEmpty()) {
                        _clinics.value = list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Clinics listener cancelled: ${error.message}")
                }
            })

            // 2. Listen to active ticket
            database.getReference("current_queue/activeTicket").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.toQueueTicket()?.let {
                            _ticket.value = it
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(TAG, "Ticket listener cancelled: ${error.message}")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up Firebase listeners", e)
        }
    }

    fun setStatus(status: QueueStatus) {
        _ticket.value = _ticket.value.copy(status = status)
        try {
            database.getReference("current_queue/activeTicket/status").setValue(status.name)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update status in Firebase", e)
        }
    }

    fun resetTicket() {
        _ticket.value = defaultTicket
        try {
            database.getReference("current_queue/activeTicket").setValue(defaultTicket)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to reset ticket in Firebase", e)
        }
    }

    fun activateReQueue() {
        _ticket.value = _ticket.value.copy(
            status = QueueStatus.WAITING,
            reQueueUsed = true
        )
        try {
            val updates = mapOf<String, Any>(
                "status" to QueueStatus.WAITING.name,
                "reQueueUsed" to true
            )
            database.getReference("current_queue/activeTicket").updateChildren(updates)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update reQueue in Firebase", e)
        }
    }

    fun registerQueue(clinicId: String) {
        val selectedClinic = _clinics.value.find { it.id == clinicId } ?: defaultClinics[0]
        val prefix = if (selectedClinic.queueCode.isNotEmpty()) selectedClinic.queueCode else "U"
        val nextQueueNumber = "$prefix-${String.format("%03d", selectedClinic.totalQueue + 1)}"

        val newTicket = _ticket.value.copy(
            ticketNumber = nextQueueNumber,
            clinicName = selectedClinic.name,
            doctorName = selectedClinic.doctor.name.split(" ").take(2).joinToString(" "),
            doctorInfo = selectedClinic.doctor,
            room = selectedClinic.room,
            status = QueueStatus.WAITING,
            currentServing = selectedClinic.currentServing,
            nextNumber = selectedClinic.nextNumber,
            reQueueUsed = false
        )

        _ticket.value = newTicket
        try {
            database.getReference("current_queue/activeTicket").setValue(newTicket)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register queue in Firebase", e)
        }
    }

    // Helper parser functions
    private fun DataSnapshot.toClinicItem(): ClinicItem? {
        return try {
            val id = child("id").getValue(String::class.java) ?: key ?: ""
            val name = child("name").getValue(String::class.java) ?: ""
            val icon = child("icon").getValue(String::class.java) ?: "general"
            val queueCode = child("queueCode").getValue(String::class.java) ?: ""
            val isOpen = child("isOpen").getValue(Boolean::class.java) ?: true
            val doctor = child("doctor").getValue(DoctorInfo::class.java) ?: DoctorInfo()
            val schedule = child("schedule").getValue(String::class.java) ?: ""
            val currentServing = child("currentServing").getValue(String::class.java) ?: ""
            val nextNumber = child("nextNumber").getValue(String::class.java) ?: ""
            val totalQueue = (child("totalQueue").getValue(Long::class.java) ?: 0L).toInt()
            val room = child("room").getValue(String::class.java) ?: "Ruang 01"
            val estimatedWaitMin = (child("estimatedWaitMin").getValue(Long::class.java) ?: 10L).toInt()

            ClinicItem(
                id = id,
                name = name,
                icon = icon,
                queueCode = queueCode,
                isOpen = isOpen,
                doctor = doctor,
                schedule = schedule,
                currentServing = currentServing,
                nextNumber = nextNumber,
                totalQueue = totalQueue,
                room = room,
                estimatedWaitMin = estimatedWaitMin
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing ClinicItem", e)
            null
        }
    }

    private fun DataSnapshot.toQueueTicket(): QueueTicket? {
        return try {
            val ticketNumber = child("ticketNumber").getValue(String::class.java) ?: "U-040"
            val clinicName = child("clinicName").getValue(String::class.java) ?: "Poli Umum"
            val doctorName = child("doctorName").getValue(String::class.java) ?: "dr. Andini"
            val doctorInfo = child("doctorInfo").getValue(DoctorInfo::class.java) ?: DoctorInfo()
            val room = child("room").getValue(String::class.java) ?: "Ruang Periksa 1"
            val statusStr = child("status").getValue(String::class.java) ?: "WAITING"
            val status = try { QueueStatus.valueOf(statusStr) } catch (_: Exception) { QueueStatus.WAITING }
            val peopleAhead = (child("peopleAhead").getValue(Long::class.java) ?: 2L).toInt()
            val estimatedWaitMin = (child("estimatedWaitMin").getValue(Long::class.java) ?: 15L).toInt()
            val registrationTime = child("registrationTime").getValue(String::class.java) ?: "09:45 WIB"
            val callTime = child("callTime").getValue(String::class.java) ?: "09:42 WIB"
            val practiceSchedule = child("practiceSchedule").getValue(String::class.java) ?: ""
            val facility = child("facility").getValue(String::class.java) ?: ""
            val ticketCode = child("ticketCode").getValue(String::class.java) ?: ""
            val currentServing = child("currentServing").getValue(String::class.java) ?: "U-038"
            val nextNumber = child("nextNumber").getValue(String::class.java) ?: "U-039"
            val reQueueUsed = child("reQueueUsed").getValue(Boolean::class.java) ?: false
            val missedCount = (child("missedCount").getValue(Long::class.java) ?: 0L).toInt()

            QueueTicket(
                ticketNumber = ticketNumber,
                clinicName = clinicName,
                doctorName = doctorName,
                doctorInfo = doctorInfo,
                room = room,
                status = status,
                peopleAhead = peopleAhead,
                estimatedWaitMin = estimatedWaitMin,
                registrationTime = registrationTime,
                callTime = callTime,
                practiceSchedule = practiceSchedule,
                facility = facility,
                ticketCode = ticketCode,
                currentServing = currentServing,
                nextNumber = nextNumber,
                reQueueUsed = reQueueUsed,
                missedCount = missedCount
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing QueueTicket", e)
            null
        }
    }
}

