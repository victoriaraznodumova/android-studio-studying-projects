package com.misis.homework2.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.misis.homework2.views.fragments.AddTransactionFragment
import com.misis.homework2.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var calendar: Calendar = Calendar.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Семейный бюджет"


        calendar = Calendar.getInstance();
        updateDate();

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment()
                .show(supportFragmentManager, null)
        }
    }

    fun updateDate() {
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        binding.currentDate.text = dateFormat.format(calendar.time)
    }

}
