 package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf("Student") } // "Student" or "Landlord"
    var authMode by remember { mutableStateOf("Sign In") } // "Sign In" or "Register"

    // Sign in state
    var email by remember { mutableStateOf("alex@campus.edu") }
    var password by remember { mutableStateOf("••••••••••") }
    var passwordVisible by remember { mutableStateOf(false) }
    var keepMeSignedIn by remember { mutableStateOf(true) }

    // Register state
    var regFullName by remember { mutableStateOf("") }
    var regIdOrProperty by remember { mutableStateOf("") }
    var regProgramOrRooms by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color(0xFFF3F4F6)), // 30% Light Grey
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.94f)
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Logo
                HostelLogo(size = 110.dp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (authMode == "Sign In") "Student Accommodation Simplified" else "New Account Registration",
                    fontSize = 12.sp,
                    color = Color(0xFF4B5563),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selector Tabs (Student / Landlord)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RoleTabButton(
                        text = "Student",
                        icon = Icons.Outlined.School,
                        selected = selectedRole == "Student",
                        modifier = Modifier.weight(1f)
                    ) { selectedRole = "Student" }

                    RoleTabButton(
                        text = "Landlord",
                        icon = Icons.Outlined.VpnKey,
                        selected = selectedRole == "Landlord",
                        modifier = Modifier.weight(1f)
                    ) { selectedRole = "Landlord" }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Selector Tabs (Sign In / Register)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ActionTabButton(
                        text = "Sign In",
                        selected = authMode == "Sign In",
                        modifier = Modifier.weight(1f)
                    ) { authMode = "Sign In" }

                    ActionTabButton(
                        text = "Register",
                        selected = authMode == "Register",
                        modifier = Modifier.weight(1f)
                    ) { authMode = "Register" }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (authMode == "Sign In") {
                    // --- SIGN IN FORM ---
                    // Email Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Email Address",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Password Field
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Password",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827)
                            )
                            Text(
                                text = "Forgot Password?",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.clickable { }
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF9CA3AF),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                                        contentDescription = null,
                                        tint = Color(0xFF9CA3AF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Keep me signed in & Fast Access badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { keepMeSignedIn = !keepMeSignedIn }
                        ) {
                            Checkbox(
                                checked = keepMeSignedIn,
                                onCheckedChange = { keepMeSignedIn = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2563EB))
                            )
                            Text(
                                text = "Keep me signed in",
                                fontSize = 12.sp,
                                color = Color(0xFF4B5563)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFD1FAE5), RoundedCornerShape(16.dp)) // 10% Accent Light
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Fast Access",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669) // 10% Accent Green Dark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Button (60% Primary Blue)
                    Button(
                        onClick = { onLoginSuccess(selectedRole) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "LOGIN",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Or authenticate with divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                        Text(
                            text = " or authenticate with ",
                            fontSize = 11.sp,
                            color = Color(0xFF9CA3AF)
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // University SSO Portal Button
                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF9FAFB)),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Shield,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "University SSO Portal",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF111827)
                            )
                        }
                    }

                } else {
                    // --- REGISTER FORM ---
                    // Full Name
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (selectedRole == "Student") "Full Name (Campus Verified)" else "Portfolio Manager / Landlord Name",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = regFullName,
                            onValueChange = { regFullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(if (selectedRole == "Student") "e.g. Chrispin Banda" else "e.g. Mr. J. Banda") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // ID or Property Name
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (selectedRole == "Student") "Student ID Number" else "Hostel / Property Name",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = regIdOrProperty,
                            onValueChange = { regIdOrProperty = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(if (selectedRole == "Student") "e.g. BED-2024-091" else "e.g. Sunrise Residence") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Program or Room count
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = if (selectedRole == "Student") "Degree Program & Level" else "Number of Managed Rooms",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = regProgramOrRooms,
                            onValueChange = { regProgramOrRooms = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text(if (selectedRole == "Student") "e.g. BSc Computer Science • Level 3" else "e.g. 10 Rooms / 25 Beds") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Email Address
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Campus Email Address",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = { regEmail = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("e.g. name@campus.edu") },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Password
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Create Password",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = regPassword,
                            onValueChange = { regPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF9FAFB),
                                focusedContainerColor = Color(0xFFF9FAFB),
                                unfocusedBorderColor = Color(0xFFE5E7EB),
                                focusedBorderColor = Color(0xFF2563EB)
                            ),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Register Button (10% Accent Green)
                    Button(
                        onClick = { onLoginSuccess(selectedRole) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = if (selectedRole == "Student") "REGISTER STUDENT ACCOUNT" else "REGISTER LANDLORD PORTFOLIO",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Don't have an account? Sign Up
                if (authMode == "Sign In") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563)
                        )
                        Text(
                            text = "Sign Up",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            modifier = Modifier.clickable { authMode = "Register" }
                        )
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 12.sp,
                            color = Color(0xFF4B5563)
                        )
                        Text(
                            text = "Sign In",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2563EB),
                            modifier = Modifier.clickable { authMode = "Sign In" }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Bottom Security Badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color(0xFF4B5563),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "ENCRYPTED & SECURE • CAMPUS VERIFIED",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B5563),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun RoleTabButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) Color(0xFF2563EB) else Color(0xFF4B5563)
    val iconTint = if (selected) Color(0xFF2563EB) else Color(0xFF9CA3AF)
    val shadow = if (selected) 2.dp else 0.dp

    Surface(
        modifier = modifier
            .height(36.dp)
            .shadow(shadow, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}

@Composable
fun ActionTabButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) Color(0xFF2563EB) else Color(0xFF4B5563)
    val shadow = if (selected) 2.dp else 0.dp

    Surface(
        modifier = modifier
            .height(34.dp)
            .shadow(shadow, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        onClick = onClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        }
    }
}
