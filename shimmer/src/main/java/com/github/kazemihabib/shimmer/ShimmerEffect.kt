package com.github.kazemihabib.shimmer

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas



interface ShimmerEffect {

    fun draw(canvas: Canvas, size: Size)

    fun updateSize(size: Size)

}

