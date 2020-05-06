package cs371.finalproject.crashsafe

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import cs371.finalproject.crashsafe.ui.HomeFragment
import cs371.finalproject.crashsafe.ui.SavedModelsFragment
import cs371.finalproject.crashsafe.ui.SearchFragment
import cs371.finalproject.crashsafe.ui.SigninFragment


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var homeFragment: HomeFragment
    private lateinit var signinFragment: SigninFragment
    private lateinit var savedModelsFragment: SavedModelsFragment
    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeFragment = HomeFragment.newInstance()
        signinFragment = SigninFragment.newInstance()
        savedModelsFragment = SavedModelsFragment.newInstance()
        searchFragment = SearchFragment.newInstance()

        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            initFragment(homeFragment)
        } else {
            initFragment(signinFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.findItem(R.id.name)?.title = FirebaseAuth.getInstance().currentUser?.displayName
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> switchToFragment(homeFragment)
            R.id.search -> switchToFragment(searchFragment)
            R.id.starred -> switchToFragment(savedModelsFragment)
            R.id.signout -> signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(applicationContext)
            .addOnCompleteListener {
                val signinFragment = SigninFragment.newInstance()
                switchToFragment(signinFragment)
            }
    }

    private fun switchToFragment(fragment: Fragment) {
        if (fragment is SigninFragment) {
            //Clear back stack
            for (i in 0..supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainFrame, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainFrame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }
}
