package pam.tugas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository {
    private val categories = listOf("Tech", "Sport", "Politics")
    fun newsStream(): Flow<News> = flow {
        var id = 1
        while (true) {
            delay(2000)
            val category = categories.random()
            emit(
                News(
                    id = id,
                    title = "Breaking News $id",
                    category = category,
                    content = "Isi berita kategori $category"
                )
            )
            id++
        }
    }
    suspend fun getNewsDetail(id: Int): String {
        delay(1000)
        return "Detail lengkap berita id $id"
    }
}

class NewsViewModel(
    private val repository: NewsRepository
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val selectedCategory = MutableStateFlow("Tech")
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount
    val newsFeed: StateFlow<List<NewsUi>> =
        repository.newsStream()
            // filter kategori
            .filter { it.category == selectedCategory.value }
            // transform ke format UI
            .map {
                NewsUi(
                    id = it.id,
                    title = it.title,
                    categoryLabel = "[${it.category}]"
                )
            }
            // kumpulkan menjadi list feed
            .scan(emptyList<NewsUi>()) { acc, value ->
                listOf(value) + acc
            }
            .stateIn(
                scope,
                SharingStarted.Eagerly,
                emptyList()
            )
    fun changeCategory(category: String) {
        selectedCategory.value = category
    }
    fun markAsRead() {
        _readCount.value += 1
    }
    fun loadDetailAsync(id: Int, onResult: (String) -> Unit) {
        scope.launch {
            val detail = repository.getNewsDetail(id)
            onResult(detail)
        }
    }
}

data class News(
    val id: Int,
    val title: String,
    val category: String,
    val content: String
)

data class NewsUi(
    val id: Int,
    val title: String,
    val categoryLabel: String
)

@Preview
@Composable
fun App() {

    val repository = remember { NewsRepository() }
    val viewModel = remember { NewsViewModel(repository) }

    val news by viewModel.newsFeed.collectAsState()
    val readCount by viewModel.readCount.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
