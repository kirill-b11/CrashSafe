package cs371.finalproject.crashsafe.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import cs371.finalproject.crashsafe.R

class SigninFragment : Fragment() {
    companion object {
        private const val RC_SIGN_IN = 123

        fun newInstance(): SigninFragment {
            return SigninFragment()
        }
    }

    private lateinit var auth: FirebaseAuth
    private val providersSignin = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
    private val providersGuest = arrayListOf(AuthUI.IdpConfig.AnonymousBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        // Listeners for all the buttons
        view.findViewById<Button>(R.id.signinButton).setOnClickListener {
            startSigninActivity()
        }
        view.findViewById<Button>(R.id.signupButton).setOnClickListener {
            startSigninActivity()
        }
        view.findViewById<Button>(R.id.contAsGuestButton).setOnClickListener {
            anonymousSignin()
        }
        view.findViewById<Button>(R.id.exitButton).setOnClickListener {
            activity?.moveTaskToBack(true)
            activity?.finish()
        }

        return view
    }

    private fun startSigninActivity() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providersSignin)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = auth.currentUser
                Log.d("test","${user?.displayName}")
                switchToHomeFragment()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.d("test","sign in failed")
            }
        }
    }

    private fun switchToHomeFragment() {
        val homeFragment = HomeFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun anonymousSignin() {
        auth.signInAnonymously()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("test", "signInAnonymously:success")
                    val user = auth.currentUser
                    switchToHomeFragment()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("test", "signInAnonymously:failure")
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}