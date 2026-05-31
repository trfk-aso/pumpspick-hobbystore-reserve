package pumpspick.hobbystore.reserve.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import pumpspick.hobbystore.reserve.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val HeadingFamily = FontFamily(
    Font(GoogleFont("Cormorant Garamond"), provider, weight = FontWeight.Bold)
)
val BodyFamily = FontFamily(
    Font(GoogleFont("Montserrat"), provider, weight = FontWeight.Normal)
)

val AppTypography = Typography(
    displayLarge   = TextStyle(fontFamily = HeadingFamily, fontWeight = FontWeight.Bold,    fontSize = 32.sp),
    headlineMedium = TextStyle(fontFamily = HeadingFamily, fontWeight = FontWeight.SemiBold, fontSize = 24.sp),
    titleLarge     = TextStyle(fontFamily = HeadingFamily, fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
    bodyLarge      = TextStyle(fontFamily = BodyFamily,    fontWeight = FontWeight.Normal,   fontSize = 16.sp),
    bodyMedium     = TextStyle(fontFamily = BodyFamily,    fontWeight = FontWeight.Normal,   fontSize = 14.sp),
    labelSmall     = TextStyle(fontFamily = BodyFamily,    fontWeight = FontWeight.Medium,   fontSize = 11.sp),
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary        = Primary,
            onPrimary      = OnPrimary,
            secondary      = Accent,
            background     = Background,
            surface        = Surface,
            onSurface      = OnSurface,
        ),
        typography = AppTypography,
        content = content
    )
}

@Composable
fun ProductAppSkeletonTheme(content: @Composable () -> Unit) = AppTheme(content)

@Composable
fun ServiceSkeletonTheme(content: @Composable () -> Unit) = AppTheme(content)
