package ru.gb.kotlin.themoviedbapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.kotlin.themoviedbapp.ui.view.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}