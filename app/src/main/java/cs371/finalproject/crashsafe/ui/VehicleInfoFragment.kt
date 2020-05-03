package cs371.finalproject.crashsafe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseUser
import cs371.finalproject.crashsafe.R
import cs371.finalproject.crashsafe.glide.Glide
import kotlinx.android.synthetic.main.fragment_vehicle_info.*

class VehicleInfoFragment : Fragment() {
    companion object {
        fun newInstance(): VehicleInfoFragment {
            return VehicleInfoFragment()
        }

        const val noImageURL = "https://cdn1.iconfinder.com/data/icons/cars-journey/91/Cars__Journey_68-512.png"
    }

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var commentAdapter: CommentAdapter
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_info, container, false)
        bindView(view)
        initRecyclerView(view)
        initCommentCompose(view)
        initObservers()
        initAuth()
        return view
    }

    private fun initObservers() {
        viewModel.observeComments().observe(viewLifecycleOwner, Observer {
            commentAdapter.updateList(it)
            commentAdapter.notifyDataSetChanged()
        })
    }

    private fun initAuth() {
        viewModel.observeFirebaseAuthLiveData().observe(viewLifecycleOwner, Observer {
            currentUser = it
        })
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

    private fun initRecyclerView(view: View) {
        val commentsRV = view.findViewById<RecyclerView>(R.id.commentsRV)
        commentAdapter = CommentAdapter(viewModel)
        commentsRV.adapter = commentAdapter
        commentsRV.layoutManager = LinearLayoutManager(context)
    }

    private fun initCommentCompose(view: View) {
        view.findViewById<Button>(R.id.postButton).setOnClickListener {
            val cUser = currentUser
            if(commentET.text.isNotEmpty() && cUser != null) {
                val comment = Comment().apply {
                    content = commentET.text.toString()
                    userName = cUser.displayName
                    if (userName!!.isEmpty()) {
                        userName = "Anonymous"
                    }
                    userUID = cUser.uid
                }
                commentET.text.clear()
                viewModel.saveComment(comment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getComments()
    }
}