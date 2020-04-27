package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import cs371.finalproject.crashsafe.R

class VehicleInfoFragment : Fragment() {
    companion object {
        fun newInstance(): VehicleInfoFragment {
            return VehicleInfoFragment()
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
        val view = inflater.inflate(R.layout.fragment_vehicle_info, container, false)
        return view
    }
}