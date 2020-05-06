package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import cs371.finalproject.crashsafe.R

class SavedModelsFragment : Fragment() {

    companion object {
        fun newInstance(): SavedModelsFragment {
            return SavedModelsFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: VehicleRowAdapter
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_models, container, false)
        setupRecyclerView(view)
        initAuth()
        return view
    }

    private fun initAuth() {
        viewModel.observeFirebaseAuthLiveData().observe(viewLifecycleOwner, Observer {
            currentUser = it
            if (it != null) {
                viewModel.getSavedVehicles(it)
            }
        })
    }

    private fun setupRecyclerView(view: View) {
        adapter = VehicleRowAdapter(viewModel, true)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        setupObservers()
        viewModel.refreshKeywordSearchResults()
    }

    private fun setupObservers() {
        viewModel.observeSavedVehicles().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.updateList(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.observeCurrentVehicle().observe(viewLifecycleOwner, Observer {
            if (viewModel.switch) {
                val vehicleInfoFragment = VehicleInfoFragment.newInstance()
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.mainFrame, vehicleInfoFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit()
                viewModel.switch = false
            }
        })
    }
}