package com.nimroddayan.coroutinesplayground

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val database: Database,
    private val server: RestApi
) : ViewModel() {

    init {
        loadData()
    }

    val dataLoadedEvent: MutableLiveData<List<Tweet>>
        get() = _dataLoadedEvent
    private val _dataLoadedEvent = MutableLiveData<List<Tweet>>()

    fun loadData() {
        val job = viewModelScope.launch {
            Log.d("MainViewModel", "launch()" + Thread.currentThread().name)
            try {
                delay(10000L)
            } catch (e: Exception) {
                //if (e is CancellationException) throw e
                Log.d("MainViewModel", "Exception $e caught" + Thread.currentThread().name)
            }

            shit()
        }

        viewModelScope.launch {
            delay(2000L)
            job.cancel()
            Log.d("MainViewModel", "${job.isActive}" + Thread.currentThread().name)
            Log.d("MainViewModel", "${viewModelScope.coroutineContext.isActive}")
        }
    }

    suspend fun shit() {
        Log.d("MainViewModel", "after exception")
    }
}
