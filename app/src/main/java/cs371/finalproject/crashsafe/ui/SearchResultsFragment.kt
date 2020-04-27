package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs371.finalproject.crashsafe.R

class SearchResultsFragment: Fragment() {
    companion object {
        fun newInstance(): SearchResultsFragment {
            return SearchResultsFragment()
        }
    }

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_results, container, false)
        setTitle(view)
        setupRecyclerView(view)
        return view
    }

    private fun setupRecyclerView(view: View) {
        val adapter = VehicleRowAdapter(viewModel)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.observeKeywordSearchResults().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.updateList(it)
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.refreshKeywordSearchResults()
    }

    private fun setTitle(view: View) {
        view.findViewById<TextView>(R.id.title).text =
            "Results for \"${viewModel.currentSearchStr}\""
    }
}