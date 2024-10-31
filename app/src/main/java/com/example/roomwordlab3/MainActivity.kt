package com.example.roomwordlab3

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.roomwordlab3.ui.theme.RoomWordLab3Theme

class MainActivity : ComponentActivity() {

    private val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomWordLab3Theme {
                WordApp(wordViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppBar(wordViewModel: WordViewModel) {
    TopAppBar(
        title = { Text(stringResource(R.string.app_name)) },
        actions = {
            IconButton(onClick = { wordViewModel.deleteAll() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete All")
            }
        }
    )
}

@Composable
fun WordApp(wordViewModel: WordViewModel){
    wordViewModel.insert(Word("hello"))
    wordViewModel.insert(Word("world"))
    val words by wordViewModel.allWords.observeAsState(emptyList())
    val context = LocalContext.current
    val newWordActivityLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra(EXTRA_REPLY)?.let { word ->
                if (words.any { it.word == word }) {
                    Toast.makeText(context, "Word already exists", Toast.LENGTH_SHORT).show()
                } else {
                    wordViewModel.insert(Word(word))
                }
            }
        }
    }
    Scaffold(
        topBar = { AddAppBar(wordViewModel) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {  val intent = Intent(context, NewWordActivity::class.java)
                    newWordActivityLauncher.launch(intent)
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "add_word")
            }
        }

    ) { innerPadding ->
        val words by wordViewModel.allWords.observeAsState()
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(words.orEmpty()) {
                    word ->
                ListItem(
                    title = word.word,
                    modifier = Modifier.fillMaxWidth(),
                    onDelete = { wordViewModel.deleteWord(word.word) }
                )
            }
        }

    }
}

@Composable
fun ListItem(title: String, modifier: Modifier, onDelete: () -> Unit) {
    Row(modifier = modifier) {
        Text(text = title, modifier = Modifier.weight(1f).padding(16.dp))
        Button(onClick = onDelete) {
            Text(stringResource(R.string.delete))
        }
    }
}