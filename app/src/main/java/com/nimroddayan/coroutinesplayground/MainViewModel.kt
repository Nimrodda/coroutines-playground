package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    private val database: Database,
    private val server: RestApi
) : ViewModel() {
    val dataLoadedEvent: MutableLiveData<List<Tweet>>
        get() = _dataLoadedEvent
    private val _dataLoadedEvent = MutableLiveData<List<Tweet>>()

    private val disposables = CompositeDisposable()

    fun loadData() {
        disposables.add(database.getTweets()
            .flatMap {
                if (it.isEmpty()) {
                    server.getTweets()
                        .flatMap { serverTweets ->
                            database.saveTweets(serverTweets)
                                .onErrorComplete {
                                    // Log error
                                    true
                                }.toSingleDefault(serverTweets)
                        }
                } else {
                    Single.just(it)
                }
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(_dataLoadedEvent::setValue, {/* report error */}))
    }

    override fun onCleared() {
        disposables.clear()
    }
}
