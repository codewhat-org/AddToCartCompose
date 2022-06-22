package vn.begreat.addtocard.card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset

/**
 *
 */
typealias ComposableFunc = @Composable () -> Unit

internal data class CartItemModelInfo(
    val composable: ComposableFunc,
    val position: Offset = Offset.Zero
)

class AddToCartState {
    private val itemComposable: MutableMap<String, CartItemModelInfo> = mutableMapOf()

    var targetPosition: Offset? = null

    var selectedItem: MutableState<String> = mutableStateOf("")
        private set

    internal fun itemInfo(key: String, item: CartItemModelInfo) {
        itemComposable[key] = item
    }

    internal fun getInfo(key: String): CartItemModelInfo? = itemComposable[key]

    fun add(item: String) {
        selectedItem.value = item
    }

    fun stop() {
        selectedItem.value = ""
    }
}
