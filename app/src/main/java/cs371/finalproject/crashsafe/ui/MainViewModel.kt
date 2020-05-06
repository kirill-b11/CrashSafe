package cs371.finalproject.crashsafe.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
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
    private var rating = MutableLiveData<UserRating>()
    private var modelsAverageRating = MutableLiveData<Float>()
    private var savedVehicles = MutableLiveData<List<VehicleModel>>()
    private var userSavedThisCar = MutableLiveData<Boolean>()

    //_____________________API calls related_____________________
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

    //_____________________User Comments related_____________________
    fun getComments() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            Log.d(javaClass.simpleName, "Can't get chat, no one is logged in")
            comments.value = listOf()
            return
        }

        val query = db.collection("models")
            .document(currentVehicle.value!!.id.toString())
            .collection("comments")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
        listener = query.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w("Error", "listen:error", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                comments.postValue(querySnapshot!!.documents.mapNotNull {
                    it.toObject(Comment::class.java)
                })
            }
    }

    fun saveComment(comment: Comment) {
        if (comment.content!!.isNotEmpty()) {
            //create document representing model if it doesn't already exist
            val docRef = db.collection("models").document(currentVehicle.value!!.id.toString())
            docRef.get()
                .addOnSuccessListener {
                    if (!it.exists()) {
                        docRef.set(mapOf("id" to currentVehicle.value!!.id,
                            "averageRating" to 0f,
                            "numRatings" to 0))
                    }
                }
                .addOnFailureListener {
                    Log.d("saveComment", "get failure")
                }

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
                    Log.d("saveComment", "success")
                }
                .addOnFailureListener { e ->
                    Log.d("saveComment", "Row create FAILED")
                    Log.w("saveComment", "Error ", e)
                }
        }
    }

    fun deleteComment(comment: Comment) {
        db.collection("models")
            .document(currentVehicle.value?.id.toString())
            .collection("comments")
            .document(comment.commentID)
            .delete()
            .addOnSuccessListener {
                Log.d("deleteComment", "success")
            }
            .addOnFailureListener {
                Log.d("deleteComment", "failure")
            }
    }

    //_____________________User Saved Vehicles Realted_____________________
    fun getSavedVehicles(user: FirebaseUser) {
        val docRef = db.collection("users")
            .document(user.uid)
        docRef.get()
            .addOnSuccessListener {
                Log.d("getSavedCars", "success")
                if (!it.exists()) {
                    savedVehicles.value = listOf()
                } else {
                    docRef.collection("savedVehicles")
                        .get()
                        .addOnSuccessListener {qs ->
                            savedVehicles.postValue(qs.documents.mapNotNull { ds ->
                                ds.toObject(VehicleModel::class.java)
                            })
                        }
                        .addOnFailureListener {
                            Log.d("getSavedCars", "no list")
                        }
                }
            }
            .addOnFailureListener {
                Log.d("getSavedCars", "failure")
            }
    }

    fun saveVehicle(user: FirebaseUser) {
        val docRef = db.collection("users")
            .document(user.uid)
        docRef.get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    docRef.set(mapOf("uid" to user.uid))
                }
            }
            .addOnFailureListener {
                Log.d("saveVehicle", "failure")
            }
        docRef.collection("savedVehicles")
            .document(currentVehicle.value?.id.toString())
            .set(currentVehicle.value!!)
            .addOnSuccessListener {
                Log.d("saveVehicle", "success")
            }
            .addOnFailureListener { e ->
                Log.d("saveVehicle", "Row create FAILED")
                Log.w("saveVehicle", "Error ", e)
            }
    }

    fun deleteVehicle(vehicleID: Int) {
        val cUser = FirebaseAuth.getInstance().currentUser
        if (cUser != null && !cUser.isAnonymous) {
            db.collection("users")
                .document(cUser.uid)
                .collection("savedVehicles")
                .document("$vehicleID")
                .delete()
                .addOnSuccessListener {
                    Log.d("deleteVehicle", "success")
                }
                .addOnFailureListener {
                    Log.d("deleteVehicle", "failure")
                }
        }
    }

    fun userSavedThisCar(user: FirebaseUser) {
        db.collection("users")
            .document(user.uid)
            .collection("savedVehicles")
            .document(currentVehicle.value?.id.toString())
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    userSavedThisCar.postValue(true)
                } else {
                    userSavedThisCar.postValue(false)
                }
            }
            .addOnFailureListener {
                Log.d("userSavedThisCar", "failure")
            }
    }

    //_____________________User Rating related_____________________
    fun saveRating(rating: UserRating) {
        if (rating.rating != 0f) {
            val docRef = db.collection("models")
                .document(currentVehicle.value!!.id.toString())
            docRef.get()
                .addOnSuccessListener {
                    if (!it.exists()) {
                        docRef.set(mapOf("id" to currentVehicle.value!!.id,
                            "averageRating" to 0f,
                            "numRatings" to 0))
                    }
                }
                .addOnFailureListener {
                    Log.d("saveRating", "get failure")
                }

            db.collection("models")
                .document(currentVehicle.value!!.id.toString())
                .collection("userRatings")
                .document(rating.userUID!!)
                .set(rating)
                .addOnSuccessListener {
                    Log.d("saveRating", "success")
                    adjustRating(rating.rating!!)
                }
                .addOnFailureListener {
                    Log.d("saveRating", "failure")
                }
        }
    }

    fun removeRating(user: FirebaseUser, rating: Float) {
        db.collection("models")
            .document(currentVehicle.value?.id.toString())
            .collection("userRatings")
            .document(user.uid)
            .delete()
            .addOnSuccessListener {
                Log.d("removeRating", "success")
                adjustRating(-rating)
            }
            .addOnFailureListener {
                Log.d("removeRating", "failure")
            }
    }

    fun getModelsAverageRating() {
        db.collection("models")
            .document(currentVehicle.value?.id.toString())
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    modelsAverageRating.postValue(it.toObject(FirestoreModel::class.java)?.averageRating)
                }
            }
            .addOnFailureListener {
                Log.d("getModelsAverageRating", "get failure")
            }
    }

    private fun adjustRating(rating: Float) {
        val docRef = db.collection("models")
            .document(currentVehicle.value?.id.toString())
        docRef.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val model = it.toObject(FirestoreModel::class.java)
                    if (model != null) {
                        var avgRating = model.averageRating!!
                        val numRatings = model.numRatings!!
                        if (rating > 0) {
                            avgRating = ((avgRating * numRatings) + rating) / (numRatings + 1)
                            model.numRatings = numRatings + 1
                        } else {
                            if (numRatings != 1) {
                                avgRating = ((avgRating * numRatings) + rating) / (numRatings - 1)
                                model.numRatings = numRatings - 1
                            } else {
                                avgRating = 0f
                                model.numRatings = 0
                            }
                        }
                        model.averageRating = avgRating
                        docRef.set(model)
                            .addOnSuccessListener {
                                getModelsAverageRating()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.d("adjustRating", "get failure")
            }
    }

    //check if current user already left a rating for current vehicle
    fun userHasRating(user: FirebaseUser) {
        val docRef = db.collection("models")
            .document(currentVehicle.value!!.id.toString())
            .collection("userRatings")
            .document(user.uid)

        docRef.get()
            .addOnSuccessListener {
                if (it.exists()) {
                    rating.postValue(it.toObject(UserRating::class.java))
                } else {
                    rating.postValue(UserRating())
                }
            }
            .addOnFailureListener {
                Log.d("userHasRating", "failure")
            }
    }

    //_____________________LiveData Observing_____________________
    fun observeComments(): LiveData<List<Comment>> {
        return comments
    }

    fun observeFirebaseAuthLiveData(): LiveData<FirebaseUser?> {
        return firebaseAuthLiveData
    }

    fun observeModelsAverageRating(): LiveData<Float> {
        return modelsAverageRating
    }

    fun observeRating(): LiveData<UserRating> {
        return rating
    }

    fun observeKeywordSearchResults(): LiveData<List<VehicleModel>> {
        return keywordSearchResults
    }

    fun observeCurrentVehicle(): LiveData<VehicleModel> {
        return currentVehicle
    }

    fun observeSavedVehicles(): LiveData<List<VehicleModel>> {
        return savedVehicles
    }

    fun observeUserSavedThisCar(): LiveData<Boolean> {
        return userSavedThisCar
    }

    //_____________________Miscellaneous_____________________
    fun refreshKeywordSearchResults() {
        keywordSearchResults.value = keywordSearchResults.value
    }

    fun updateCurrentVehicle(vehicle: VehicleModel?) {
        switch = true
        currentVehicle.value = vehicle
    }

    override fun onCleared() {
        Log.d(javaClass.simpleName, "onCleared!!")
        super.onCleared()
        listener?.remove()
    }
}