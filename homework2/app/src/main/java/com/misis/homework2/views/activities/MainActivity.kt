package com.misis.homework2.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.misis.homework2.views.fragments.AddTransactionFragment
import com.misis.homework2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Семейный бюджет"

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment()
                .show(supportFragmentManager, null)
        }
    }
}
