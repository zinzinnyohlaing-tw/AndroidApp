package eu.hanna.news.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import eu.hanna.news.MainActivity
import eu.hanna.news.R
import eu.hanna.news.adapters.NewsAdapter
import eu.hanna.news.databinding.FragmentBookmarkedBinding
import eu.hanna.news.viewmodel.NewsViewModel

class BookmarkedFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkedBinding
    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkedBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        setupRecyclerView()

        // navigate from savefragment to article fragment
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_bookmarkedFragment_to_articleFragment,
                bundle
            )
        }

        // swipe to delete the news at recycler view
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvBookmarkedNews)
        }

        // observe the news from the db
        observeBookmarkedNews()
    }

    private fun observeBookmarkedNews() {
        viewModel.getSavedNews().observe(viewLifecycleOwner,Observer{ articles ->
            newsAdapter.differ.submitList(articles)

        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBookmarkedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}
