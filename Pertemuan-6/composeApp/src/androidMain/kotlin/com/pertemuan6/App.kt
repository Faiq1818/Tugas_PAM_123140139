package com.pertemuan6

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pertemuan6.data.remote.RetrofitInstance
import com.pertemuan6.data.repository.NewsRepositoryImpl
import com.pertemuan6.ui.screen.NewsDetailScreen
import com.pertemuan6.ui.screen.NewsListScreen
import com.pertemuan6.ui.viewmodel.NewsViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val viewModel = viewModel<NewsViewModel> {
        NewsViewModel(NewsRepositoryImpl(RetrofitInstance.apiService))
    }

    MaterialTheme {
        NavHost(navController = navController, startDestination = "news_list") {
            composable("news_list") {
                NewsListScreen(
                    viewModel = viewModel,
                    onArticleClick = { article ->
                        viewModel.selectArticle(article)
                        navController.navigate("news_detail")
                    }
                )
            }
            composable("news_detail") {
                val article by viewModel.selectedArticle.collectAsStateWithLifecycle()
                article?.let {
                    NewsDetailScreen(
                        article = it,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
