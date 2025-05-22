package com.dualand.app.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dualand.app.R

@Composable
fun InfoDialogContent(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(10.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Close Button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Close Button aligned to top end
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(onClick = onDismiss) {
                            Image(
                                painter = painterResource(id = R.drawable.close_btn),
                                contentDescription = "Close",
                                modifier = Modifier.size(width = 29.dp, height = 30.dp)
                            )
                        }
                    }

                    // Centered Texts
                    Text("DuaLand", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Version 1.0", fontSize = 14.sp, color = Color.Gray)
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text("Developed by MetaFront LLP", fontSize = 14.sp)

                val context = LocalContext.current
                ClickableText(
                    text = AnnotatedString("http://metafront.net"),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://metafront.net"))
                        context.startActivity(intent)
                    },
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("All rights reserved", fontSize = 14.sp)
                ClickableText(
                    text = AnnotatedString("Privacy policy"),
                    onClick = {
                        val url = "https://metafront.net/dualand/privacy-policy.html"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    style = TextStyle(
                        color = Color.Blue,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.Underline
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://metafront.net"))
                        context.startActivity(intent) },
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.highlited_color)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Website", color = Color.White)
                }
            }
        }
    }
}
