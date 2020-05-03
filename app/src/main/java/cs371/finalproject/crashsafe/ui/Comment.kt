package cs371.finalproject.crashsafe.ui

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Comment(
    var commentID: String = "",
    var userName: String? = null,
    var userUID: String? = null,
    @ServerTimestamp val timeStamp: Timestamp? = null,
    var content: String? = null
)