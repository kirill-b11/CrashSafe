package cs371.finalproject.crashsafe.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs371.finalproject.crashsafe.R
import cs371.finalproject.crashsafe.api.crashsafeapi.VehicleModel
import cs371.finalproject.crashsafe.glide.Glide

class VehicleRowAdapter(private val viewModel: SearchViewModel)
    : RecyclerView.Adapter<VehicleRowAdapter.VH>()  {

    private var list = mutableListOf<VehicleModel>()
    private var pos = 0

    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        private var vehicleImage = itemView.findViewById<ImageView>(R.id.vehicleImage)
        private var vehicleTitle = itemView.findViewById<TextView>(R.id.vehicleTitle)

        fun bind(vehicle: VehicleModel) {
            Glide.glideFetch(vehicle.img, VehicleInfoFragment.noImageURL, vehicleImage)
            vehicleTitle.text = "${vehicle.year} ${vehicle.make} ${vehicle.model}"
            vehicleTitle.setOnClickListener {
                viewModel.updateCurrentVehicle(vehicle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleRowAdapter.VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_model, parent, false)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VehicleRowAdapter.VH, position: Int) {
        pos = position
        holder.bind(list[position])
    }

    fun updateList(newList: List<VehicleModel> ) {
        list.clear()
        list.addAll(newList)
    }
}