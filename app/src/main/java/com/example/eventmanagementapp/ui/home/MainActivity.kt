package com.example.eventmanagementapp.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.eventmanagementapp.R
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.databinding.ActivityMainBinding
import com.example.eventmanagementapp.ui.adapter.EventAdapter
import com.example.eventmanagementapp.ui.edit.EditEventActivity


class MainActivity : AppCompatActivity() {

    private var eventsEntityList: MutableList<EventEntity> = mutableListOf()
    private var mAdapter: EventAdapter? = null
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()
    private val ivMoreOption by lazy { binding.root.findViewById<ImageView>(R.id.ivMoreOption) }
    private val ivSearchOption by lazy { binding.root.findViewById<ImageView>(R.id.ivSearchOption) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initViewModel()
        setUpOnClickListeners()
        setUpSearchViewListener()
    }

    /**
     * Initializes the ViewModel and sets up the observer for the event list.
     */
    private fun initViewModel() {
        val eventsObserver: Observer<MutableList<EventEntity>> = Observer {
            eventsEntityList.clear()
            eventsEntityList.addAll(it)
            if (mAdapter == null) {
                mAdapter = EventAdapter(eventsEntityList)
                binding.rvEvent.adapter = mAdapter
            } else {
                mAdapter?.notifyDataSetChanged()
            }
            // Show or hide empty view based on the number of items in the adapter
            binding.tvEmptyView.visibility = if ((mAdapter?.itemCount ?: 0) > 0)
                View.GONE
            else
                View.VISIBLE
        }
        mainViewModel.mEvents.observe(this, eventsObserver)
    }

    /**
     * Sets up click listeners for the FloatingActionButton and menu options.
     */
    private fun setUpOnClickListeners() {
        binding.addFab.setOnClickListener {
            addFabClickHandler()
        }

        ivMoreOption.setOnClickListener {
            showPopupMenu(it)
        }

        ivSearchOption.setOnClickListener {
            if (!binding.searchView.isVisible) {
                if ((mAdapter?.itemCount ?: 0) > 0) {
                    binding.searchView.visibility = View.VISIBLE
                    binding.searchView.setIconifiedByDefault(true)
                    binding.searchView.isFocusable = true
                    binding.searchView.isIconified = false
                    binding.searchView.clearFocus()
                    binding.searchView.requestFocusFromTouch()
                } else
                    Toast.makeText(this, "No Events to Search", Toast.LENGTH_LONG).show()
            } else {
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                binding.searchView.visibility = View.GONE
            }
        }

        // Handle close button click on the search view
        val closeButton: View? = binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            if (binding.searchView.query.isEmpty()) {
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                binding.searchView.visibility = View.GONE
            } else {
                binding.searchView.setQuery("", false)
            }
        }
    }

    /**
     * Sets up the search view query text listener for filtering events.
     */
    private fun setUpSearchViewListener() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                mAdapter?.getFilter()?.filter(queryString)
                return false
            }

            override fun onQueryTextChange(queryString: String?): Boolean {
                mAdapter?.getFilter()?.filter(queryString)
                return false
            }
        })
    }

    /**
     * Displays a popup menu with options for the user.
     */
    private fun showPopupMenu(view: View) {
        val menu = PopupMenu(applicationContext, view)
        menu.menu.add(Menu.NONE, 1, 1, R.string.delete_all)
        menu.show()
        menu.setOnMenuItemClickListener { item ->
            val i = item.itemId
            if (i == 1) {
                deleteAllEvents()
                true
            } else {
                false
            }
        }
    }

    /**
     * Deletes all events by calling the ViewModel.
     */
    private fun deleteAllEvents() {
        mainViewModel.deleteAllEvents()
    }

    /**
     * Handles the click event of the FloatingActionButton to start the EditEventActivity.
     */
    private fun addFabClickHandler() {
        val intent = EditEventActivity.newIntent(this)
        startActivity(intent)
    }
}

