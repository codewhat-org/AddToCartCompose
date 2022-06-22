package vn.begreat.addtocard.items

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.begreat.addtocard.R
import vn.begreat.addtocard.card.AddToCartState
import vn.begreat.addtocard.card.CartItemTarget
import vn.begreat.addtocard.card.rememberAddToCartState
import vn.begreat.addtocard.ui.theme.AddToCardComposeTheme


class CartItemModel(
    val id: Int,
    @DrawableRes val drawableId: Int,
    val name: String
)


private val cartItems = listOf(
    CartItemModel(id = 1, drawableId = R.drawable.product_1, name = "Product 1"),
    CartItemModel(id = 2, drawableId = R.drawable.product_2, name = "Product 2"),
    CartItemModel(id = 3, drawableId = R.drawable.product_3, name = "Product 3"),
    CartItemModel(id = 4, drawableId = R.drawable.product_4, name = "Product 4"),
    CartItemModel(id = 5, drawableId = R.drawable.product_5, name = "Product 5"),
    CartItemModel(id = 6, drawableId = R.drawable.product_6, name = "Product 6"),
    CartItemModel(id = 7, drawableId = R.drawable.product_6, name = "Product 7"),
    CartItemModel(id = 8, drawableId = R.drawable.product_6, name = "Product 8"),
    CartItemModel(id = 9, drawableId = R.drawable.product_6, name = "Product 9"),
    CartItemModel(id = 10, drawableId = R.drawable.product_6, name = "Product 10"),
    CartItemModel(id = 11, drawableId = R.drawable.product_6, name = "Product 11"),
)


@Composable
fun CartItemList(
    modifier: Modifier = Modifier,
    carts: List<CartItemModel> = cartItems,
    addToCardState: AddToCartState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        items(items = carts, key = { it.id }) { data ->
            CardItemView(
                model = data,
                addToCardState = addToCardState
            )
        }
    }
}

@Composable
fun CardItemView(
    model: CartItemModel,
    addToCardState: AddToCartState,
) {

    val modelKey = "${model.id}"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CartItemTarget(
            key = modelKey,
        ) {
            Image(
                painter = painterResource(model.drawableId),
                contentDescription = "",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.width(20.dp))
        Text(text = model.name, fontSize = 16.sp)

        Spacer(modifier = Modifier.weight(1f))


        TextButton(onClick = {
            addToCardState.add(modelKey)
        }) {
            Text(text = "Add to cart")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardItemViewPreview() {
    AddToCardComposeTheme {
        CardItemView(
            model = CartItemModel(
                id = 1,
                drawableId = R.drawable.product_1, name = "Product 1",
            ),
            addToCardState = rememberAddToCartState(),
        )
    }
}
