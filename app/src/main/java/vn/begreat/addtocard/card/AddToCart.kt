package vn.begreat.addtocard.card

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
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
            val itemKey by addToCardState.selectedItem
            val itemModelInfo = addToCardState.getInfo(itemKey)

            if (itemModelInfo != null) {
                AnimateToTarget(
                    itemModelInfo = itemModelInfo,
                    targetPosition = addToCardState.targetPosition ?: Offset.Zero
                )
            }
        }
    }
}

@Composable
private fun AnimateToTarget(
    itemModelInfo: CartItemModelInfo,
    targetPosition: Offset
) {

    val state = LocalAddToCartState.current

    val animatable = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    val scale = (1f - animatable.value).coerceAtLeast(0.5f)

    LaunchedEffect(key1 = itemModelInfo) {
        scope.launch {
            animatable.animateTo(
                1f,
                animationSpec = tween(durationMillis = 500)
            )
            state.stop()
        }
    }

    Box(
        modifier = Modifier.graphicsLayer {
            translationX =
                itemModelInfo.position.x + (targetPosition.x - itemModelInfo.position.x) * animatable.value
            translationY =
                itemModelInfo.position.y + (targetPosition.y - itemModelInfo.position.y) * animatable.value
            scaleX = scale
            scaleY = scale
        }
    ) {
        itemModelInfo.composable()
    }
}


@Composable
fun CartTarget(
    content: @Composable () -> Unit
) {
    val state = LocalAddToCartState.current

    Box(modifier = Modifier.onGloballyPositioned {
        state.targetPosition = it.localToWindow(Offset.Zero)
    }
    ) {
        content()
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

    LaunchedEffect(key1 = key, key2 = itemInfo.value) {
        state.itemInfo(key, itemInfo.value)
    }

    Box(modifier = Modifier.onGloballyPositioned {
        itemInfo.value = itemInfo.value.copy(position = it.localToWindow(Offset.Zero))
    }
    ) {
        content()
    }
}
