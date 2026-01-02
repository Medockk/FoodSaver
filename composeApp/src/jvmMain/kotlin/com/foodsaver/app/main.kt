package com.foodsaver.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.foodsaver.app.di.initSharedKoin
import com.foodsaver.app.di.uiModule
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

fun main() {
    initSharedKoin(arrayOf(uiModule))
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "FoodSaver",
            alwaysOnTop = false,

        ) {
            App()
            //MorphingLayout()
            //StackedAnimatedLayout((1..10).map { it.toString() })
            //PhysicsLayout()
            //RadialAnimatedLayout(items = (1..10).map { it.toString() })
        }
    }
}

@Composable
fun MorphingLayout() {

    var expanded by remember { mutableStateOf(false) }

    // t — степень morphing: 0 = круг, 1 = колонка
    val t by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing)
    )

    val items = remember { (1..6).toList() }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .clickable { expanded = !expanded },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Tap to morph",
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Layout(
            content = {
                items.forEach {
                    Box(
                        Modifier
                            .padding(10.dp)
                            .size(50.dp)
                            .background(Color(0xFF64B5F6), CircleShape)
                    )
                }
            }
        ) { measurables, constraints ->

            val placeables = measurables.map { it.measure(constraints) }

            val width = constraints.maxWidth
            val height = constraints.maxHeight

            // --- Цель 1: координаты в круге ---
            val radius = min(width, height) / 3f
            val centerX = width / 2f
            val centerY = height / 2f

            // --- Цель 2: координаты в колонке ---
            val columnX = width / 2f
            val startY = height / 2f - placeables.size * 60 / 2f

            layout(width, height) {

                placeables.forEachIndexed { index, placeable ->

                    // --- позиция в круге ---
                    val angle = index / placeables.size.toFloat() * 2f * PI
                    val circleX = centerX + cos(angle).toFloat() * radius
                    val circleY = centerY + sin(angle).toFloat() * radius

                    // --- позиция в колонке ---
                    val lineX = columnX
                    val lineY = startY + index * 60f

                    // --- morph: интерполяция ---
                    val finalX = lerp(circleX, lineX, t) - placeable.width / 2f
                    val finalY = lerp(circleY, lineY, t) - placeable.height / 2f

                    placeable.place(finalX.roundToInt(), finalY.roundToInt())
                }
            }
        }
    }
}

// Линейная интерполяция
private fun lerp(a: Float, b: Float, t: Float): Float =
    a + (b - a) * t

@Composable
fun StackedAnimatedLayout(
    items: List<String>
) {
    var topIndex by remember { mutableStateOf(0) }

    Layout(
        modifier = Modifier.fillMaxSize(),
        content = {
            items.forEachIndexed { index, str ->

                val isTop = index == topIndex

                // Z-index animation
                val animatedZ by animateFloatAsState(
                    targetValue = if (isTop) 10f else 0f,
                    animationSpec = tween(600)
                )

                // Offset animation
                val offsetX by animateFloatAsState(
                    targetValue = if (isTop) 0f else -40f * index,
                    animationSpec = tween(500)
                )

                Box(
                    Modifier
                        .graphicsLayer {
                            translationX = offsetX
                            this.shadowElevation = animatedZ * 20
                        }
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                        .clickable { topIndex = index }
                ) {
                    Text("Card: $str")
                }
            }
        }
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            val centerX = constraints.maxWidth / 2

            placeables.forEachIndexed { index, placeable ->
                placeable.place(
                    centerX - placeable.width / 2,
                    index * 40 // вертикальное смещение
                )
            }
        }
    }
}

@Composable
fun PhysicsLayout(
    modifier: Modifier = Modifier,
    count: Int = 10
) {
    // Храним состояние каждого объекта
    data class Body(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        val size: Float = 40f
    )

    val bodies = remember {
        List(count) {
            Body(
                x = (0..300).random().toFloat(),
                y = (0..300).random().toFloat(),
                vx = (-2..2).random().toFloat(),
                vy = (-2..2).random().toFloat()
            )
        }
    }

    // Запускаем бесконечный physics-loop
    LaunchedEffect(true) {
        while (true) {
            withFrameNanos { dt ->
                val delta = dt / 1_000_000_000f

                bodies.forEach { b ->
                    // Гравитация
                    b.vy += 300f * delta

                    // Обновление позиции
                    b.x += b.vx
                    b.y += b.vy

                    // Столкновения с границами
                    if (b.x < 0 || b.x > 900) b.vx *= -0.8f
                    if (b.y < 0 || b.y > 1600) b.vy *= -0.8f
                }

                // Столкновения между объектами
                for (i in bodies.indices) {
                    for (j in i + 1 until bodies.size) {
                        val a = bodies[i]
                        val b = bodies[j]
                        val dx = b.x - a.x
                        val dy = b.y - a.y
                        val dist = sqrt(dx * dx + dy * dy)

                        if (dist < (a.size + b.size)) {
                            // Эластичное столкновение
                            a.vx = -a.vx
                            a.vy = -a.vy
                            b.vx = -b.vx
                            b.vy = -b.vy
                        }
                    }
                }
            }
        }
    }

    // Layout отрисовывает текущее состояние тел
    Layout(
        content = {
            bodies.forEach {
                Box(
                    Modifier
                        .size(it.size.dp)
                        .background(Color.Red, CircleShape)
                )
            }
        }
    ) { measurables, constraints ->

        val placeables = measurables.map { it.measure(constraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, p ->
                val b = bodies[index]
                p.place(b.x.toInt(), b.y.toInt())
            }
        }
    }
}

@Composable
fun RadialAnimatedLayout(
    modifier: Modifier = Modifier,
    items: List<String>,
) {
    // Какой элемент выбран — тот анимируется
    var selectedIndex by remember { mutableStateOf(-1) }

    // Анимация "радиуса", чтобы элементы разлетались плавно
    val radius by animateFloatAsState(
        targetValue = if (selectedIndex == -1) 80f else 150f,
        animationSpec = tween(700)
    )

    // Сам Layout
    Layout(
        modifier = modifier,
        content = {
            items.forEachIndexed { index, label ->

                // Индивидуальная анимация размера каждого ребёнка
                val scale by animateFloatAsState(
                    targetValue = if (index == selectedIndex) 1.6f else 1f,
                    animationSpec = tween(500)
                )

                Box(
                    Modifier
                        .size((40 * scale).dp) // изменяемый размер
                        .background(
                            if (index == selectedIndex) Color.Red else Color.Blue,
                            CircleShape
                        )
                        .clickable { selectedIndex = index } // кликаем — выбираем
                ) {
                    Text(
                        label,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { measurables, constraints ->

        // Размер всего Layout — разрешаем любое
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        // Центр окружности
        val centerX = layoutWidth / 2
        val centerY = layoutHeight / 2

        // Измеряем детей
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        // Размещаем
        layout(layoutWidth, layoutHeight) {

            val count = placeables.size
            if (count == 0) return@layout

            placeables.forEachIndexed { index, placeable ->

                // Угол для позиции на круге
                val angle = 360f / count * index

                // Перевод в радианы
                val rad = Math.toRadians(angle.toDouble())

                // Координаты на окружности (radius — анимируется!)
                val x = centerX + radius * cos(rad) - placeable.width / 2
                val y = centerY + radius * sin(rad) - placeable.height / 2

                placeable.place(
                    x.toInt(),
                    y.toInt()
                )
            }
        }
    }
}

