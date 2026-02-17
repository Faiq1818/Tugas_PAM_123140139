package pam.tugas

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
