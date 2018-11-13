package com.badoo.mvicoredemo.ui.main.aac.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<Event, Data> : ViewModel(), ObservableSource<Event>, Consumer<Data> {
    private val source = PublishSubject.create<Event>()

    fun onNext(event: Event) {
        source.onNext(event)
    }

    override fun subscribe(observer: Observer<in Event>) {
        source.subscribe(observer)
    }

    abstract override fun accept(data: Data?)
}
