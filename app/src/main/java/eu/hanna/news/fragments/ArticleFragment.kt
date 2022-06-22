package eu.hanna.news.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import eu.hanna.news.MainActivity
import eu.hanna.news.R
import eu.hanna.news.databinding.FragmentArticleBinding
import eu.hanna.news.viewmodel.NewsViewModel

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding
    lateinit var viewModel : NewsViewModel
    val args: ArticleFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        // apply the onclick item to the webview
        val article = args.article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.fab.setOnClickListener {
            try{
                viewModel.saveArticle(article)
                Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show()
            } catch (e:Exception){
                //Log.e("message",$e.message)
            }
        }
    }


}