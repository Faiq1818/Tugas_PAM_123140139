package pam.tugas

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsViewModel(
    private val repository: NewsRepository
) {

    private val scope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

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
