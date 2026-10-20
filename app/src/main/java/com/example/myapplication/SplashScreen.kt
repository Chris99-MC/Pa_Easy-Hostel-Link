package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }

    // Smoothly animate progress over 2.2 seconds then finish splash
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        val duration = 2200L
        while (true) {
            val elapsed = System.currentTimeMillis() - startTime
            progress = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
            if (progress >= 1f) break
            delay(16)
        }
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF3F4F6), // 30% Light Grey
                        Color(0xFFE5E7EB),
                        Color(0xFFFFFFFF)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Logo
            HostelLogo(size = 96.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // App Title (60% Blue + 10% Green)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pa_Easy Hostel ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                )
                Text(
                    text = "Link",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Student Living, Simplified.",
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Animated Progress Bar (Track: Light Grey, Active Fill: Accent Green)
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .background(Color(0xFFE5E7EB), shape = MaterialTheme.shapes.small)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(Color(0xFF10B981), shape = MaterialTheme.shapes.small)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "CONCIERGE ENVIRONMENT READY.",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2563EB),
                letterSpacing = 1.5.sp
            )
        }

        // Version at bottom
        Text(
            text = "v1.0.4",
            fontSize = 12.sp,
            color = Color(0xFF9CA3AF),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
