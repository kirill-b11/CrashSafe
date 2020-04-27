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
import okhttp3.internal.wait
import java.util.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    private val viewModel: SearchViewModel by activityViewModels()

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
        return view
    }

    private fun setupListeners(view: View) {
        view.findViewById<Button>(R.id.keywordSearchButton).setOnClickListener {
            val searchStr = view.findViewById<EditText>(R.id.keywordSearchET).text.toString()
            if (searchStr.isEmpty()) {
                Toast.makeText(context, "Please enter a search", Toast.LENGTH_SHORT).show()
            } else {
                searchAndSwitchFragment(searchStr)
            }
        }
    }

    private fun searchAndSwitchFragment(searchStr: String) {
        viewModel.searchModels(searchStr)
        val searchResultsFragment = SearchResultsFragment.newInstance()
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.mainFrame, searchResultsFragment)
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
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, years)
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