package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    onNavigateBack: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedChip by remember { mutableStateOf("All Hostels") }
    var searchQuery by remember { mutableStateOf("") }

    // BottomSheet & Dialog States
    var showApplySheet by remember { mutableStateOf(false) }
    var showDetailsSheet by remember { mutableStateOf(false) }
    var selectedHostelForAction by remember { mutableStateOf<HostelItem?>(null) }

    var isSubmitting by remember { mutableStateOf(false) }
    var submitSuccess by remember { mutableStateOf(false) }
    var contactPhone by remember { mutableStateOf("+265 99 123 4567") }
    val selectedPeriod by remember { mutableStateOf("Semester 1 (Sept 2024 - Jan 2025)") }
    var expandedPeriodDropdown by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var selectedHostelDetails by remember { mutableStateOf("Sunrise Residence") }
    var applicationError by remember { mutableStateOf<String?>(null) }

    // Application Form Input State
    var studentNameInput by remember { mutableStateOf("Chrispin Banda") }
    var studentIdInput by remember { mutableStateOf("BED-2024-091") }
    var contactPhoneInput by remember { mutableStateOf("+265 99 123 4567") }
    var selectedRoomTypeInput by remember { mutableStateOf("Single Room (Self-contained)") }
    var selectedPeriodInput by remember { mutableStateOf("Semester 1 (Sept 2024 - Jan 2025)") }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
    val allHostels = AppDataRepository.hostels
    val userApplications = AppDataRepository.applications

    // Filter hostels based on search query and selected chip
    val filteredHostels = remember(searchQuery, selectedChip, allHostels.size) {
        allHostels.filter { h ->
            val matchesQuery = searchQuery.isBlank() ||
                    h.name.contains(searchQuery, ignoreCase = true) ||
                    h.location.contains(searchQuery, ignoreCase = true) ||
                    h.description.contains(searchQuery, ignoreCase = true)

            val matchesChip = when (selectedChip) {
                "Close to Campus" -> h.distance.contains("250m") || h.location.contains("250m")
                "Single Rooms" -> h.priceRange.contains("65") || h.name.contains("Sunrise") || h.name.contains("Highland")
                "Budget Under MK 50k" -> h.priceRange.contains("35") || h.priceRange.contains("40") || h.priceRange.contains("45")
                else -> true
            }

            matchesQuery && matchesChip
        }
    }

    val studentFormTextFieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF9FAFB),
        focusedContainerColor = Color(0xFFF9FAFB),
        unfocusedBorderColor = Color(0xFFE5E7EB),
        focusedBorderColor = Color(0xFF2563EB),
        unfocusedTextColor = Color(0xFF111827),
        focusedTextColor = Color(0xFF111827),
        unfocusedLabelColor = Color(0xFF4B5563),
        focusedLabelColor = Color(0xFF2563EB),
        unfocusedPlaceholderColor = Color(0xFF6B7280),
        focusedPlaceholderColor = Color(0xFF6B7280)
    )

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
                                text = "Student Accommodations",
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
                                .clickable { },
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
                    icon = { Icon(Icons.Default.Roofing, contentDescription = "Hostels") },
                    label = { Text("Hostels", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
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
                    icon = { Icon(Icons.Default.MeetingRoom, contentDescription = "My Room") },
                    label = { Text("My Room", fontSize = 11.sp) },
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
                    icon = { Icon(Icons.Default.Bookmark, contentDescription = "Saved") },
                    label = { Text("Saved", fontSize = 11.sp) },
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
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
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
        when (selectedTab) {
            3 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F4F6))
                        .padding(innerPadding)
                ) {
                    StudentProfileContent(onLogout = onNavigateBack)
                }
            }
            1 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F4F6))
                        .padding(innerPadding)
                ) {
                    StudentMyRoomContent(
                        applications = userApplications,
                        onBrowseHostels = { selectedTab = 0 }
                    )
                }
            }
            2 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F4F6))
                        .padding(innerPadding)
                ) {
                    StudentSavedHostelsContent(
                        hostels = allHostels.take(2),
                        onApply = { h ->
                            selectedHostelForAction = h
                            showApplySheet = true
                        }
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F4F6))
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Welcome back, Chrispin",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Explore available hostels and reserve your room",
                                    fontSize = 13.sp,
                                    color = Color(0xFF4B5563)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFDBEAFE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = Color(0xFF2563EB),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Active Application Alert Banner
                    userApplications.firstOrNull()?.let { app ->
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(1.dp, RoundedCornerShape(12.dp)),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                                onClick = { selectedTab = 1 }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF2563EB)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.HourglassTop,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "Application Status: ${app.status}",
                                                    fontSize = 13.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF111827)
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(Color(0xFFD97706), CircleShape)
                                                )
                                            }
                                            Text(
                                                text = "${app.hostelName} • ${app.roomType}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF4B5563),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                        contentDescription = null,
                                        tint = Color(0xFF111827),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Search & Filter Controls
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text("Search hostels, landmarks, campus zones...", color = Color(0xFF6B7280)) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF4B5563))
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = studentFormTextFieldColors,
                                    singleLine = true
                                )
                            }

                            val chips = listOf(
                                Triple("All Hostels", Icons.Default.Domain, true),
                                Triple("Close to Campus", Icons.Default.NearMe, false),
                                Triple("Single Rooms", Icons.Default.Bed, false),
                                Triple("Budget Under MK 50k", Icons.Default.Payments, false)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(chips) { chip ->
                                    val isSelected = selectedChip == chip.first
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedChip = chip.first },
                                        label = { Text(chip.first, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = chip.second,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            containerColor = Color(0xFFE5E7EB),
                                            labelColor = Color(0xFF0F172A),
                                            iconColor = Color(0xFF0F172A),
                                            selectedContainerColor = Color(0xFF2563EB),
                                            selectedLabelColor = Color.White,
                                            selectedLeadingIconColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Verified Listings Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Verified Listings",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )
                                Surface(
                                    shape = RoundedCornerShape(50.dp),
                                    color = Color(0xFFD1FAE5)
                                ) {
                                    Text(
                                        text = "${filteredHostels.size} available",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Empty Search Results Feedback (PHL_SEARCH_003)
                    if (filteredHostels.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.SearchOff,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Text(
                                        text = "No hostels match your search criteria",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF111827)
                                    )
                                    Text(
                                        text = "Try clearing search query or selecting 'All Hostels'.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4B5563)
                                    )
                                }
                            }
                        }
                    }

                    // Dynamic Hostels List
                    items(filteredHostels) { hostel ->
                        val isFull = hostel.availableSpaces <= 0
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(2.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(160.dp)
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Apartment,
                                        contentDescription = null,
                                        tint = Color(0xFF2563EB),
                                        modifier = Modifier.size(64.dp)
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = Color.White.copy(alpha = 0.95f),
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(if (isFull) Color(0xFFDC2626) else Color(0xFF10B981), CircleShape)
                                            )
                                            Text(
                                                text = if (isFull) "Full (0 spaces)" else "${hostel.availableSpaces} spaces remaining",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = hostel.name,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF111827)
                                            )
                                            Text(
                                                text = hostel.location,
                                                fontSize = 12.sp,
                                                color = Color(0xFF4B5563)
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = hostel.priceRange,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF111827)
                                            )
                                            Text(
                                                text = "monthly rent",
                                                fontSize = 11.sp,
                                                color = Color(0xFF4B5563)
                                            )
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                selectedHostelForAction = hostel
                                                showDetailsSheet = true
                                            },
                                            modifier = Modifier
                                                .height(44.dp)
                                                .weight(0.4f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFDBEAFE)),
                                            border = null
                                        ) {
                                            Text(
                                                text = "Details",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                        }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { 
                                        showDetailsDialog = true
                                        selectedHostelDetails = "Sunrise Residence"
                                    },
                                    modifier = Modifier
                                        .height(44.dp)
                                        .weight(0.4f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFDBEAFE)),
                                    border = null
                                ) {
                                    Text(
                                        text = "Details",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A) // Dark Blue text on light shape
                                    )
                                }

                                Button(
                                    onClick = { showBottomSheet = true },
                                    modifier = Modifier
                                        .height(44.dp)
                                        .weight(0.6f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)) // Dark Blue shape
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FlashOn,
                                            contentDescription = null,
                                            tint = Color.White, // White icon on dark shape
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "Apply for Sunrise",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White // White text on dark shape
                                        )
                                        Button(
                                            onClick = {
                                                if (!isFull) {
                                                    selectedHostelForAction = hostel
                                                    showApplySheet = true
                                                }
                                            },
                                            enabled = !isFull,
                                            modifier = Modifier
                                                .height(44.dp)
                                                .weight(0.6f),
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (isFull) Color(0xFF9CA3AF) else Color(0xFF2563EB),
                                                disabledContainerColor = Color(0xFFE5E7EB)
                                            )
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isFull) Icons.Default.Block else Icons.Default.FlashOn,
                                                    contentDescription = null,
                                                    tint = if (isFull) Color(0xFF6B7280) else Color.White,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Text(
                                                    text = if (isFull) "Fully Booked" else "Apply Now",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isFull) Color(0xFF6B7280) else Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }

    // --- HOSTEL DETAILS SHEET ---
    selectedHostelForAction?.let { hostel ->
        if (showDetailsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showDetailsSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = hostel.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    Text(text = hostel.location, fontSize = 13.sp, color = Color(0xFF4B5563))

                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFDBEAFE)) {
                        Text(
                            text = "Rate: ${hostel.priceRange} • Distance: ${hostel.distance}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Text(text = "About Property", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    Text(text = hostel.description, fontSize = 13.sp, color = Color(0xFF4B5563))

                    Text(text = "Key Features & Amenities", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    hostel.features.forEach { ft ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Text(text = ft, fontSize = 13.sp, color = Color(0xFF111827))
                        }
                    }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { 
                                        showDetailsDialog = true
                                        selectedHostelDetails = "Sunrise Residence"
                                    },
                                    modifier = Modifier
                                        .height(44.dp)
                                        .weight(0.4f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFDBEAFE)),
                                    border = null
                                ) {
                                    Text(
                                        text = "Details",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A) // Dark Blue text on light shape
                                    )
                                }
                    Text(text = "Landlord Contact: ${hostel.contactPhone}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))

                    Button(
                        onClick = {
                            showDetailsSheet = false
                            showApplySheet = true
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Text("Proceed to Apply", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // --- APPLICATION SHEET ---
    selectedHostelForAction?.let { hostel ->
        if (showApplySheet) {
            ModalBottomSheet(
                onDismissRequest = { showApplySheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Apply for ${hostel.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

                    applicationError?.let { err ->
                        Text(text = err, fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                    }

                    OutlinedTextField(
                        value = studentNameInput,
                        onValueChange = { studentNameInput = it },
                        label = { Text("Student Full Name") },
                        colors = studentFormTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = studentIdInput,
                        onValueChange = { studentIdInput = it },
                        label = { Text("Student ID Number") },
                        colors = studentFormTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = contactPhoneInput,
                        onValueChange = { contactPhoneInput = it },
                        label = { Text("Contact Phone Number") },
                        colors = studentFormTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = selectedRoomTypeInput,
                        onValueChange = { selectedRoomTypeInput = it },
                        label = { Text("Room Type Preference") },
                        colors = studentFormTextFieldColors,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Button(
                        onClick = {
                            if (studentNameInput.isBlank() || contactPhoneInput.isBlank()) {
                                applicationError = "Please fill in all mandatory fields"
                            } else {
                                isSubmitting = true
                                applicationError = null
                                coroutineScope.launch {
                                    delay(800)
                                    AppDataRepository.submitApplication(
                                        studentName = studentNameInput,
                                        studentId = studentIdInput,
                                        hostelName = hostel.name,
                                        roomType = selectedRoomTypeInput,
                                        phone = contactPhoneInput,
                                        period = selectedPeriodInput
                                    )
                                    isSubmitting = false
                                    submitSuccess = true
                                    delay(1000)
                                    submitSuccess = false
                                    showApplySheet = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        enabled = !isSubmitting
                    ) {
                        Text(
                            text = if (isSubmitting) "Submitting Application..." else "Submit Application",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentMyRoomContent(
    applications: List<StudentApplicationItem>,
    onBrowseHostels: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "My Room & Allocation Status", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

        if (applications.isEmpty()) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No Active Room Allocations", fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                    Text("Explore available hostels and apply for accommodation.", fontSize = 12.sp, color = Color(0xFF4B5563))
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onBrowseHostels, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))) {
                        Text("Browse Hostels", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            applications.forEach { app ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = app.hostelName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = when(app.status) {
                                    "ALLOCATED" -> Color(0xFFDBEAFE)
                                    "APPROVED" -> Color(0xFFD1FAE5)
                                    "REJECTED" -> Color(0xFFFEE2E2)
                                    else -> Color(0xFFFEF3C7)
                                }
                            ) {
                                Text(
                                    text = app.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when(app.status) {
                                        "ALLOCATED" -> Color(0xFF2563EB)
                                        "APPROVED" -> Color(0xFF059669)
                                        "REJECTED" -> Color(0xFFDC2626)
                                        else -> Color(0xFFD97706)
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF3F4F6))

                        Text(text = "Preference: ${app.roomType}", fontSize = 13.sp, color = Color(0xFF4B5563))
                        Text(text = "Academic Period: ${app.period}", fontSize = 13.sp, color = Color(0xFF4B5563))

                        if (app.allocatedRoom.isNotBlank()) {
                            Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFD1FAE5)) {
                                Text(
                                    text = "Allocated Unit: ${app.allocatedRoom}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StudentSavedHostelsContent(
    hostels: List<HostelItem>,
    onApply: (HostelItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Saved Hostels & Favorites", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

        hostels.forEach { h ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = h.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text(text = "${h.location} • ${h.priceRange}", fontSize = 12.sp, color = Color(0xFF4B5563))
                    }
                    Button(
                        onClick = { onApply(h) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Hostel Details Dialog UI
    if (showDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            title = { Text("$selectedHostelDetails - Details & Amenities") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Location: Near University Main Gate (5 mins walk)")
                    Text("Room Types Available: Shared Double, Deluxe Single, Self-contained")
                    Text("Amenities: High-speed Wi-Fi, 24/7 Security Guard, Backup Generator, Continuous Water Supply")
                    Text("Status: Active Admission Open for Semester 1")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDetailsDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
  }
}

@Composable
fun StudentProfileContent(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Profile Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2563EB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Chrispin Banda",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Text(
                    text = "BED-2024-091 • BSc Computer Science",
                    fontSize = 13.sp,
                    color = Color(0xFF4B5563)
                )

                Text(
                    text = "alex@campus.edu",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFD1FAE5)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.VerifiedUser,
                            contentDescription = null,
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "CAMPUS VERIFIED STUDENT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        )
                    }
                }
            }
        }

        // Account Details Options
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileOptionItem(
                    icon = Icons.Outlined.Assignment,
                    title = "My Hostel Applications",
                    subtitle = "1 Active Request (Highland Villa)"
                )
                HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileOptionItem(
                    icon = Icons.Outlined.BookmarkBorder,
                    title = "Saved Hostels & Favorites",
                    subtitle = "2 Saved Properties"
                )
                HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileOptionItem(
                    icon = Icons.Outlined.Lock,
                    title = "Security & Privacy",
                    subtitle = "Change password & authentication"
                )
                HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(horizontal = 16.dp))
                ProfileOptionItem(
                    icon = Icons.Outlined.HelpOutline,
                    title = "Support & Campus Concierge",
                    subtitle = "Get help with hostel allocations"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Log Out Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "LOG OUT",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProfileOptionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFDBEAFE)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF2563EB),
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF111827)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF6B7280)
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color(0xFF9CA3AF),
            modifier = Modifier.size(16.dp)
        )
    }
}
