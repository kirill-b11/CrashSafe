package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import cs371.finalproject.crashsafe.R

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<Button>(R.id.searchButton).setOnClickListener {
            Log.d("test", "search clicked")
            val searchFragment = SearchFragment.newInstance()
            parentFragmentManager
                .beginTransaction()
                .add(R.id.mainFrame, searchFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
        }

        return view
    }




}