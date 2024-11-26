package com.misis.homework2.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import io.realm.RealmResults
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var calendar: Calendar = Calendar.getInstance()

    //0 - day, 1 - month, 2 - calendar, 3 - summary, 4 - notes


//    viewmodel
//    lateinit var realm: Realm

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
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1)
            }
            updateDate()
        }

        binding.previousDateBtn.setOnClickListener {
            if (Constants.SELECTED_TAB == Constants.DAILY){
                calendar.add(Calendar.DATE, -1)
            } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1)
            }
            updateDate()
        }

        binding.floatingActionButton.setOnClickListener {
            AddTransactionFragment()
                .show(supportFragmentManager, null)
        }


//        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
//            @Override
//            public void onTabSelected(TabLayout.Tab tab){
////                Toast.makeText(MainActivity.this, tab.getText().toString(), Toast.LENGTH_SHORT).show();
//                if (tab.getText().equals("Monthly")){
//                    Constants.SELECTED_TAB = 1;
//                    updateDate();
//                } else if (tab.getText().equals("Daily")){
//                    Constants.SELECTED_TAB = 0;
//                    updateDate();
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayoit.Tab tab){
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab){
//
//            }
//        });


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text?.let { tabText ->
                    when (tabText) {
                        "Monthly" -> {
                            Constants.SELECTED_TAB = 1
                            updateDate()
                        }
                        "Daily" -> {
                            Constants.SELECTED_TAB = 0
                            updateDate()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Do nothing
            }
        })









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




//
//
//        realm!!.commitTransaction()



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

            if (transactions?.size ?: 0 > 0) {
                binding.emptyState.visibility = View.GONE
            }
            else{
                binding.emptyState.visibility = View.VISIBLE;
            }
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

    //перенесен в viewmodel
//    fun setupDatabase() {
//        Realm.init(this)
//        realm = Realm.getDefaultInstance()
//    }




    fun getTransactions() {
        viewModel.getTransactions(calendar)
    }



    fun updateDate() {
//        val dateFormat = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.currentDate.text = Helper.formatDate(calendar.time)
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY){
            binding.currentDate.text = Helper.formatDateByMonth(calendar.time)
        }
        viewModel.getTransactions(calendar)
    }

}