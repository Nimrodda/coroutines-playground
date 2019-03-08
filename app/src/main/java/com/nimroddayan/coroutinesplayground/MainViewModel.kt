package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val database: Database,
    private val server: RestApi
) : ViewModel(), CoroutineScope {

    val dataLoadedEvent: MutableLiveData<List<Tweet>>
        get() = _dataLoadedEvent
    private val _dataLoadedEvent = MutableLiveData<List<Tweet>>()

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun loadData() {
        launch {
            val localTweets = database.getTweets()
            // One way to handle errors is to wrap the data in a Result
            // sealed class which will contain the data or error
            if (localTweets is Result.Success) {
                _dataLoadedEvent.value = localTweets.data
            } else {
                val serverTweets = server.getTweets()
                if (serverTweets is Result.Success) {
                    try {
                        // Another way to handle errors
                        // this time with try-catch
                        database.saveTweets(serverTweets.data)
                    } catch (ex: Exception) {
                        // Error while persisting locally
                    }
                    _dataLoadedEvent.value = serverTweets.data
                } else {
                    // Failed to fetch from server
                    // Report no tweets to user
                    _dataLoadedEvent.value = emptyList()
                }
            }
        }
    }

    fun loadDataParallel() = launch {
        // Here I demonstrate how to run coroutines in parallel
        // Both database.getTweets() and server.getTweets() will run
        // concurrently unlike the example above in loadData()
        // where they're run sequentially.
        val localTweets = async { database.getTweets() }
        val serverTweets = async { server.getTweets() }

        // One way to handle errors is to wrap the data in a Result
        // sealed class which will contain the data or error
        val tweets = mutableListOf<Tweet>()
        tweets += (localTweets.await() as? Result.Success)?.data ?: emptyList()
        tweets += (serverTweets.await() as? Result.Success)?.data ?: emptyList()
        _dataLoadedEvent.value = tweets
    }
}

override fun onCleared() {
    job.cancel()
}
}
