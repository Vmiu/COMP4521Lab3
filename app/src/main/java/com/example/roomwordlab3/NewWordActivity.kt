package com.example.roomwordlab3

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

const val EXTRA_REPLY = "com.example.roomwordlab3.REPLY"

class NewWordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewWordScreen()
        }
    }

    @Composable
    fun NewWordScreen() {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                var word by remember { mutableStateOf("") }

                TextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text(stringResource(R.string.input_field)) },
                    modifier = Modifier.padding(16.dp)
                )

                Button(
                    onClick = {
                        val replyIntent = Intent()
                        if (TextUtils.isEmpty(word)) {
                            setResult(RESULT_CANCELED, replyIntent)
                        } else {
                            replyIntent.putExtra(EXTRA_REPLY, word)
                            setResult(RESULT_OK, replyIntent)
                        }
                        finish()
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(stringResource(R.string.button_save))
                }
            }
        }
    }
}