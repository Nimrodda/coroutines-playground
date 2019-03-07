package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val database: Database,
    private val server: RestApi
) : ViewModel() {
    val dataLoadedEvent: MutableLiveData<List<Tweet>>
        get() = _dataLoadedEvent
    private val _dataLoadedEvent = MutableLiveData<List<Tweet>>()

    fun loadData() {
        database.getTweets { isSuccess, error, tweets ->
            if (isSuccess) {
                if (tweets.isEmpty()) {
                    server.getTweets { isSuccess, error, tweets ->
                        if (isSuccess) {
                            if (tweets.isEmpty()) {
                                // Report no tweets available
                                _dataLoadedEvent.value = emptyList()
                            } else {
                                database.saveTweets(tweets) { isSuccess, error ->
                                    if (isSuccess) {
                                        // Successfully persisted locally - Happiness :D
                                    } else {
                                        // log error
                                    }
                                }
                                _dataLoadedEvent.value = tweets
                            }
                        } else {
                            // report error
                        }
                    }
                } else {
                    _dataLoadedEvent.value = tweets
                }
            } else {
                // report error
            }
        }
    }

    override fun onCleared() {
        database.close()
        server.close()
    }
}
