package pam.tugas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun App() {

    val repository = remember { NewsRepository() }
    val viewModel = remember { NewsViewModel(repository) }

    val news by viewModel.newsFeed.collectAsState()
    val readCount by viewModel.readCount.collectAsState()

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {

            Text("Read News: $readCount")

            Button(onClick = { viewModel.changeCategory("Tech") }) {
                Text("Teh")
            }

            Button(onClick = { viewModel.changeCategory("Sport") }) {
                Text("Sport")
            }

            Button(onClick = { viewModel.changeCategory("Politics") }) {
                Text("Politics")
            }

            news.forEach { item ->
                Button(
                    onClick = {
                        viewModel.markAsRead()
                        viewModel.loadDetailAsync(item.id) {
                            println(it)
                        }
                    }
                ) {
                    Text("${item.categoryLabel} ${item.title}")
                }
            }
        }
    }
}
