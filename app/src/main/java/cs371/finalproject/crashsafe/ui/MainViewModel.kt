package cs371.finalproject.crashsafe.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import cs371.finalproject.crashsafe.api.crashsafeapi.CrashSafeApi
import cs371.finalproject.crashsafe.api.crashsafeapi.CrashSafeRepository
import cs371.finalproject.crashsafe.api.crashsafeapi.VehicleModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val crashSafeApi = CrashSafeApi.create()
    private val repository = CrashSafeRepository(crashSafeApi)
    private val keywordSearchResults = MutableLiveData<List<VehicleModel>>()
    private val currentVehicle = MutableLiveData<VehicleModel>()
    var currentSearchStr = ""
    var switch = true //used to prevent reinitialization of VehicleInfoFragment (couldn't find any other way to prevent it)
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuthLiveData = FirestoreAuthLiveData()
    private var comments = MutableLiveData<List<Comment>>()
    private var listener: ListenerRegistration? = null

    fun searchModels(searchStr: String) {
        currentSearchStr = searchStr
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            keywordSearchResults.postValue(repository.fetchModelsSearch(searchStr))
        }
    }

    fun searchModel(modelName: String) {
        viewModelScope.launch(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            currentVehicle.postValue(repository.fetchModel(modelName))
        }
    }

    fun observeComments(): LiveData<List<Comment>> {
        return comments
    }

    fun observeFirebaseAuthLiveData(): LiveData<FirebaseUser?> {
        return firebaseAuthLiveData
    }

    fun saveComment(comment: Comment) {
        if (comment.content!!.isNotEmpty()) {
            //create document representing model if it doesn't already exist
            db.collection("models").document(currentVehicle.value!!.id.toString())
                .set(mapOf("id" to currentVehicle.value!!.id))

            //add comment to database
            comment.commentID = db.collection("models")
                .document(currentVehicle.value!!.id.toString())
                .collection("comments")
                .document().id
            db.collection("models")
                .document(currentVehicle.value!!.id.toString())
                .collection("comments")
                .document(comment.commentID)
                .set(comment)
                .addOnSuccessListener {
                    Log.d("test", "saved success")
                }
                .addOnFailureListener { e ->
                    Log.d(javaClass.simpleName, "Row create FAILED")
                    Log.w(javaClass.simpleName, "Error ", e)
                }
        }
    }

    fun observeKeywordSearchResults(): LiveData<List<VehicleModel>> {
        return keywordSearchResults
    }

    fun refreshKeywordSearchResults() {
        keywordSearchResults.value = keywordSearchResults.value
    }

    fun updateCurrentVehicle(vehicle: VehicleModel?) {
        switch = true
        currentVehicle.value = vehicle
    }

    fun observeCurrentVehicle(): LiveData<VehicleModel> {
        return currentVehicle
    }
}