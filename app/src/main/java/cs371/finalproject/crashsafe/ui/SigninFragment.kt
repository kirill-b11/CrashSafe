package cs371.finalproject.crashsafe.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import cs371.finalproject.crashsafe.MainActivity
import cs371.finalproject.crashsafe.R

class SigninFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 123

        fun newInstance(): SigninFragment {
            return SigninFragment()
        }
    }

    val providersSignin = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
    val providersGuest = arrayListOf(AuthUI.IdpConfig.AnonymousBuilder().build())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        val loginBut = view.findViewById<Button>(R.id.signinButton).setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providersSignin)
                    .build(),
                RC_SIGN_IN
            )
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.d("test","${user?.displayName}")
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.d("test","sign in failed")
            }
        }
    }
}