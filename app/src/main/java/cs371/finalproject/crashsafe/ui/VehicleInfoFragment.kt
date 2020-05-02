package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import cs371.finalproject.crashsafe.R
import cs371.finalproject.crashsafe.glide.Glide

class VehicleInfoFragment : Fragment() {
    companion object {
        fun newInstance(): VehicleInfoFragment {
            return VehicleInfoFragment()
        }

        const val noImageURL = "https://cdn1.iconfinder.com/data/icons/cars-journey/91/Cars__Journey_68-512.png"
    }

    private val viewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_info, container, false)
        bindView(view)
        return view
    }

    private fun bindView(view: View) {
        val vehicle = viewModel.observeCurrentVehicle().value!!
        //title
        val vehTitle = "${vehicle.year} ${vehicle.make} ${vehicle.model}"
        view.findViewById<TextView>(R.id.title).text = vehTitle
        //image
        val vehImg = view.findViewById<ImageView>(R.id.vehiclePicture)
        Glide.glideFetch(vehicle.img, noImageURL, vehImg)
        //IIHS
        view.findViewById<TextView>(R.id.IIHS_moderateOverlapTV).text = vehicle.IIHS_frontModerateOverlap
        view.findViewById<TextView>(R.id.IIHS_smallOverlapTV).text = vehicle.IIHS_frontSmallOverlap
        view.findViewById<TextView>(R.id.IIHS_sideCrashTV).text = vehicle.IIHS_side
        view.findViewById<TextView>(R.id.IIHS_rolloverTV).text = vehicle.IIHS_rollover
        view.findViewById<TextView>(R.id.IIHS_rearCrashTV).text = vehicle.IIHS_rear
        //NHTSA
        view.findViewById<TextView>(R.id.NHTSA_overallTV).text = vehicle.NHTSA_overallRating
        view.findViewById<TextView>(R.id.NHTSA_sideCrashTV).text = vehicle.NHTSA_overallSideCrashRating
        view.findViewById<TextView>(R.id.NHTSA_frontCrashTV).text = vehicle.NHTSA_overallFrontCrashRating
        view.findViewById<TextView>(R.id.NHTSA_rolloverTV).text = vehicle.NHTSA_rolloverRating
        //Specs
        view.findViewById<TextView>(R.id.engineTypeTV).text = vehicle.engineType
        view.findViewById<TextView>(R.id.enginePowerTV).text = vehicle.horsePowers
        view.findViewById<TextView>(R.id.vehTypeTV).text = vehicle.vehicleType
    }

    fun initRecyclerView(view: View) {
        val commentsRV = view.findViewById<RecyclerView>(R.id.commentsRV)
    }
}