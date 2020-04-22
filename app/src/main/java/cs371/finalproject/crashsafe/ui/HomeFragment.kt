package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import cs371.finalproject.crashsafe.R

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<Button>(R.id.signoutButton).setOnClickListener {
            signOut()
        }
        return view
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(context!!)
            .addOnCompleteListener {
                sitchToSigninFragment()
            }
    }

    private fun sitchToSigninFragment() {
        val signinFragment = SigninFragment.newInstance()
        requireFragmentManager()
            .beginTransaction()
            .replace(R.id.mainFrame, signinFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }
}