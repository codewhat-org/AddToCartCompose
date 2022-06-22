package vn.begreat.addtocard

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vn.begreat.addtocard.cart.AddToCartContainer
import vn.begreat.addtocard.cart.AddToCartState
import vn.begreat.addtocard.cart.CartTarget
import vn.begreat.addtocard.cart.rememberAddToCartState
import vn.begreat.addtocard.items.CartItemList
import vn.begreat.addtocard.ui.theme.AddToCardComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddToCardComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {

    val addToCardState: AddToCartState = rememberAddToCartState()

    AddToCartContainer(addToCardState = addToCardState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Text(text = "Add To cart")
                    },
                    modifier = Modifier.statusBarsPadding(),
                    actions = {
                        CartTarget {
                            IconButton(onClick = { }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                )
            }
        ) {
            CartItemList(
                addToCardState = addToCardState,
                modifier = Modifier.padding(top = it.calculateTopPadding())
            )
        }
    }
}
