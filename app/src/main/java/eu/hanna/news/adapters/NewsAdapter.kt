package eu.hanna.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import eu.hanna.news.R
import eu.hanna.news.databinding.ItemArticleBinding
import eu.hanna.news.model.Article
//import kotlinx.android.synthetic.main.item_article.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>(){

    class ArticleViewHolder(val itemBinding: ItemArticleBinding) : RecyclerView.ViewHolder(itemBinding.root)

    // To compare two lists and update only the update list
    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        Glide.with(holder.itemView)
            .load(article.urlToImage)
            .into(holder.itemBinding.ivArticleImage)

        holder.itemBinding.tvSource.text = article.source.name
        holder.itemBinding.tvTitle.text = article.title
        holder.itemBinding.tvDescription.text = article.description
        holder.itemBinding.tvPublishedAt.text = article.publishedAt

        // call the setOnItemClickListener
        holder.itemView.setOnClickListener {
            // check itemclicklistener is not equal to null
            onItemClickListener?.let {
                it(article)
            }
        }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}