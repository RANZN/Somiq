package com.ranjan.somiq.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.unit.dp

val SomiQSplashIcon: ImageVector
    get() {
        if (_somiqIcon != null) return _somiqIcon!!
        _somiqIcon = ImageVector.Builder(
            name = "SomiQSplash",
            defaultWidth = 1080.dp,
            defaultHeight = 1920.dp,
            viewportWidth = 1080f,
            viewportHeight = 1920f
        ).apply {
            // 1. Black Background
            path(fill = SolidColor(Color.Black)) {
                moveTo(0f, 0f)
                horizontalLineTo(1080f)
                verticalLineTo(1920f)
                horizontalLineTo(0f)
                close()
            }

            // 2. Main Group Centered
            group(translationX = 540f, translationY = 960f) {

                // --- ICON GROUP (Shifted UP) ---
                group(translationY = -220f, scaleX = 1.8f, scaleY = 1.8f) {
                    group(translationX = -100f, translationY = -100f) {
                        // Camera Body (Yellow)
                        path(fill = SolidColor(Color(0xFFFFE600))) {
                            moveTo(40f, 0f)
                            horizontalLineTo(160f)
                            curveTo(182.09f, 0f, 200f, 17.9f, 200f, 40f)
                            verticalLineTo(140f)
                            curveTo(200f, 162.1f, 182.09f, 180f, 160f, 180f)
                            horizontalLineTo(130f)
                            lineTo(160f, 210f)
                            lineTo(110f, 180f)
                            horizontalLineTo(40f)
                            curveTo(17.9f, 180f, 0f, 162.1f, 0f, 140f)
                            verticalLineTo(40f)
                            curveTo(0f, 17.9f, 17.9f, 0f, 40f, 0f)
                            close()
                        }
                        // Lens Ring (White Stroke)
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 14f
                        ) {
                            moveTo(145f, 90f)
                            curveTo(145f, 114.85f, 124.85f, 135f, 100f, 135f)
                            curveTo(75.15f, 135f, 55f, 114.85f, 55f, 90f)
                            curveTo(55f, 65.15f, 75.15f, 45f, 100f, 45f)
                            curveTo(124.85f, 45f, 145f, 65.15f, 145f, 90f)
                            close()
                        }
                        // Flash Dot (White Fill)
                        path(fill = SolidColor(Color.White)) {
                            moveTo(177f, 35f)
                            curveTo(177f, 41.6f, 171.6f, 47f, 165f, 47f)
                            curveTo(158.4f, 47f, 153f, 41.6f, 153f, 35f)
                            curveTo(153f, 28.4f, 158.4f, 23f, 165f, 23f)
                            curveTo(171.6f, 23f, 177f, 28.4f, 177f, 35f)
                            close()
                        }
                    }
                }

                // --- TEXT GROUP (Shifted DOWN) ---
                group(translationY = 160f) {
                    group(translationX = -145f) {
                        // Letter S
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f,
                            strokeLineCap = StrokeCap.Round
                        ) {
                            moveTo(40f, 10f)
                            curveTo(40f, 0f, 20f, 0f, 15f, 5f)
                            curveTo(5f, 10f, 5f, 25f, 20f, 30f)
                            curveTo(35f, 35f, 45f, 40f, 45f, 60f)
                            curveTo(45f, 80f, 20f, 85f, 10f, 80f)
                        }
                        // Letter o
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f
                        ) {
                            moveTo(104f, 58f)
                            curveTo(104f, 70.15f, 94.15f, 80f, 82f, 80f)
                            curveTo(69.85f, 80f, 60f, 70.15f, 60f, 58f)
                            curveTo(60f, 45.85f, 69.85f, 36f, 82f, 36f)
                            curveTo(94.15f, 36f, 104f, 45.85f, 104f, 58f)
                            close()
                        }
                        // Letter m
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f,
                            strokeLineCap = StrokeCap.Round
                        ) {
                            moveTo(119f, 35f)
                            verticalLineTo(80f)
                            moveTo(119f, 50f)
                            curveTo(119f, 35f, 144f, 35f, 144f, 50f)
                            verticalLineTo(80f)
                            moveTo(144f, 50f)
                            curveTo(144f, 35f, 169f, 35f, 169f, 50f)
                            verticalLineTo(80f)
                        }
                        // Letter i (Line + Dot)
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f,
                            strokeLineCap = StrokeCap.Round
                        ) {
                            moveTo(198f, 35f)
                            verticalLineTo(80f)
                        }
                        path(fill = SolidColor(Color.White)) {
                            moveTo(206f, 15f)
                            curveTo(206f, 19.4f, 202.4f, 23f, 198f, 23f)
                            curveTo(193.6f, 23f, 190f, 19.4f, 190f, 15f)
                            curveTo(190f, 10.6f, 193.6f, 7f, 198f, 7f)
                            curveTo(202.4f, 7f, 206f, 10.6f, 206f, 15f)
                            close()
                        }
                        // Letter Q
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f
                        ) {
                            moveTo(283f, 45f)
                            curveTo(283f, 64.3f, 267.3f, 80f, 248f, 80f)
                            curveTo(228.7f, 80f, 213f, 64.3f, 213f, 45f)
                            curveTo(213f, 25.7f, 228.7f, 10f, 248f, 10f)
                            curveTo(267.3f, 10f, 283f, 25.7f, 283f, 45f)
                            close()
                        }
                        path(
                            stroke = SolidColor(Color.White),
                            strokeLineWidth = 12f,
                            strokeLineCap = StrokeCap.Round
                        ) {
                            moveTo(265f, 65f)
                            lineTo(285f, 85f)
                        }
                    }
                }
            }
        }.build()
        return _somiqIcon!!
    }

private var _somiqIcon: ImageVector? = null