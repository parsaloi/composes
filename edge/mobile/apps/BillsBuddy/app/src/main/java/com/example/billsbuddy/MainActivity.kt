package com.example.billsbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.billsbuddy.ui.screens.BillsScreen
import com.example.billsbuddy.ui.theme.BillsBuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BillsBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BillsScreen()
                }
            }
        }
    }
}