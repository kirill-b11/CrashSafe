package cs371.finalproject.crashsafe.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cs371.finalproject.crashsafe.api.crashsafeapi.CrashSafeApi
import cs371.finalproject.crashsafe.api.crashsafeapi.CrashSafeRepository
import cs371.finalproject.crashsafe.api.crashsafeapi.VehicleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {
    private val crashSafeApi = CrashSafeApi.create()
    private val repository = CrashSafeRepository(crashSafeApi)
    private val keywordSearchResults = MutableLiveData<List<VehicleModel>>()
    var currentSearchStr = ""

    fun searchModels(searchStr: String) {
        currentSearchStr = searchStr
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            keywordSearchResults.postValue(repository.fetchModelsSearch(searchStr))
        }
    }

    fun observeKeywordSearchResults(): LiveData<List<VehicleModel>> {
        return keywordSearchResults
    }

    fun refreshKeywordSearchResults() {
        keywordSearchResults.value = keywordSearchResults.value
    }
}