package com.example.onearmedbanditgame

import android.content.Intent
import androidx.activity.ComponentActivity
import android.os.Bundle
import kotlinx.coroutines.delay

class StartScreenActivity :ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for the content view.
        startActivity(Intent(this@StartScreenActivity, MainActivity::class.java))
        finish()

    }

}