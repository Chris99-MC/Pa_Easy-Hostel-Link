package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf

data class HostelItem(
    val id: String,
    var name: String,
    var location: String,
    var priceRange: String,
    var totalRooms: Int,
    var availableSpaces: Int,
    var distance: String,
    var accredited: Boolean = true,
    var features: List<String> = listOf("Fenced Compound", "Security Guard 24/7", "Wi-Fi"),
    var description: String = "Accredited student housing near campus with 24/7 security and essential study utilities.",
    var contactPhone: String = "+265 99 123 4567"
)

data class RoomItem(
    val id: String,
    var hostelName: String,
    var roomNumber: String,
    var type: String = "Single", // "Single", "Double", "Suite"
    var capacity: Int = 1, // Total bedspaces
    var occupied: Int = 0, // Occupied bedspaces
    var price: String = "MK 50,000",
    var status: String = "VACANT" // "VACANT", "PARTIAL", "FULL"
) {
    val title: String get() = "$hostelName • $roomNumber"
    val occupancy: String get() = "$occupied / $capacity Beds"
    val category: String get() = status.lowercase()
}

data class StudentApplicationItem(
    val id: String,
    var studentName: String,
    var studentId: String,
    var hostelName: String,
    var roomType: String,
    var phone: String,
    var period: String,
    var status: String = "PENDING", // "PENDING", "APPROVED", "ALLOCATED", "REJECTED"
    var allocatedRoom: String = ""
) {
    val name: String get() = studentName
    val hostel: String get() = hostelName
    val roomPref: String get() = roomType
    val price: String get() = "MK 45,000 - 65,000"
}

object AppDataRepository {
    val hostels = mutableStateListOf(
        HostelItem(
            id = "H1",
            name = "Sunrise Student Residence",
            location = "Chikanda Urban (250m to Campus)",
            priceRange = "MK 45k - 65k",
            totalRooms = 10,
            availableSpaces = 5,
            distance = "250m",
            accredited = true,
            features = listOf("Fenced Compound", "Security Guard 24/7", "Maid Service", "Kitchen"),
            description = "High-security accredited residence featuring fully furnished single and double units close to campus.",
            contactPhone = "+265 99 111 2222"
        ),
        HostelItem(
            id = "H2",
            name = "Highland Villa",
            location = "Chikanda Market Area (400m to Campus)",
            priceRange = "MK 40k - 55k",
            totalRooms = 8,
            availableSpaces = 3,
            distance = "400m",
            accredited = true,
            features = listOf("Fenced Compound", "Borehole Water", "Wi-Fi"),
            description = "Quiet and conducive environment for studying with backup water supply.",
            contactPhone = "+265 88 333 4444"
        ),
        HostelItem(
            id = "H3",
            name = "Victoria Student Lodge",
            location = "Kanjedza Zone (600m to Campus)",
            priceRange = "MK 35k - 50k",
            totalRooms = 12,
            availableSpaces = 6,
            distance = "600m",
            accredited = false,
            features = listOf("Self contained", "Spacious Yard", "Security Light"),
            description = "Affordable student lodging with spacious rooms and self-contained amenities.",
            contactPhone = "+265 99 555 6666"
        )
    )

    val rooms = mutableStateListOf(
        RoomItem(
            id = "R101",
            hostelName = "Sunrise Student Residence",
            roomNumber = "Room 101",
            type = "Double",
            capacity = 2,
            occupied = 1,
            price = "MK 45,000",
            status = "PARTIAL"
        ),
        RoomItem(
            id = "R102",
            hostelName = "Sunrise Student Residence",
            roomNumber = "Room 102",
            type = "Single",
            capacity = 1,
            occupied = 0,
            price = "MK 65,000",
            status = "VACANT"
        ),
        RoomItem(
            id = "R104",
            hostelName = "Highland Villa",
            roomNumber = "Room 204",
            type = "Single",
            capacity = 1,
            occupied = 0,
            price = "MK 55,000",
            status = "VACANT"
        ),
        RoomItem(
            id = "R105",
            hostelName = "Victoria Student Lodge",
            roomNumber = "Room 301",
            type = "Double",
            capacity = 2,
            occupied = 2,
            price = "MK 35,000",
            status = "FULL"
        )
    )

    val applications = mutableStateListOf(
        StudentApplicationItem(
            id = "APP-101",
            studentName = "Chrispin Banda",
            studentId = "BED-2024-091",
            hostelName = "Highland Villa",
            roomType = "Room 204 (Single)",
            phone = "+265 99 123 4567",
            period = "Semester 1 (Sept 2024 - Jan 2025)",
            status = "PENDING",
            allocatedRoom = ""
        ),
        StudentApplicationItem(
            id = "APP-102",
            studentName = "Mercy Phiri",
            studentId = "BSC-2023-044",
            hostelName = "Sunrise Student Residence",
            roomType = "Room 101 (Double)",
            phone = "+265 88 444 5555",
            period = "Semester 1 (Sept 2024 - Jan 2025)",
            status = "APPROVED",
            allocatedRoom = "Room 101"
        )
    )

    fun addHostel(name: String, location: String, priceRange: String, totalRooms: Int, description: String, phone: String) {
        val newId = "H${hostels.size + 1}"
        hostels.add(
            HostelItem(
                id = newId,
                name = name,
                location = location,
                priceRange = priceRange,
                totalRooms = totalRooms,
                availableSpaces = totalRooms,
                distance = "300m",
                accredited = true,
                description = description,
                contactPhone = phone
            )
        )
    }

    fun updateHostel(id: String, name: String, location: String, priceRange: String, description: String) {
        val index = hostels.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = hostels[index]
            hostels[index] = old.copy(
                name = name,
                location = location,
                priceRange = priceRange,
                description = description
            )
        }
    }

    fun addRoom(hostelName: String, roomNumber: String, type: String, capacity: Int, price: String) {
        val newId = "R${System.currentTimeMillis() % 10000}"
        val initialStatus = "VACANT"
        val newRoom = RoomItem(
            id = newId,
            hostelName = hostelName,
            roomNumber = roomNumber,
            type = type,
            capacity = capacity,
            occupied = 0,
            price = price,
            status = initialStatus
        )
        rooms.add(newRoom)
    }

    fun toggleRoomStatus(roomId: String) {
        val index = rooms.indexOfFirst { it.id == roomId }
        if (index != -1) {
            val item = rooms[index]
            val newOccupied = if (item.occupied >= item.capacity) 0 else item.occupied + 1
            val newStatus = when {
                newOccupied == 0 -> "VACANT"
                newOccupied < item.capacity -> "PARTIAL"
                else -> "FULL"
            }
            rooms[index] = item.copy(occupied = newOccupied, status = newStatus)
        }
    }

    fun submitApplication(
        studentName: String,
        studentId: String,
        hostelName: String,
        roomType: String,
        phone: String,
        period: String
    ) {
        val newAppId = "APP-${applications.size + 101}"
        applications.add(
            0, // Add to top
            StudentApplicationItem(
                id = newAppId,
                studentName = studentName,
                studentId = studentId,
                hostelName = hostelName,
                roomType = roomType,
                phone = phone,
                period = period,
                status = "PENDING"
            )
        )
    }

    fun updateApplicationStatus(appId: String, newStatus: String) {
        val index = applications.indexOfFirst { it.id == appId }
        if (index != -1) {
            applications[index] = applications[index].copy(status = newStatus)
        }
    }

    fun allocateRoomToApplication(appId: String, roomOptName: String) {
        val index = applications.indexOfFirst { it.id == appId }
        if (index != -1) {
            val app = applications[index]
            applications[index] = app.copy(
                status = "ALLOCATED",
                allocatedRoom = roomOptName
            )
            // Also update room occupied count if matching room found
            val roomIndex = rooms.indexOfFirst { roomOptName.contains(it.roomNumber) || it.roomNumber.contains(roomOptName) }
            if (roomIndex != -1) {
                val rm = rooms[roomIndex]
                val updatedOccupied = (rm.occupied + 1).coerceAtMost(rm.capacity)
                val updatedStatus = when {
                    updatedOccupied == 0 -> "VACANT"
                    updatedOccupied < rm.capacity -> "PARTIAL"
                    else -> "FULL"
                }
                rooms[roomIndex] = rm.copy(occupied = updatedOccupied, status = updatedStatus)
            }
        }
    }
}
