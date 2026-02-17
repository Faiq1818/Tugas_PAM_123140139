package pam.tugas

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
