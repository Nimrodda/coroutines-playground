package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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

    override fun onCleared() {
        job.cancel()
    }
}
