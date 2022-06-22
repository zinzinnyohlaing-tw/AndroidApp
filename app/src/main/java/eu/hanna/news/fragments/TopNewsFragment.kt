package eu.hanna.news.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.hanna.news.MainActivity
import eu.hanna.news.R
import eu.hanna.news.adapters.NewsAdapter
import eu.hanna.news.databinding.FragmentBreakingNewsBinding
import eu.hanna.news.util.Resource
import eu.hanna.news.viewmodel.NewsViewModel
//import kotlinx.android.synthetic.main.fragment_breaking_news.*

class TopNewsFragment : Fragment() {

    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        // Take the article and put it into the bundle,attach the bundle to the navigation component
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
        }

        observeBreakingNews()
       // viewModel.getBreakingNews("us")

    }

    // observe the breakingnews
    private fun observeBreakingNews(){
        try {
            //observe the breaking news from the viewmodel
            viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
                when(response) {
                    is Resource.Success -> {
                        hideProgressBar()

                        // checking if the response data is not null
                        response.data?.let { newsResponse ->
                            newsAdapter.differ.submitList(newsResponse.articles)
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            Log.e("breaking", "An error occured: $message")
                        }
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            })
        }catch (e:Exception){
            Log.e("Error","$e")
        }

    }

    // set up the recyclerview
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}