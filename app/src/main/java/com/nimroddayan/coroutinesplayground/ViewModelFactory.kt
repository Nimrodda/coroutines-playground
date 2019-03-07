package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val ioDispatcher = Dispatchers.IO
        return MainViewModel(Database(ioDispatcher), RestApi(ioDispatcher)) as T
    }
}
