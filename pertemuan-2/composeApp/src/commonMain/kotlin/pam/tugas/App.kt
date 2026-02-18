package pam.tugas

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
    val selectedCategoryState: StateFlow<String> = selectedCategory

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount

    val newsFeed: StateFlow<List<NewsUi>> =
        repository.newsStream()
            .combine(selectedCategory) { news, category ->
                news.takeIf { it.category == category }
            }
            .filterNotNull()
            .map {
                NewsUi(
                    id = it.id,
                    title = it.title,
                    categoryLabel = "[${it.category}]"
                )
            }
            .scan(emptyList<NewsUi>()) { acc, value ->
                listOf(value) + acc
            }
            .stateIn(scope, SharingStarted.Eagerly, emptyList())

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


@Composable
fun CategoryItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        RadioButton(
            selected = selected,
            onClick = null
        )
    }
}

@Preview
@Composable
fun App() {
    val repository = remember { NewsRepository() }
    val viewModel = remember { NewsViewModel(repository) }

    val news by viewModel.newsFeed.collectAsState()
    val readCount by viewModel.readCount.collectAsState()
    val selectedCategory by viewModel.selectedCategoryState.collectAsState()

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CategoryItem(
                text = "Tech",
                selected = selectedCategory == "Tech",
                onClick = { viewModel.changeCategory("Tech") }
            )

            CategoryItem(
                text = "Sport",
                selected = selectedCategory == "Sport",
                onClick = { viewModel.changeCategory("Sport") }
            )

            CategoryItem(
                text = "Politics",
                selected = selectedCategory == "Politics",
                onClick = { viewModel.changeCategory("Politics") }
            )

            Text("Read News: $readCount")

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                items(news) { item ->
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
}
