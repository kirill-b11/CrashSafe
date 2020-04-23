package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import cs371.finalproject.crashsafe.R
import java.util.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

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

        return view
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
                Log.d("test", years[position])
                selectedYear = years[position]
            }

        }

    }
}