package com.badoo.mvicoredemo.ui.main.aac.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.badoo.feature1.Feature1
import com.badoo.feature2.Feature2
import com.badoo.mvicoredemo.ui.main.analytics.FakeAnalyticsTracker
import com.badoo.mvicoredemo.ui.main.news.NewsListener

class MainViewModelFactory(
        private val feature1: Feature1,
        private val feature2: Feature2,
        private val analyticsTracker: FakeAnalyticsTracker,
        private val newsListener: NewsListener
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
                feature1,
                feature2,
                analyticsTracker,
                newsListener
        ) as T
    }
}
