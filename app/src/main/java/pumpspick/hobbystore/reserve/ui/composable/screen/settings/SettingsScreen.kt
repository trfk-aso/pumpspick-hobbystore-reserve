package pumpspick.hobbystore.reserve.ui.composable.screen.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Support
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pumpspick.hobbystore.reserve.ui.theme.*

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, color = OnSurface) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("About", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Muted, letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp))
            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Surface), elevation = CardDefaults.cardElevation(1.dp)) {
                Column {
                    SettingsRow(label = "App Name", value = "Pumps Pick")
                    Divider(color = Border, thickness = 0.5.dp)
                    SettingsRow(label = "Company", value = "PUMPS VENTURES LTD")
                    Divider(color = Border, thickness = 0.5.dp)
                    SettingsRow(label = "Version", value = "1.0.0")
                }
            }

            Text("Legal", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Muted, letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp))
            Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Surface), elevation = CardDefaults.cardElevation(1.dp)) {
                Column {
                    SettingsLinkRow(label = "Privacy Policy") {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://pumps-ventures.click/privacy")))
                    }
                    Divider(color = Border, thickness = 0.5.dp)
                    SettingsLinkRow(label = "Terms of Service") {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://pumps-ventures.click/terms")))
                    }
                }
            }

            Text("Support", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Muted, letterSpacing = androidx.compose.ui.unit.TextUnit(1f, androidx.compose.ui.unit.TextUnitType.Sp))
            Button(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://pumps-ventures.click")))
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OnSurface),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Filled.Support, contentDescription = null, tint = Surface, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Customer Support", fontFamily = HeadingFamily, fontWeight = FontWeight.Bold, color = Surface)
            }
        }
    }
}

@Composable
private fun SettingsRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontFamily = BodyFamily, fontSize = 15.sp, color = OnSurface)
        Text(value, fontFamily = BodyFamily, fontSize = 14.sp, color = Muted)
    }
}

@Composable
private fun SettingsLinkRow(label: String, onClick: () -> Unit) {
    TextButton(onClick = onClick, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(label, fontFamily = BodyFamily, fontSize = 15.sp, color = OnSurface)
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Muted, modifier = Modifier.size(18.dp))
        }
    }
}
