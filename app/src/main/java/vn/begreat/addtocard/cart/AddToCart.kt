package vn.begreat.addtocard.cart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun rememberAddToCartState(): AddToCartState = remember {
    AddToCartState()
}

private val LocalAddToCartState = staticCompositionLocalOf<AddToCartState> {
    error("no AddToCartState provided")
}

/**
 * Add to card container
 */
@Composable
fun AddToCartContainer(
    addToCardState: AddToCartState = rememberAddToCartState(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalAddToCartState provides addToCardState,
    ) {
        Box {
            content()
            //
            val itemKey by addToCardState.selectedKey
            val info = addToCardState.getInfo(itemKey)

            if (info != null) {
                AnimateToTarget(
                    info = info,
                    target = addToCardState.targetPosition
                )
            }
        }
    }
}

@Composable
private fun AnimateToTarget(
    info: CartItemModelInfo,
    target: Offset
) {
    val state = LocalAddToCartState.current

    val animatable = remember { Animatable(0f) }

    val scale = (1.5f - animatable.value).coerceAtLeast(0.5f)

    Box(
        modifier = Modifier.graphicsLayer {
            translationX = info.run {
                position.x + (target.x - position.x) * animatable.value
            }

            translationY = info.run {
                position.y + (target.y - position.y) * animatable.value - size.height / 2
            }

            scaleX = scale
            scaleY = scale
        }
    ) {
        info.composable()
    }

    // Apply the animation
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = info) {
        scope.launch {
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300)
            )
            state.stop()
        }
    }
}


@Composable
fun CartTarget(
    content: @Composable () -> Unit
) {
    val state = LocalAddToCartState.current
    val count by state.totalCount

    Box(
        modifier = Modifier.onGloballyPositioned {
            state.targetPosition = it.localToWindow(Offset.Zero)
        },
        contentAlignment = Alignment.TopEnd,
    ) {
        content()

        if (count > 0) {
            val countText = if (count > 5) "5+" else "$count"

            Box(
                modifier = Modifier
                    .background(color = Color.Red, shape = CircleShape)
                    .size(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = countText,
                    fontSize = 10.sp, color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CartItemTarget(
    key: String,
    content: @Composable () -> Unit
) {
    val state = LocalAddToCartState.current

    val itemInfo = remember(key1 = key) {
        mutableStateOf(CartItemModelInfo(composable = content))
    }

    Box(modifier = Modifier.onGloballyPositioned {
        itemInfo.value = itemInfo.value.copy(
            position = it.localToWindow(Offset.Zero),
            size = it.size,
        )
    }) {
        content()
    }

    // Effect: update the item info to the state
    DisposableEffect(key1 = key, key2 = itemInfo.value, effect = {
        state.itemInfo(key, itemInfo.value)

        onDispose {
            state.itemInfo(key, null)
        }
    })
}
