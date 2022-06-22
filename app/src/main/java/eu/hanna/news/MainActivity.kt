package eu.hanna.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import eu.hanna.news.database.ArticleDatabase
import eu.hanna.news.repository.NewsRepository
import eu.hanna.news.viewmodel.NewsViewModel
import eu.hanna.news.viewmodel.NewsViewModelProviderFactory
//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

        // inflate all the fragments
        //bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        // Inflate the bottom navigation and set up the bottom navigation with navigation controller

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = Navigation.findNavController(this, R.id.newsNavHostFragment)
        NavigationUI.setupWithNavController(bottomNavigation,navController)
    }
}