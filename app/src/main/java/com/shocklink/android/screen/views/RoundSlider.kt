import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shocklink.android.ui.theme.ShockLinkAndroidTheme
import java.text.DecimalFormat
import kotlin.math.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTextApi::class)
@Composable
fun CircularSlider(
    modifier: Modifier = Modifier,
    padding: Float = 20f,
    stroke: Float = 100f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 150f,
    thumbColor: Color = Color.White,
    progressColor: Color = Color.Magenta,
    backgroundColor: Color = Color.DarkGray,
    textColor: Color = Color.DarkGray,
    maxValue: Float = 100f,
    minValue: Float = 0f,
    numbersAfterComma: Int = 0,
    debug: Boolean = false,
    onChange: ((Float)->Unit)? = null
){
    var width by remember { mutableStateOf(0) }
    var height by remember { mutableStateOf(0) }
    var angle by remember { mutableStateOf(-60f) }
    var last by remember { mutableStateOf(0f) }
    var down  by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }
    var appliedAngle by remember { mutableStateOf(0f) }

    val textMeasurer = rememberTextMeasurer()

    val textLayoutResult: TextLayoutResult =
        textMeasurer.measure(text = AnnotatedString(getStringFromValue(getValue(appliedAngle, maxValue, minValue), numbersAfterComma)))
    val textSizeX = textLayoutResult.size.times(3)
    LaunchedEffect(key1 = angle){
        var a = angle
        a += 60
        if(a<=0f){
            a += 360
        }
        a = a.coerceIn(0f,300f)
        if(last<150f&&a==300f){
            a = 0f
        }
        last = a
        appliedAngle = a
    }
    LaunchedEffect(key1 = appliedAngle){
        onChange?.invoke(getValue(appliedAngle, maxValue, minValue))
    }
    Canvas(
        modifier = modifier
            .onGloballyPositioned {
                width = it.size.width
                height = it.size.height
                center = Offset(width / 2f, height / 2f)
                radius = min(width.toFloat(), height.toFloat()) / 2f - padding - stroke / 2f
            }
            .pointerInput(Unit) {
                forEachGesture {

                    awaitPointerEventScope {

                        val eventDown = awaitFirstDown()
                        var offset = eventDown.position
                        // ACTION_DOWN here
                        val d = distance(offset, center)
                        val a = angle(center, offset)
                        if (d >= radius - touchStroke / 2f && d <= radius + touchStroke / 2f && a !in -120f..-60f) {
                            down = true
                            angle = a
                        } else {
                            down = false
                        }
                        do {
                            val event: PointerEvent = awaitPointerEvent()
                            // ACTION_MOVE loop
                            event.changes.forEach { pointerInputChange: PointerInputChange ->
                                offset = pointerInputChange.position
                                angle = angle(center, offset)
                                pointerInputChange.consumePositionChange()
                            }
                        } while (event.changes.any { it.pressed })

                        // ACTION_UP is here
                    }
                }
            }
    ){
        drawArc(
            color = backgroundColor,
            startAngle = -240f,
            sweepAngle = 300f,
            topLeft = center - Offset(radius,radius),
            size = Size(radius*2,radius*2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
        drawArc(
            color = progressColor,
            startAngle = 120f,
            sweepAngle = appliedAngle,
            topLeft = center - Offset(radius,radius),
            size = Size(radius*2,radius*2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
        drawCircle(
            color = thumbColor,
            radius = stroke / 2,
            center = center + Offset(
                radius*cos((120+appliedAngle)*PI/180f).toFloat(),
                radius*sin((120+appliedAngle)*PI/180f).toFloat()
            )
        )
        drawText(
            textMeasurer = textMeasurer,
            text = getStringFromValue(getValue(appliedAngle, maxValue, minValue), numbersAfterComma),
            topLeft = Offset(
                (width - textSizeX.width) / 2f,
                (height - textSizeX.height) / 2f
            ),
            style = TextStyle(
                fontSize = 40.sp,
                color = textColor),
        )
        if(debug){
            drawRect(
                color = Color.Green,
                topLeft = Offset.Zero,
                size = Size(width.toFloat(),height.toFloat()),
                style = Stroke(
                    4f
                )
            )
            drawRect(
                color = Color.Red,
                topLeft = Offset(padding,padding),
                size = Size(width.toFloat()-padding*2,height.toFloat()-padding*2),
                style = Stroke(
                    4f
                )
            )
            drawRect(
                color = Color.Blue,
                topLeft = Offset(padding,padding),
                size = Size(width.toFloat()-padding*2,height.toFloat()-padding*2),
                style = Stroke(
                    4f
                )
            )
            drawCircle(
                color = Color.Red,
                center = center,
                radius = radius+stroke/2f,
                style = Stroke(2f)
            )
            drawCircle(
                color = Color.Red,
                center = center,
                radius = radius-stroke/2f,
                style = Stroke(2f)
            )
        }
    }
}

fun angle(center: Offset, offset: Offset): Float {
    val rad = atan2(center.y - offset.y, center.x - offset.x)
    val deg = Math.toDegrees(rad.toDouble())
    return deg.toFloat()
}
fun distance(first: Offset, second: Offset) : Float{
    return sqrt((first.x-second.x).square()+(first.y-second.y).square())
}
fun Float.square(): Float{
    return this*this
}

fun getValue(appliedAngle: Float, maxValue: Float, minValue: Float):Float {
    val value = (appliedAngle/300f) * maxValue
    if(value < minValue)
        return minValue
    return value
}

fun getStringFromValue(value: Float, numbersAfterComma: Int):String {
    return if(numbersAfterComma == 0){
        value.roundToInt().toString()
    }
    else{
        val formatter = DecimalFormat("0.0")
        formatter.format(value)
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    ShockLinkAndroidTheme {
        CircularSlider(
            modifier = Modifier.size(200.dp),
        )
    }
}