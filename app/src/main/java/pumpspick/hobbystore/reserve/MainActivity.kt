package pumpspick.hobbystore.reserve

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pumpspick.hobbystore.reserve.ui.composable.approot.AppRoot
import pumpspick.hobbystore.reserve.ui.theme.ProductAppFECTBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductAppFECTBTheme {
                AppRoot()
            }
        }
    }
}