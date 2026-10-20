package com.example.myapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HostelLogo(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val blueColor = Color(0xFF2563EB) // Primary Blue (60%)
    val greenColor = Color(0xFF10B981) // Accent Green (10%)

    Canvas(
        modifier = modifier.size(width = size * 1.15f, height = size)
    ) {
        val w = size.toPx() * 1.15f
        val h = size.toPx()
        val strokeWidth = w * 0.045f

        // 1. Outer Roof (Primary Blue)
        val outerRoofPath = Path().apply {
            moveTo(w * 0.12f, h * 0.52f)
            lineTo(w * 0.46f, h * 0.18f)
            lineTo(w * 0.77f, h * 0.53f)
        }
        drawPath(
            path = outerRoofPath,
            color = blueColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Chimney on Outer Roof
        val chimneyPath = Path().apply {
            moveTo(w * 0.59f, h * 0.33f)
            lineTo(w * 0.59f, h * 0.24f)
            lineTo(w * 0.64f, h * 0.24f)
            lineTo(w * 0.64f, h * 0.38f)
        }
        drawPath(
            path = chimneyPath,
            color = blueColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 2. Inner Roof (Accent Green)
        val innerRoofPath = Path().apply {
            moveTo(w * 0.22f, h * 0.46f)
            lineTo(w * 0.46f, h * 0.24f)
            lineTo(w * 0.70f, h * 0.52f)
        }
        drawPath(
            path = innerRoofPath,
            color = greenColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 3. Left Wall & Base Line (Primary Blue)
        val bodyPath = Path().apply {
            moveTo(w * 0.22f, h * 0.46f)
            lineTo(w * 0.22f, h * 0.85f)
            lineTo(w * 0.58f, h * 0.85f)
        }
        drawPath(
            path = bodyPath,
            color = blueColor,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // 4. Text "Pa_Easy Hostel" (Accent Green)
        val hostelTextResult = textMeasurer.measure(
            text = "Pa_Easy Hostel",
            style = TextStyle(
                color = greenColor,
                fontSize = (h * 0.125f).toSp(),
                fontWeight = FontWeight.Bold
            )
        )
        drawText(
            textLayoutResult = hostelTextResult,
            topLeft = Offset(
                x = w * 0.31f,
                y = h * 0.61f
            )
        )

        // 5. Text "LINK" (Primary Blue)
        val linkTextResult = textMeasurer.measure(
            text = "LINK",
            style = TextStyle(
                color = blueColor,
                fontSize = (h * 0.155f).toSp(),
                fontWeight = FontWeight.ExtraBold
            )
        )
        drawText(
            textLayoutResult = linkTextResult,
            topLeft = Offset(
                x = w * 0.59f,
                y = h * 0.77f
            )
        )
    }
}
