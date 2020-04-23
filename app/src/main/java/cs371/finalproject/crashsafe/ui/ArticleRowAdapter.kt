package cs371.finalproject.crashsafe.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

class ArticleRowAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<ArticleRowAdapter.VH>() {

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // (itemView.context as FragmentActivity).supportFragmentManager.popBackStack()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        TODO("Not yet implemented")
    }

}