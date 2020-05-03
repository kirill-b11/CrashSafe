package cs371.finalproject.crashsafe.ui

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cs371.finalproject.crashsafe.R
import kotlinx.android.synthetic.main.row_comment.view.*

class CommentAdapter(): RecyclerView.Adapter<CommentAdapter.VH>() {

    companion object {
        private val dateFormat =
            SimpleDateFormat("hh:mm:ss MM-dd-yyyy")
    }

    private var list = mutableListOf<Comment>()

    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        private var userTV = itemView.findViewById<TextView>(R.id.userTV)
        private var timeTV = itemView.findViewById<TextView>(R.id.timeTV)
        private var contentTV = itemView.findViewById<TextView>(R.id.contentTV)

        fun bind(comment: Comment) {
            if (comment.userName == null) {
                userTV.text = "Anonymous"
            } else {
                userTV.text = comment.userName
            }

            if (comment.content == null) {
                contentTV.text = "Nothing to see here.."
            } else {
                contentTV.text = comment.content
            }

            if (comment.timeStamp == null) {
                timeTV.text = ""
            } else {
                timeTV.text = dateFormat.format(comment.timeStamp.toDate())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_comment, parent, false)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentAdapter.VH, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(newList: List<Comment>) {
        list.clear()
        list.addAll(newList)
    }
}