package cs371.finalproject.crashsafe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import cs371.finalproject.crashsafe.ui.HomeFragment
import cs371.finalproject.crashsafe.ui.SigninFragment

class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    private lateinit var signinFragment: SigninFragment

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            homeFragment = HomeFragment.newInstance()
            initHomeFragment()
        } else {
            signinFragment = SigninFragment.newInstance()
            initSigninFragment()
        }
    }

    private fun initSigninFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, signinFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initHomeFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }
}
