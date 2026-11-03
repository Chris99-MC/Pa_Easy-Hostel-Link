package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordDashboardScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val hostelsList = AppDataRepository.hostels
    val roomsList = AppDataRepository.rooms
    val applicationsList = AppDataRepository.applications

    // Dialog & Sheet States
    var showAddHostelSheet by remember { mutableStateOf(false) }
    var showEditHostelSheet by remember { mutableStateOf(false) }
    var selectedHostelForEdit by remember { mutableStateOf<HostelItem?>(null) }

    var showAddRoomSheet by remember { mutableStateOf(false) }
    var showAllocateModal by remember { mutableStateOf(false) }
    var activeAppForAllocation by remember { mutableStateOf<StudentApplicationItem?>(null) }
    var selectedRoomForAllocation by remember { mutableStateOf("") }

    var showAppDetailModal by remember { mutableStateOf(false) }
    var selectedAppForDetail by remember { mutableStateOf<StudentApplicationItem?>(null) }

    // New Hostel Form State
    var newHostelName by remember { mutableStateOf("") }
    var newHostelLocation by remember { mutableStateOf("") }
    var newHostelPrice by remember { mutableStateOf("") }
    var newHostelRooms by remember { mutableStateOf("10") }
    var newHostelDesc by remember { mutableStateOf("") }
    var newHostelPhone by remember { mutableStateOf("") }

    // New Room Form State
    var newRoomHostelName by remember { mutableStateOf("Sunrise Student Residence") }
    var newRoomNumber by remember { mutableStateOf("") }
    var newRoomType by remember { mutableStateOf("Single") }
    var newRoomCapacity by remember { mutableStateOf("1") }
    var newRoomPrice by remember { mutableStateOf("MK 50,000") }

    fun showToast(msg: String) {
        toastMessage = msg
        coroutineScope.launch {
            delay(2500)
            if (toastMessage == msg) toastMessage = null
        }
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp),
                color = Color(0xFFF3F4F6)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(72.dp)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        HostelLogo(size = 36.dp)

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pa_Easy Hostel Link",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                maxLines = 1
                            )
                            Text(
                                text = when(selectedTab) {
                                    0 -> "Dashboard Overview"
                                    1 -> "Rooms & Properties"
                                    2 -> "Student Requests"
                                    else -> "Settings"
                                },
                                fontSize = 12.sp,
                                color = Color(0xFF4B5563),
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { showToast("No new notifications") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = Color(0xFF4B5563),
                                modifier = Modifier.size(22.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                                    .align(Alignment.TopEnd)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB))
                                .clickable { selectedTab = 3 },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF3F4F6),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard", fontSize = 11.sp, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        unselectedIconColor = Color(0xFF4B5563),
                        unselectedTextColor = Color(0xFF4B5563),
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Apartment, contentDescription = "Rooms") },
                    label = { Text("Rooms", fontSize = 11.sp, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        unselectedIconColor = Color(0xFF4B5563),
                        unselectedTextColor = Color(0xFF4B5563),
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { 
                        BadgedBox(badge = { Badge { Text("${applicationsList.count { it.status == "PENDING" }}") } }) {
                            Icon(Icons.Default.AssignmentInd, contentDescription = "Requests")
                        }
                    },
                    label = { Text("Requests", fontSize = 11.sp, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        unselectedIconColor = Color(0xFF4B5563),
                        unselectedTextColor = Color(0xFF4B5563),
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.ManageAccounts, contentDescription = "Settings") },
                    label = { Text("Settings", fontSize = 11.sp, fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF2563EB),
                        selectedTextColor = Color(0xFF2563EB),
                        unselectedIconColor = Color(0xFF4B5563),
                        unselectedTextColor = Color(0xFF4B5563),
                        indicatorColor = Color(0xFFDBEAFE)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> LandlordDashboardContent(
                    onNavigateToRooms = { selectedTab = 1 },
                    onNavigateToRequests = { selectedTab = 2 },
                    totalRooms = roomsList.size,
                    pendingAppsCount = applicationsList.count { it.status == "PENDING" },
                    onAddHostel = { showAddHostelSheet = true }
                )
                1 -> LandlordRoomsContent(
                    rooms = roomsList,
                    hostels = hostelsList,
                    onAddRoomClick = { showAddRoomSheet = true },
                    onAddHostelClick = { showAddHostelSheet = true },
                    onEditHostelClick = { hostel ->
                        selectedHostelForEdit = hostel
                        showEditHostelSheet = true
                    },
                    onToggleRoomStatus = { roomId -> AppDataRepository.toggleRoomStatus(roomId) }
                )
                2 -> LandlordApplicationsContent(
                    applications = applicationsList,
                    onApprove = { app ->
                        AppDataRepository.updateApplicationStatus(app.id, "APPROVED")
                        showToast("Approved application for ${app.studentName}")
                    },
                    onReject = { app ->
                        AppDataRepository.updateApplicationStatus(app.id, "REJECTED")
                        showToast("Rejected application for ${app.studentName}")
                    },
                    onAllocateClick = { app ->
                        activeAppForAllocation = app
                        showAllocateModal = true
                    },
                    onAppClick = { app ->
                        selectedAppForDetail = app
                        showAppDetailModal = true
                    }
                )
                3 -> LandlordSettingsContent(onLogout = onLogout)
            }

            // Toast feedback
            toastMessage?.let { msg ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF111827)
                ) {
                    Text(
                        text = msg,
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }

    val sheetTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF9FAFB),
        focusedContainerColor = Color(0xFFF9FAFB),
        unfocusedBorderColor = Color(0xFFE5E7EB),
        focusedBorderColor = Color(0xFF2563EB),
        unfocusedTextColor = Color(0xFF111827),
        focusedTextColor = Color(0xFF111827),
        unfocusedLabelColor = Color(0xFF4B5563),
        focusedLabelColor = Color(0xFF2563EB),
        unfocusedPlaceholderColor = Color(0xFF9CA3AF),
        focusedPlaceholderColor = Color(0xFF9CA3AF)
    )

    // --- ADD HOSTEL SHEET ---
    if (showAddHostelSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddHostelSheet = false },
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Register New Hostel Property", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                
                OutlinedTextField(
                    value = newHostelName,
                    onValueChange = { newHostelName = it },
                    label = { Text("Hostel Property Name") },
                    placeholder = { Text("e.g. Sunrise Student Residence") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newHostelLocation,
                    onValueChange = { newHostelLocation = it },
                    label = { Text("Location / Campus Zone") },
                    placeholder = { Text("e.g. Chikanda Urban (300m to Campus)") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newHostelPrice,
                    onValueChange = { newHostelPrice = it },
                    label = { Text("Monthly Price Range") },
                    placeholder = { Text("e.g. MK 45,000 - 65,000") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newHostelDesc,
                    onValueChange = { newHostelDesc = it },
                    label = { Text("Hostel Description & Amenities") },
                    placeholder = { Text("e.g. Fenced compound with 24/7 security guard and water supply.") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = newHostelPhone,
                    onValueChange = { newHostelPhone = it },
                    label = { Text("Contact Phone Number") },
                    placeholder = { Text("e.g. +265 99 123 4567") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = {
                        if (newHostelName.isNotBlank() && newHostelLocation.isNotBlank()) {
                            AppDataRepository.addHostel(
                                name = newHostelName,
                                location = newHostelLocation,
                                priceRange = if (newHostelPrice.isNotBlank()) newHostelPrice else "MK 45k - 60k",
                                totalRooms = newHostelRooms.toIntOrNull() ?: 10,
                                description = newHostelDesc,
                                phone = newHostelPhone
                            )
                            showAddHostelSheet = false
                            showToast("Hostel '$newHostelName' registered successfully!")
                            newHostelName = ""
                            newHostelLocation = ""
                            newHostelPrice = ""
                            newHostelDesc = ""
                            newHostelPhone = ""
                        } else {
                            showToast("Please enter hostel name and location")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Register Hostel", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    // --- EDIT HOSTEL SHEET ---
    selectedHostelForEdit?.let { hostel ->
        if (showEditHostelSheet) {
            var editName by remember { mutableStateOf(hostel.name) }
            var editLoc by remember { mutableStateOf(hostel.location) }
            var editPrice by remember { mutableStateOf(hostel.priceRange) }
            var editDesc by remember { mutableStateOf(hostel.description) }

            ModalBottomSheet(
                onDismissRequest = { showEditHostelSheet = false },
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                containerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "Edit Hostel Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Hostel Name") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = editLoc,
                        onValueChange = { editLoc = it },
                        label = { Text("Location") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = editPrice,
                        onValueChange = { editPrice = it },
                        label = { Text("Price Range") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = editDesc,
                        onValueChange = { editDesc = it },
                        label = { Text("Description") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Button(
                        onClick = {
                            AppDataRepository.updateHostel(hostel.id, editName, editLoc, editPrice, editDesc)
                            showEditHostelSheet = false
                            showToast("Updated ${hostel.name} details!")
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // --- ADD ROOM SHEET ---
    if (showAddRoomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddRoomSheet = false },
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(text = "Add New Room Unit", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

                Text(text = "Hostel Property", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    hostelsList.forEach { h ->
                        val selected = newRoomHostelName == h.name
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) Color(0xFF2563EB) else Color(0xFFE5E7EB),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { newRoomHostelName = h.name }
                        ) {
                            Text(
                                text = h.name.take(12) + "...",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else Color(0xFF0F172A),
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = newRoomNumber,
                    onValueChange = { newRoomNumber = it },
                    label = { Text("Room Number / Title") },
                    placeholder = { Text("e.g. Room 105") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = newRoomType,
                        onValueChange = { newRoomType = it },
                        label = { Text("Room Type") },
                        placeholder = { Text("Single / Double") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = newRoomCapacity,
                        onValueChange = { newRoomCapacity = it },
                        label = { Text("Bed Capacity") },
                        placeholder = { Text("1 or 2") },
                        colors = sheetTextFieldColors,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = newRoomPrice,
                    onValueChange = { newRoomPrice = it },
                    label = { Text("Monthly Rate (MK)") },
                    placeholder = { Text("e.g. MK 50,000") },
                    colors = sheetTextFieldColors,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = {
                        val cap = newRoomCapacity.toIntOrNull()
                        val isNegativePrice = newRoomPrice.contains("-")
                        if (newRoomNumber.isBlank()) {
                            showToast("Please enter a room number")
                        } else if (cap == null || cap <= 0) {
                            showToast("Bed capacity must be at least 1 bed")
                        } else if (isNegativePrice) {
                            showToast("Monthly rental amount cannot be negative")
                        } else {
                            AppDataRepository.addRoom(
                                hostelName = newRoomHostelName,
                                roomNumber = newRoomNumber,
                                type = newRoomType,
                                capacity = cap,
                                price = newRoomPrice
                            )
                            showAddRoomSheet = false
                            showToast("Added $newRoomNumber ($cap beds) to inventory!")
                            newRoomNumber = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Save Room to Inventory", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    // --- ALLOCATE ROOM MODAL ---
    if (showAllocateModal && activeAppForAllocation != null) {
        val app = activeAppForAllocation!!
        val availableRoomOpts = roomsList.filter { it.status == "VACANT" || it.status == "PARTIAL" }

        AlertDialog(
            onDismissRequest = { showAllocateModal = false },
            title = { Text("Allocate Room for ${app.studentName}", fontWeight = FontWeight.Bold, color = Color(0xFF111827)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Hostel: ${app.hostelName}", fontWeight = FontWeight.Medium, color = Color(0xFF4B5563))
                    Text("Select available bedspace:", fontWeight = FontWeight.SemiBold, color = Color(0xFF111827))
                    
                    if (availableRoomOpts.isEmpty()) {
                        Text("No vacant or partial rooms available. Please add a room first.", fontSize = 12.sp, color = Color(0xFFDC2626))
                    } else {
                        availableRoomOpts.forEach { rm ->
                            val optText = "${rm.roomNumber} (${rm.type} • ${rm.occupancy})"
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedRoomForAllocation = rm.roomNumber }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedRoomForAllocation == rm.roomNumber,
                                    onClick = { selectedRoomForAllocation = rm.roomNumber }
                                )
                                Text(text = optText, fontSize = 13.sp, color = Color(0xFF111827))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (selectedRoomForAllocation.isNotBlank()) {
                            AppDataRepository.allocateRoomToApplication(app.id, selectedRoomForAllocation)
                            showAllocateModal = false
                            showToast("Allocated $selectedRoomForAllocation to ${app.studentName}!")
                        } else {
                            showToast("Please select a room to allocate")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                ) {
                    Text("Confirm & Allocate", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAllocateModal = false }) {
                    Text("Cancel", color = Color(0xFF4B5563))
                }
            }
        )
    }

    // --- APPLICATION DETAIL MODAL ---
    selectedAppForDetail?.let { app ->
        if (showAppDetailModal) {
            AlertDialog(
                onDismissRequest = { showAppDetailModal = false },
                title = { Text("Application Details", fontWeight = FontWeight.Bold, color = Color(0xFF111827)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Student Name: ${app.studentName}", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text("Student ID: ${app.studentId}", fontSize = 13.sp, color = Color(0xFF4B5563))
                        Text("Phone: ${app.phone}", fontSize = 13.sp, color = Color(0xFF4B5563))
                        Text("Requested Hostel: ${app.hostelName}", fontSize = 13.sp, color = Color(0xFF4B5563))
                        Text("Room Preference: ${app.roomType}", fontSize = 13.sp, color = Color(0xFF4B5563))
                        Text("Academic Period: ${app.period}", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                        Text("Current Status: ${app.status}", fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            activeAppForAllocation = app
                            showAppDetailModal = false
                            showAllocateModal = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Allocate Room", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAppDetailModal = false }) {
                        Text("Close", color = Color(0xFF4B5563))
                    }
                }
            )
        }
    }
}

@Composable
fun LandlordDashboardContent(
    onNavigateToRooms: () -> Unit,
    onNavigateToRequests: () -> Unit,
    totalRooms: Int,
    pendingAppsCount: Int,
    onAddHostel: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFD1FAE5)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(14.dp))
                            Text(text = "Verified Portfolio Owner", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                        }
                    }

                    OutlinedButton(
                        onClick = onAddHostel,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF2563EB))
                    ) {
                        Text("+ Add Hostel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }
                }

                Text(text = "Welcome, Mr. J. Banda", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text(text = "Overview of property performance, room allocations, and student requests.", fontSize = 13.sp, color = Color(0xFF4B5563))

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFF10B981), CircleShape))
                            Column {
                                Text(text = "Semester 1 Intake Active", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                Text(text = "Near University Campus • High Demand Peak", fontSize = 11.sp, color = Color(0xFF4B5563))
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KPICard(title = "Hostels Managed", value = "${AppDataRepository.hostels.size}", subtitle = "Active properties", icon = Icons.Default.Apartment, modifier = Modifier.weight(1f))
                    KPICard(title = "Total Rooms", value = totalRooms.toString(), subtitle = "Active inventory", icon = Icons.Default.Bed, modifier = Modifier.weight(1f))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KPICard(title = "Occupancy", value = "72%", subtitle = "18/25 units", icon = Icons.Default.PieChart, modifier = Modifier.weight(1f))
                    KPICard(title = "Applications", value = pendingAppsCount.toString(), subtitle = "Pending vetting", icon = Icons.Default.Pending, modifier = Modifier.weight(1f), isAlert = true)
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.clickable { onNavigateToRooms() }
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFDBEAFE), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CorporateFare, contentDescription = null, tint = Color(0xFF2563EB))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Hostels & Rooms", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                Text(text = "Manage properties, $totalRooms rooms, view occupancy, and add inventory.", fontSize = 12.sp, color = Color(0xFF4B5563))
                            }
                        }
                        Button(
                            onClick = onNavigateToRooms,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Manage Inventory", fontWeight = FontWeight.Bold, color = Color.White)
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.clickable { onNavigateToRequests() }
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFD1FAE5), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFF059669))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "Applications & Allocations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                    Surface(shape = CircleShape, color = Color(0xFF10B981)) {
                                        Text(text = pendingAppsCount.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text(text = "$pendingAppsCount pending student requests awaiting vetting and room allocation.", fontSize = 12.sp, color = Color(0xFF4B5563))
                            }
                        }
                        Button(
                            onClick = onNavigateToRequests,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Review Applications ($pendingAppsCount)", fontWeight = FontWeight.Bold, color = Color.White)
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color.White)
                            Text(text = "Estimated Monthly Revenue", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    Text(text = "MK 1,120,000 / MK 1,450,000 max", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text(text = "Vacancies remain to reach full capacity. Filling these unlocks additional monthly yield.", fontSize = 12.sp, color = Color(0xFFDBEAFE))
                }
            }
        }
    }
}

@Composable
fun KPICard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isAlert: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, fontSize = 12.sp, color = Color(0xFF4B5563), fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(if (isAlert) Color(0xFFFEF3C7) else Color(0xFFDBEAFE), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = if (isAlert) Color(0xFFD97706) else Color(0xFF2563EB), modifier = Modifier.size(16.dp))
                }
            }
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF111827))
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF4B5563))
        }
    }
}

@Composable
fun LandlordRoomsContent(
    rooms: List<RoomItem>,
    hostels: List<HostelItem>,
    onAddRoomClick: () -> Unit,
    onAddHostelClick: () -> Unit,
    onEditHostelClick: (HostelItem) -> Unit,
    onToggleRoomStatus: (String) -> Unit
) {
    var selectedHostelFilter by remember { mutableStateOf("All Properties") }
    var selectedOccupancyFilter by remember { mutableStateOf("All") }

    val filteredRooms = remember(rooms.size, selectedHostelFilter, selectedOccupancyFilter) {
        rooms.filter { rm ->
            val matchesHostel = selectedHostelFilter == "All Properties" || rm.hostelName.contains(selectedHostelFilter, ignoreCase = true)
            val matchesOccupancy = when (selectedOccupancyFilter) {
                "Vacant" -> rm.status == "VACANT"
                "Partial" -> rm.status == "PARTIAL"
                "Full" -> rm.status == "FULL"
                else -> true
            }
            matchesHostel && matchesOccupancy
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Hostels & Rooms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text(text = "Manage properties, room setups, and live occupancy", fontSize = 13.sp, color = Color(0xFF4B5563))
                    }
                    Button(
                        onClick = onAddRoomClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("+ Add Room", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Registered Hostels Cards (PHL_HOSTEL_004 Filter)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Registered Properties (${hostels.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    if (selectedHostelFilter != "All Properties") {
                        Text(
                            text = "Reset Filter",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            modifier = Modifier.clickable { selectedHostelFilter = "All Properties" }
                        )
                    }
                }

                hostels.forEach { h ->
                    val isSelected = selectedHostelFilter == h.name
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFDBEAFE) else Color.White),
                        border = if (isSelected) BorderStroke(1.5.dp, Color(0xFF2563EB)) else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedHostelFilter = if (isSelected) "All Properties" else h.name
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(text = h.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                                    if (isSelected) {
                                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFF2563EB)) {
                                            Text(
                                                text = "Active Filter",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(text = "${h.location} • ${h.priceRange}", fontSize = 12.sp, color = Color(0xFF4B5563))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = onAddRoomClick,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color(0xFF2563EB)),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("+ Room", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                                }
                                IconButton(onClick = { onEditHostelClick(h) }) {
                                    Icon(Icons.Outlined.Edit, contentDescription = "Edit Hostel", tint = Color(0xFF2563EB))
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Room Inventory (${filteredRooms.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    OutlinedButton(
                        onClick = onAddRoomClick,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF2563EB)),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("+ Add Room", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }
                }

                // Room Status Filter Chips (PHL_ROOM_004)
                val statusFilters = listOf("All", "Vacant", "Partial", "Full")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    statusFilters.forEach { st ->
                        val isSelected = selectedOccupancyFilter == st
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedOccupancyFilter = st },
                            label = { Text(st, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFE5E7EB),
                                labelColor = Color(0xFF0F172A),
                                selectedContainerColor = Color(0xFF2563EB),
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(50.dp)
                        )
                    }
                }
            }
        }

        if (filteredRooms.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No rooms found for selected filters",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Text(
                            text = "Try resetting filters or adding new room units to your inventory.",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563)
                        )
                        Button(
                            onClick = onAddRoomClick,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Add Room Unit", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        items(filteredRooms) { room ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFDBEAFE), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = room.roomNumber.takeLast(3), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                            }
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = room.price,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF2563EB)
                                )
                                Text(
                                    text = room.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFF3F4F6)) {
                                    Text(
                                        text = "${room.type} • Status: ${room.status}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (room.status == "VACANT") Color(0xFF059669) else Color(0xFF2563EB),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDBEAFE)
                            ) {
                                Text(
                                    text = room.occupancy,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { onToggleRoomStatus(room.id) }) {
                            Text("Toggle Occupancy Status", fontSize = 12.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LandlordApplicationsContent(
    applications: List<StudentApplicationItem>,
    onApprove: (StudentApplicationItem) -> Unit,
    onReject: (StudentApplicationItem) -> Unit,
    onAllocateClick: (StudentApplicationItem) -> Unit,
    onAppClick: (StudentApplicationItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(text = "Applications & Allocation", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text(text = "Review submitted student applications and allocate available rooms.", fontSize = 13.sp, color = Color(0xFF4B5563))
            }
        }

        items(applications) { application ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAppClick(application) }
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = application.studentName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                            Text(text = "ID: ${application.studentId} • ${application.hostelName}", fontSize = 12.sp, color = Color(0xFF4B5563))
                        }

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = when (application.status) {
                                "APPROVED" -> Color(0xFFD1FAE5)
                                "ALLOCATED" -> Color(0xFFDBEAFE)
                                "REJECTED" -> Color(0xFFFEE2E2)
                                else -> Color(0xFFFEF3C7)
                            }
                        ) {
                            Text(
                                text = application.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (application.status) {
                                    "APPROVED" -> Color(0xFF059669)
                                    "ALLOCATED" -> Color(0xFF2563EB)
                                    "REJECTED" -> Color(0xFFDC2626)
                                    else -> Color(0xFFD97706)
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(text = "Pref: ${application.roomType} (${application.price})", fontSize = 12.sp, color = Color(0xFF4B5563))
                    Text(text = "Phone: ${application.phone}", fontSize = 12.sp, color = Color(0xFF4B5563))

                    if (application.allocatedRoom.isNotBlank()) {
                        Text(text = "Allocated: ${application.allocatedRoom}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (application.status == "PENDING") {
                            OutlinedButton(
                                onClick = { onReject(application) },
                                modifier = Modifier.weight(1f).height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFDC2626))
                            ) {
                                Text("Reject", color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { onApprove(application) },
                                modifier = Modifier.weight(1f).height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                            ) {
                                Text("Approve", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }

                        Button(
                            onClick = { onAllocateClick(application) },
                            modifier = Modifier.weight(1.2f).height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                        ) {
                            Text("Allocate Room", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LandlordSettingsContent(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Landlord Account & Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Mr. J. Banda", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                Text(text = "Portfolio Owner • 3 Properties", fontSize = 13.sp, color = Color(0xFF4B5563))
                Text(text = "Email: landlord@campus.edu", fontSize = 13.sp, color = Color(0xFF4B5563))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}
