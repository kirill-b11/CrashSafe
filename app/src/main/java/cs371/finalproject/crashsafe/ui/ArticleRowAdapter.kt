package cs371.finalproject.crashsafe.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import cs371.finalproject.crashsafe.R
import cs371.finalproject.crashsafe.api.newsapi.NewsArticle
import cs371.finalproject.crashsafe.glide.Glide

class ArticleRowAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<ArticleRowAdapter.VH>() {

    private val noImageURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRz11g0zDpzs09W5rI4roWOz__EKQTaZJqen49NBP61LsX6GNAa&usqp=CAU"
    private var list = mutableListOf<NewsArticle>()

    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        private var articleImage = itemView.findViewById<ImageView>(R.id.articleImage)
        private var articleTitle = itemView.findViewById<TextView>(R.id.articleTitle)
        private var articleDescription = itemView.findViewById<TextView>(R.id.articleDescription)

        fun bind(article: NewsArticle) {
            val imgUrl = article.imageUrl
            Glide.glideFetch(imgUrl, noImageURL, articleImage)
            articleTitle.text = article.title
            articleDescription.text = article.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_article, parent, false)
        return VH(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    fun updateList(newList: List<NewsArticle> ) {
        list.clear()
        list.addAll(newList)
    }
}