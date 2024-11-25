package com.misis.homework2.views.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.misis.homework2.adapters.TransactionsAdapter
import com.misis.homework2.views.fragments.AddTransactionFragment
import com.misis.homework2.databinding.ActivityMainBinding
import com.misis.homework2.models.Transaction
import com.misis.homework2.utils.Constants
import com.misis.homework2.utils.Helper
import com.misis.homework2.viewmodels.MainViewModel
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var calendar: Calendar = Calendar.getInstance()
    lateinit var realm: Realm

    lateinit var viewmodel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: MainViewModel by viewModels()

        setupDatabase()

        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Семейный бюджет"

        Constants.setCategories();
        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            updateDate()
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment()
                .show(supportFragmentManager, null)
        }

//        val transactions = arrayListOf<Transaction>()
//        transactions.add(Transaction(Constants.INCOME, "Business", "Cash", "Some note", Date(), 500.0, 2));
//        transactions.add(Transaction(Constants.EXPENSE, "Business", "Cash", "Some note", Date(), 50.0, 2));


        realm.beginTransaction()
        realm.copyToRealmOrUpdate(Transaction(Constants.INCOME, "Business", "Cash", "Some note", Date(), 500.0, System.currentTimeMillis()))
        realm.copyToRealmOrUpdate(Transaction(Constants.EXPENSE, "Business", "Cash", "Some note", Date(), 50.0, System.currentTimeMillis()))


        realm.commitTransaction()

//      select * from transactions


        val transactions = realm.where(Transaction::class.java).findAll()


        val transactionsAdapter = TransactionsAdapter(this, transactions)
        binding.transactionsList.layoutManager = LinearLayoutManager(this)
        binding.transactionsList.adapter = transactionsAdapter




    }

//    fun setupDatabase() {
//        Realm.init(this)
//        realm = Realm.getDefaultInstance()
//    }



    fun updateDate() {
//        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        binding.currentDate.text = Helper.formatDate(calendar.time)
    }

}
