package cs371.finalproject.crashsafe.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs371.finalproject.crashsafe.api.newsapi.NewsApi
import cs371.finalproject.crashsafe.api.newsapi.NewsArticle
import cs371.finalproject.crashsafe.api.newsapi.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val redditApi = NewsApi.create()
    private val repository = NewsRepository(redditApi)
    private val articles = MutableLiveData<List<NewsArticle>>()

    fun updatePosts() {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            articles.postValue(repository.fetchArticles())
        }
    }

    fun observeArticles(): LiveData<List<NewsArticle>> {
        return articles
    }
}