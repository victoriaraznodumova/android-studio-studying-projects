package com.misis.homework2.views.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.misis.homework2.adapters.TransactionsAdapter
import com.misis.homework2.databinding.ActivityMainBinding
import com.misis.homework2.models.Transaction
import com.misis.homework2.utils.Constants
import com.misis.homework2.utils.Helper
import com.misis.homework2.viewmodels.MainViewModel
import com.misis.homework2.views.fragments.AddTransactionFragment
import io.realm.Realm
import android.view.MenuItem;
import io.realm.RealmResults
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var calendar: Calendar = Calendar.getInstance()

//    viewmodel
//    lateinit var realm: Realm

    var selectedTab: Int = 0

    public lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //    viewmodel
//      setupDatabase()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        setSupportActionBar(binding.toolBar)
        supportActionBar?.title = "Семейный бюджет"

        Constants.setCategories();
        calendar = Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, 1)
            }
            else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1)
            }
            else if (Constants.SELECTED_TAB == Constants.YEAR){
                calendar.add(Calendar.YEAR, 1)
            }

            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, -1)
            }
            else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1)
            }
            else if (Constants.SELECTED_TAB == Constants.YEAR){
                calendar.add(Calendar.YEAR, -1)
            }

            updateDate()
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment()
                .show(supportFragmentManager, null)
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text?.let { tabText ->
                    when (tabText) {
                        "Месяц" -> {
                            Constants.SELECTED_TAB = 1
                            updateDate()
                        }

                        "День" -> {
                            Constants.SELECTED_TAB = 0
                            updateDate()
                        }

                        "Год" -> {
                            Constants.SELECTED_TAB = 2
                            updateDate()
                        }
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Реализация не нужна, оставьте пустым
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Реализация не нужна, оставьте пустым
            }
        });


//        val transactions = arrayListOf<Transaction>()
//        transactions.add(Transaction(Constants.INCOME, "Business", "Cash", "Some note", Date(), 500.0, 2));
//        transactions.add(Transaction(Constants.EXPENSE, "Business", "Cash", "Some note", Date(), 50.0, 2));





//        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>(){
//            @Override
//            public void onChanged(RealmResults<Transaction> transactions){
//
//            }
//        })

//        binding.transactionsList.layoutManager = LinearLayoutManager(this)

//        val transactions = RealmResults<Transaction>()
//        ArrayList<Transaction> transactions = new ArrayList<>();








//        RealmResults<Transaction> transactions = realm.where(Transaction.class).findAll();


        //    viewmodel
// val transactions: RealmResults<Transaction> = realm.where(Transaction::class.java).findAll()





//        TransactionsAdapter transactionsAdapter = new TransactionsAdapter(this, transactions);
//        binding.transactionsList.layoutManager(new LinearLayoutManager(this));
//        binding.transactionsList.setAdapter(transactionsAdapter);

        binding.transactionsList.layoutManager = LinearLayoutManager(this)
        viewModel.transactions.observe(this) { transactions: RealmResults<Transaction>? ->
            val transactionsAdapter = TransactionsAdapter(this@MainActivity, transactions)
            binding.transactionsList.adapter = transactionsAdapter
//            делает невидимым весь экран
//            if (transactions?.size ?: 0 != 0){
//                binding.emptyState.visibility = View.GONE;
//            } else{
//                binding.emptyState.visibility = View.VISIBLE;
//            }

        }

        viewModel.totalIncome.observe(this) { income ->
            binding.incomeLbl.text = income.toString()
        }

        viewModel.totalExpense.observe(this) { expense ->
            binding.expenseLbl.text = expense.toString()
        }

        viewModel.totalAmount.observe(this) { amount ->
            binding.totalLbl.text = amount.toString()
        }

        viewModel.getTransactions(calendar)







//
//        viewModel.transactions.observe(this) { transactions ->
//            val transactionsAdapter = TransactionsAdapter(this@MainActivity, transactions)
//            binding.transactionsList.adapter = transactionsAdapter
//
//        }
//
//        viewModel.getTransactions()






    }


    fun getTransactions() {
        viewModel.getTransactions(calendar)
    }

    //перенесен в viewmodel
//    fun setupDatabase() {
//        Realm.init(this)
//        realm = Realm.getDefaultInstance()
//    }



    fun updateDate() {
//        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        if (Constants.SELECTED_TAB == Constants.DAILY){
            binding.currentDate.text = Helper.formatDate(calendar.time)
        }
        else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        else if(Constants.SELECTED_TAB == Constants.YEAR){
            binding.currentDate.text = Helper.formatDateByYear(calendar.time)
        }

        viewModel.getTransactions(calendar)
    }

}