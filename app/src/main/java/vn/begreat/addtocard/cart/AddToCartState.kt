package vn.begreat.addtocard.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

/**
 *
 */
typealias ComposableFunc = @Composable () -> Unit

internal data class CartItemModelInfo(
    val composable: ComposableFunc,
    val position: Offset = Offset.Zero,
    val size: IntSize = IntSize.Zero,
)

class AddToCartState {
    private val itemComposable: MutableMap<String, CartItemModelInfo?> = mutableMapOf()
    private val itemCount: MutableMap<String, Int> = mutableMapOf()

    var targetPosition: Offset = Offset.Zero

    var selectedKey: MutableState<String> = mutableStateOf("")
        private set

    var totalCount: MutableState<Int> = mutableStateOf(0)
        private set

    internal fun itemInfo(key: String, item: CartItemModelInfo?) {
        itemComposable[key] = item
    }

    internal fun getInfo(key: String): CartItemModelInfo? = itemComposable[key]

    private var lastTime = 0L

    fun add(key: String) {
        val time = System.currentTimeMillis()
        if (time - lastTime > 450) {
            lastTime = time
            selectedKey.value = key
            itemCount[key] = (itemCount[key] ?: 0) + 1

            totalCount.value = itemCount.values.sum()
        }
    }

    fun stop() {
        selectedKey.value = ""
    }
}
