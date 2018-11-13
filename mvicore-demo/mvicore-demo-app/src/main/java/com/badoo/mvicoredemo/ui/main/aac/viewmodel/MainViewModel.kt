package com.badoo.mvicoredemo.ui.main.aac.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.badoo.feature1.Feature1
import com.badoo.feature2.Feature2
import com.badoo.mvicore.binder.Binder
import com.badoo.mvicore.binder.lifecycle.ManualLifecycle
import com.badoo.mvicore.binder.named
import com.badoo.mvicore.binder.using
import com.badoo.mvicoredemo.ui.main.analytics.FakeAnalyticsTracker
import com.badoo.mvicoredemo.ui.main.datamodel.DataModel
import com.badoo.mvicoredemo.ui.main.datamodel.DataModelTransformer
import com.badoo.mvicoredemo.ui.main.event.UiEvent
import com.badoo.mvicoredemo.ui.main.event.UiEventTransformer1
import com.badoo.mvicoredemo.ui.main.event.UiEventTransformer2
import com.badoo.mvicoredemo.ui.main.news.NewsListener
import com.badoo.mvicoredemo.utils.combineLatest

class MainViewModel(
        feature1: Feature1,
        feature2: Feature2,
        analyticsTracker: FakeAnalyticsTracker,
        newsListener: NewsListener
) : BaseViewModel<UiEvent, DataModel>() {

    private val lifeCycle = ManualLifecycle()
    private val binder = Binder(lifeCycle)

    val liveDataModel: MutableLiveData<DataModel> = MutableLiveData()

    init {
        binder.bind(combineLatest(feature1, feature2) to this using DataModelTransformer() named "MainActivity.ViewModels")
        binder.bind(this to feature1 using UiEventTransformer1())
        binder.bind(this to feature2 using UiEventTransformer2())
        binder.bind(this to analyticsTracker named "MainActivity.Analytics")
        binder.bind(feature2.news to newsListener named "MainActivity.News")

        lifeCycle.begin()
    }

    override fun onCleared() = super.onCleared().apply {
        lifeCycle.end()

        binder.dispose()
        binder.clear()
    }

    override fun accept(data: DataModel?) {
        liveDataModel.value = data
    }
}
