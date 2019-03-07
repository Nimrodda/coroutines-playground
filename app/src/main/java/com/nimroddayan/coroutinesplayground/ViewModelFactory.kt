package com.nimroddayan.coroutinesplayground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.Executors

object ViewModelFactory : ViewModelProvider.Factory {
    private val ioExecutor = Executors.newFixedThreadPool(3)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(Database(ioExecutor), RestApi(ioExecutor)) as T
    }
}
