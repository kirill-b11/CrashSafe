package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import cs371.finalproject.crashsafe.R
import androidx.lifecycle.Observer
import java.util.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    private var selectedYear = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setupSpinner(view)
        setupListeners(view)
        setupObservers()
        return view
    }

    private fun setupObservers() {
        viewModel.observeCurrentVehicle().observe(viewLifecycleOwner, Observer {
            if (viewModel.switch) {
                val vehicleInfoFragment = VehicleInfoFragment.newInstance()
                switchToFragment(vehicleInfoFragment)
                viewModel.switch = false
            }
        })
    }

    private fun setupListeners(view: View) {
        view.findViewById<Button>(R.id.keywordSearchButton).setOnClickListener {
            val searchStr = view.findViewById<EditText>(R.id.keywordSearchET).text.toString()
            if (searchStr.isEmpty()) {
                Toast.makeText(context, "Please enter a search", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.searchModels(searchStr)
                val searchResultsFragment = SearchResultsFragment.newInstance()
                switchToFragment(searchResultsFragment)
            }
        }
        view.findViewById<Button>(R.id.nameSearchButton).setOnClickListener {
            viewModel.switch = true
            val make = view.findViewById<EditText>(R.id.makeET).text
            val model = view.findViewById<EditText>(R.id.modelET).text
            if (make.isEmpty()) {
                Toast.makeText(context, "Please enter vehicle's  make", Toast.LENGTH_SHORT).show()
            } else if (model.isEmpty()) {
                Toast.makeText(context, "Please enter vehicle's  model", Toast.LENGTH_SHORT).show()
            } else {
                val name = "$selectedYear $make $model"
                viewModel.searchModel(name)
            }
        }
    }

    private fun switchToFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit()
    }

    private fun setupSpinner(view: View) {
        val years = arrayListOf<String>()
        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in 1965..thisYear) {
            years.add(i.toString())
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        val spinner = view.findViewById<Spinner>(R.id.yearSpinner)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = years[position]
            }

        }

    }
}