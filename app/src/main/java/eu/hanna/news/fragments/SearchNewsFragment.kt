package eu.hanna.news.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import eu.hanna.news.MainActivity
import eu.hanna.news.R
import eu.hanna.news.adapters.NewsAdapter
import eu.hanna.news.databinding.FragmentSearchNewsBinding
import eu.hanna.news.util.Constants.Companion.DELAY_TIME
import eu.hanna.news.util.Resource
import eu.hanna.news.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {

    private lateinit var binding: FragmentSearchNewsBinding
    lateinit var viewModel : NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater,container,false)
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
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,bundle)
        }

        // make a little time delay for search
        loadingTime()

        observesSearchNews()
    }

    private fun loadingTime() {
        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(DELAY_TIME)
                editable?.let {
                    if(editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun observesSearchNews() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("searchNews", "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    // set up the recyclerview
    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.loadingSearchProgress.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.loadingSearchProgress.visibility = View.VISIBLE
    }

}