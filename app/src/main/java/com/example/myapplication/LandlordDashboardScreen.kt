package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
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

data class RoomItem(
    val id: String,
    val title: String,
    val status: String,
    val price: String,
    val occupancy: String,
    val category: String // "vacant", "partial", "full"
)

data class ApplicationItem(
    val name: String,
    val studentId: String,
    val hostel: String,
    val roomPref: String,
    val price: String,
    val phone: String,
    var status: String // "PENDING", "APPROVED", "REJECTED", "ALLOCATED"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordDashboardScreen(
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Dashboard, 1: Rooms, 2: Requests, 3: Settings

    // Rooms state
    val roomsList = remember {
        mutableStateListOf(
            RoomItem("101", "Shared Double Room", "Available (1 Space)", "MK 45,000", "1 / 2 Occupied", "partial"),
            RoomItem("102", "Single room Deluxe", "Occupied", "MK 65,000", "1 / 1 Occupied", "full"),
            RoomItem("103", "Standard Quad Room", "Full (4 / 4)", "MK 38,000", "4 / 4 Occupied", "full"),
            RoomItem("104", "Single room (Self contained)", "Vacant (Ready)", "MK 60,000", "0 / 1 Beds", "vacant"),
            RoomItem("105", "Shared Double Room", "Maintenance", "MK 45,000", "0 / 2 Occ.", "vacant")
        )
    }
    var roomFilter by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var showAddRoomSheet by remember { mutableStateOf(false) }
    var newRoomNumber by remember { mutableStateOf("") }
    var newRoomRate by remember { mutableStateOf("45000") }

    // Applications state
    val applicationsList = remember {
        mutableStateListOf(
            ApplicationItem("John Banda", "BED-2024-091", "Sunrise Residence", "Double room (Self contained)", "MK 45,000", "+265 991 234 567", "PENDING"),
            ApplicationItem("Mary Phiri", "MED-2023-118", "Sunrise Residence", "Room 102 (Private Single)", "MK 60,000", "+265 888 776 543", "APPROVED"),
            ApplicationItem("Kondwani Moyo", "ENG-2024-045", "Highland Villa", "Single room", "MK 38,000", "+265 994 402 119", "PENDING"),
            ApplicationItem("Eunice Kamanga", "LAW-2022-019", "Victoria Hall", "Room 104", "MK 55,000", "+265 881 123 990", "ALLOCATED")
        )
    }
    var appFilter by remember { mutableStateOf("all") }
    var showAllocateModal by remember { mutableStateOf(false) }
    var activeAppForAllocation by remember { mutableStateOf<ApplicationItem?>(null) }
    var selectedRoomForAllocation by remember { mutableStateOf("Room 101") }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var toastMessage by remember { mutableStateOf<String?>(null) }

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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Pa_Easy Hostel Link",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827),
                                    maxLines = 1
                                )
                            }
                            Text(
                                text = when(selectedTab) {
                                    0 -> "Dashboard"
                                    1 -> "Rooms & Inventory"
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
                                .background(Color(0xFF2563EB)),
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
                    pendingAppsCount = applicationsList.count { it.status == "PENDING" }
                )
                1 -> LandlordRoomsContent(
                    rooms = roomsList,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilter = roomFilter,
                    onFilterChange = { roomFilter = it },
                    onAddRoomClick = { showAddRoomSheet = true },
                    onToggleRoomStatus = { roomId ->
                        val index = roomsList.indexOfFirst { it.id == roomId }
                        if (index != -1) {
                            val r = roomsList[index]
                            val newStatus = if (r.status.contains("Vacant") || r.status.contains("Available")) "Occupied" else "Vacant (Ready)"
                            roomsList[index] = r.copy(status = newStatus)
                            showToast("Updated room $roomId status to $newStatus")
                        }
                    }
                )
                2 -> LandlordApplicationsContent(
                    applications = applicationsList,
                    selectedFilter = appFilter,
                    onFilterChange = { appFilter = it },
                    onApprove = { app ->
                        app.status = "APPROVED"
                        showToast("Approved application for ${app.name}")
                    },
                    onReject = { app ->
                        app.status = "REJECTED"
                        showToast("Rejected application for ${app.name}")
                    },
                    onAllocateClick = { app ->
                        activeAppForAllocation = app
                        showAllocateModal = true
                    }
                )
                3 -> LandlordSettingsContent(onLogout = onLogout)
            }

            // Toast overlay
            toastMessage?.let { msg ->
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    color = Color(0xFF0F172A),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF86F2E4), modifier = Modifier.size(18.dp))
                        Text(text = msg, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }

    // Add Room Bottom Sheet
    if (showAddRoomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddRoomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add Room to Sunrise Residence",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B1C30)
                )
                OutlinedTextField(
                    value = newRoomNumber,
                    onValueChange = { newRoomNumber = it },
                    label = { Text("Room Number (e.g. 106)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = newRoomRate,
                    onValueChange = { newRoomRate = it },
                    label = { Text("Monthly Rate (MK)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (newRoomNumber.isNotBlank()) {
                            roomsList.add(
                                RoomItem(
                                    id = newRoomNumber.trim(),
                                    title = "Standard Room",
                                    status = "Vacant (Ready)",
                                    price = "MK $newRoomRate",
                                    occupancy = "0 / 1 Beds",
                                    category = "vacant"
                                )
                            )
                            showAddRoomSheet = false
                            newRoomNumber = ""
                            showToast("Successfully added Room $newRoomNumber!")
                        } else {
                            showToast("Please enter a valid room number")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)), // Dark Blue shape
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Room", fontWeight = FontWeight.Bold, color = Color.White) // White text on dark shape
                }
            }
        }
    }

    // Allocate Room Dialog / Modal
    if (showAllocateModal && activeAppForAllocation != null) {
        val app = activeAppForAllocation!!
        AlertDialog(
            onDismissRequest = { showAllocateModal = false },
            title = { Text("Allocate Room for ${app.name}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Hostel: ${app.hostel}", fontWeight = FontWeight.Medium)
                    Text("Select available bedspace:")
                    
                    val availableRooms = listOf("Room 101 (Shared Double)", "Room 104 (Deluxe En-suite)")
                    availableRooms.forEach { roomOpt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedRoomForAllocation = roomOpt }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = selectedRoomForAllocation == roomOpt,
                                onClick = { selectedRoomForAllocation = roomOpt }
                            )
                            Text(text = roomOpt, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        app.status = "ALLOCATED"
                        showAllocateModal = false
                        showToast("Successfully allocated $selectedRoomForAllocation to ${app.name}!")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)) // Dark Blue shape
                ) {
                    Text("Confirm & Allocate", color = Color.White, fontWeight = FontWeight.Bold) // White text on dark shape
                }
            },
            dismissButton = {
                TextButton(onClick = { showAllocateModal = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun LandlordDashboardContent(
    onNavigateToRooms: () -> Unit,
    onNavigateToRequests: () -> Unit,
    totalRooms: Int,
    pendingAppsCount: Int
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFF131B2E)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFF89F5E7), modifier = Modifier.size(14.dp))
                        Text(text = "Verified Portfolio Owner", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Text(text = "Welcome, Mr. J. Banda", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                Text(text = "Overview of property performance, room allocations, and student requests.", fontSize = 13.sp, color = Color(0xFF45464D))

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
                            Box(modifier = Modifier.size(10.dp).background(Color(0xFF006A61), CircleShape))
                            Column {
                                Text(text = "Semester 1 Intake Active", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                                Text(text = "Near University Campus• High Demand Peak", fontSize = 11.sp, color = Color(0xFF45464D))
                            }
                        }
//                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF86F2E4)) {
//                            Text(text = "Live Sync", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00201D), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
//                        }
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    KPICard(title = "Hostels Managed", value = "3", subtitle = "Sunrise, Highland, Victoria", icon = Icons.Default.Apartment, modifier = Modifier.weight(1f))
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
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFFE5EEFF), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CorporateFare, contentDescription = null, tint = Color(0xFF0B1C30))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Hostels & Rooms", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                                Text(text = "Manage properties, $totalRooms rooms, view occupancy, and add inventory.", fontSize = 12.sp, color = Color(0xFF45464D))
                            }
                        }
                        Button(
                            onClick = onNavigateToRooms,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Manage Inventory", fontWeight = FontWeight.Bold)
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
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
                            Box(modifier = Modifier.size(40.dp).background(Color(0xFF86F2E4), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Badge, contentDescription = null, tint = Color(0xFF00201D))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "Applications & Allocations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                                    Surface(shape = CircleShape, color = Color(0xFF006A61)) {
                                        Text(text = pendingAppsCount.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Text(text = "$pendingAppsCount pending student requests awaiting vetting and room allocation.", fontSize = 12.sp, color = Color(0xFF45464D))
                            }
                        }
                        Button(
                            onClick = onNavigateToRequests,
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006A61))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Review Applications ($pendingAppsCount)", fontWeight = FontWeight.Bold)
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Payments, contentDescription = null, tint = Color(0xFF89F5E7))
                            Text(text = "Estimated Monthly Revenue", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
//                        Surface(shape = RoundedCornerShape(50.dp), color = Color.White.copy(alpha = 0.15f)) {
//                            Text(text = "Term Projection", fontSize = 10.sp, color = Color(0xFFBEC6E0), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
//                        }
                    }
                    Text(text = "MK 1,120,000 / MK 1,450,000 max", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                    Text(text = "Vacancies remain to reach full capacity. Filling these unlocks additional monthly yield.", fontSize = 12.sp, color = Color(0xFFBEC6E0))
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = Color(0xFF45464D), fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(if (isAlert) Color(0xFFFFDDB8) else Color(0xFFE5EEFF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = if (isAlert) Color(0xFFB87500) else Color(0xFF0B1C30), modifier = Modifier.size(16.dp))
                }
            }
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF0B1C30))
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF45464D))
        }
    }
}

@Composable
fun LandlordRoomsContent(
    rooms: List<RoomItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onAddRoomClick: () -> Unit,
    onToggleRoomStatus: (String) -> Unit
) {
    val filteredRooms = remember(rooms, selectedFilter, searchQuery) {
        rooms.filter { room ->
            val matchesFilter = when (selectedFilter) {
                "vacant" -> room.category == "vacant"
                "partial" -> room.category == "partial"
                "full" -> room.category == "full"
                else -> true
            }
            val matchesSearch = searchQuery.isBlank() || room.id.contains(searchQuery, true) || room.title.contains(searchQuery, true)
            matchesFilter && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hostels & Rooms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                    Text(text = "Manage properties, room setups, and live occupancy", fontSize = 13.sp, color = Color(0xFF45464D))
                }
                Button(
                    onClick = onAddRoomClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Room", fontSize = 12.sp)
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = { Text("Search room no or property...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val filters = listOf("all" to "All", "vacant" to "Vacant", "partial" to "Partial", "full" to "Full")
                    filters.forEach { (key, label) ->
                        FilterChip(
                            selected = selectedFilter == key,
                            onClick = { onFilterChange(key) },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        items(filteredRooms) { room ->
            RoomInventoryCard(
                roomNumber = room.id,
                roomTitle = room.title,
                status = room.status,
                price = room.price,
                occupancy = room.occupancy,
                onToggleStatus = { onToggleRoomStatus(room.id) }
            )
        }
    }
}

@Composable
fun RoomInventoryCard(
    roomNumber: String,
    roomTitle: String,
    status: String,
    price: String,
    occupancy: String,
    onToggleStatus: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFF86F2E4), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = roomNumber, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00201D))
                    }
                    Column {
                        Text(text = roomTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                        Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFE5EEFF)) {
                            Text(text = status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006F66), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                    Text(text = occupancy, fontSize = 11.sp, color = Color(0xFF45464D))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = onToggleStatus) {
                    Text("Toggle Occupancy Status", fontSize = 12.sp, color = Color(0xFF006A61))
                }
            }
        }
    }
}

@Composable
fun LandlordApplicationsContent(
    applications: List<ApplicationItem>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    onApprove: (ApplicationItem) -> Unit,
    onReject: (ApplicationItem) -> Unit,
    onAllocateClick: (ApplicationItem) -> Unit
) {
    val filteredApps = remember(applications, selectedFilter) {
        applications.filter { app ->
            when (selectedFilter) {
                "pending" -> app.status == "PENDING"
                "approved" -> app.status == "APPROVED" || app.status == "ALLOCATED"
                "rejected" -> app.status == "REJECTED"
                else -> true
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(text = "Applications & Allocation", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                Text(text = "Review submitted student applications and allocate available rooms.", fontSize = 13.sp, color = Color(0xFF45464D))
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val tabs = listOf("all" to "All", "pending" to "Pending", "approved" to "Approved", "rejected" to "Rejected")
                tabs.forEach { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { onFilterChange(key) },
                        label = { Text(label, fontSize = 11.sp) }
                    )
                }
            }
        }

        items(filteredApps) { app ->
            ApplicationCard(
                application = app,
                onApprove = { onApprove(app) },
                onReject = { onReject(app) },
                onAllocate = { onAllocateClick(app) }
            )
        }
    }
}

@Composable
fun ApplicationCard(
    application: ApplicationItem,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onAllocate: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(text = application.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
                    Text(text = "ID: ${application.studentId} • ${application.hostel}", fontSize = 12.sp, color = Color(0xFF45464D))
                }
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = when (application.status) {
                        "APPROVED" -> Color(0xFF86F2E4)
                        "ALLOCATED" -> Color(0xFF006A61)
                        "REJECTED" -> Color(0xFFFFDAD6)
                        else -> Color(0xFFFFDDB8)
                    }
                ) {
                    Text(
                        text = application.status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (application.status) {
                            "APPROVED" -> Color(0xFF00201D)
                            "ALLOCATED" -> Color.White
                            "REJECTED" -> Color(0xFF93000A)
                            else -> Color(0xFF2A1700)
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(text = "Pref: ${application.roomPref} (${application.price})", fontSize = 12.sp, color = Color(0xFF45464D))
            Text(text = "Phone: ${application.phone}", fontSize = 12.sp, color = Color(0xFF45464D))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (application.status == "PENDING") {
                    OutlinedButton(onClick = onReject, modifier = Modifier.weight(1f)) {
                        Text("Reject", color = Color(0xFFBA1A1A))
                    }
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006A61))
                    ) {
                        Text("Approve", color = Color.White)
                    }
                } else if (application.status == "APPROVED") {
                    Button(
                        onClick = onAllocate,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Allocate Room", color = Color.White)
                    }
                } else {
                    Text(
                        text = if (application.status == "ALLOCATED") "Room Successfully Allocated" else "Application Closed",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006A61)
                    )
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
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Landlord Account & Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B1C30))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
