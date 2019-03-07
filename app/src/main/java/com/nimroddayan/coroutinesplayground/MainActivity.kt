package com.nimroddayan.coroutinesplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this, ViewModelFactory).get(MainViewModel::class.java)
        viewModel.dataLoadedEvent.observe(this, Observer { dataset ->
            // Update UI
        })
        viewModel.loadData()
    }
}
