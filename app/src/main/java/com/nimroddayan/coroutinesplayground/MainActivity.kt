package com.nimroddayan.coroutinesplayground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Scene
import android.transition.TransitionManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.addListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    private lateinit var start: ConstraintSet
    private lateinit var transition: AutoTransition
    private lateinit var end: ConstraintSet
    private lateinit var container: ConstraintLayout
    private var reverse = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = findViewById(R.id.container)
        start = ConstraintSet()
        start.clone(container)
        end = ConstraintSet()
        end.clone(this, R.layout.activity_main_end)
        transition = AutoTransition()
        transition.duration = 3000L
        transition.addListener(onEnd = {
            replay()
        })
        container.setOnClickListener {
            replay()
        }
        val viewModel = ViewModelProviders.of(this, ViewModelFactory).get(MainViewModel::class.java)
        viewModel.dataLoadedEvent.observe(this, Observer { dataset ->
            // Update UI
        })
        viewModel.loadData()
    }

    private fun replay() {
        val constraintSet = if (reverse) start else end
        TransitionManager.beginDelayedTransition(container, transition)
        constraintSet.applyTo(container)
        reverse = !reverse
    }

}
