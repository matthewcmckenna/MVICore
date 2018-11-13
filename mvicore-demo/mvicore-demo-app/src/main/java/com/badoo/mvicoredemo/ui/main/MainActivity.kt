package com.badoo.mvicoredemo.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View
import com.badoo.mvicoredemo.R
import com.badoo.mvicoredemo.auth.logout
import com.badoo.mvicoredemo.glide.GlideApp
import com.badoo.mvicoredemo.ui.common.DebugActivity
import com.badoo.mvicoredemo.ui.main.aac.viewmodel.MainViewModel
import com.badoo.mvicoredemo.ui.main.aac.viewmodel.MainViewModelFactory
import com.badoo.mvicoredemo.ui.main.analytics.FakeAnalyticsTracker
import com.badoo.mvicoredemo.ui.main.datamodel.DataModel
import com.badoo.mvicoredemo.ui.main.di.component.MainScreenInjector
import com.badoo.mvicoredemo.ui.main.event.UiEvent.ButtonClicked
import com.badoo.mvicoredemo.ui.main.event.UiEvent.ImageClicked
import com.badoo.mvicoredemo.ui.main.event.UiEvent.PlusClicked
import init
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : DebugActivity() {

    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    @Inject
    lateinit var analyticsTracker: FakeAnalyticsTracker
    private lateinit var buttons: List<View>

    private val viewModel: MainViewModel by lazy(NONE) {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScreenInjector.get(this).inject(this)
        setContentView(R.layout.activity_main)
        setupViews()
        setupDebugDrawer()
        observeUpdates()
    }

    private fun observeUpdates() {
        viewModel.liveDataModel.observe(this, Observer<DataModel> { dataModel ->
            counter.text = dataModel?.counter.toString()

            buttons.forEachIndexed { idx, button ->
                dataModel?.buttonColors?.get(idx)
                        ?.let { ContextCompat.getColor(this, it) }
                        ?.let { button.setBackgroundColor(it) }
            }

            imageProgress.visibility = if (dataModel?.imageIsLoading == true) View.VISIBLE else View.GONE
            loadImage(dataModel?.imageUrl)
        })
    }

    private fun setupViews() {
        buttons = listOf(button0, button1, button2, button3)
        buttons.forEachIndexed { idx, button -> button.setOnClickListener { viewModel.onNext(ButtonClicked(idx)) } }
        image.setOnClickListener { viewModel.onNext(ImageClicked) }
        fab.setOnClickListener { viewModel.onNext(PlusClicked) }
        signOut.setOnClickListener { logout() }
        showToasts.setOnClickListener {
            // Only for debugging purposes, otherwise should be part of the state!
            analyticsTracker.showToasts = !analyticsTracker.showToasts
            showToasts.toggle(analyticsTracker.showToasts)
        }

        help.setOnClickListener {
            HelpDialogFragment().show(supportFragmentManager, "help")
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.init(drawerLayout, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when (item.itemId) {
                android.R.id.home -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    private fun loadImage(url: String?) {
        if (url != null) {
            GlideApp.with(this)
                    .load(url)
                    .centerCrop()
                    .into(image)
        }
    }
}
