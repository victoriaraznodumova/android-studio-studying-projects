package com.misis.homework2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.misis.homework2.R;
import com.misis.homework2.databinding.RowAccountBinding;
import com.misis.homework2.databinding.RowTransactionsBinding;
import com.misis.homework2.models.Category;
import com.misis.homework2.models.Transaction;
import com.misis.homework2.utils.Constants;
import com.misis.homework2.utils.Helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>{


    Context context;
    RealmResults<Transaction> transactions;

    public TransactionsAdapter(Context context, RealmResults<Transaction> transactions){
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.row_transactions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.binding.transactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.accountLbl.setText(transaction.getAccount());

        holder.binding.transactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.transactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategoryDetails(transaction.getCategory());

//        assert transactionCategory != null;

        if (transactionCategory != null) {
            holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        } else {
            // Установите изображение по умолчанию или оставьте пустым
            holder.binding.categoryIcon.setImageResource(R.drawable.statistics);
        }
//        holder.binding.categoryIcon.setImageResource(transactionCategory.getCategoryImage());
        if (transactionCategory != null) {
            holder.binding.categoryIcon.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));
        }
        else{
            holder.binding.categoryIcon.setImageResource(R.color.black);
        }

        holder.binding.accountLbl.setBackgroundTintList(context.getColorStateList(Constants.getAccountsColor(transaction.getAccount())));

        if (transaction.getType().equals(Constants.INCOME)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.greenColor));
        } else if (transaction.getType().equals(Constants.EXPENSE)){
            holder.binding.transactionAmount.setTextColor(context.getColor(R.color.redColor));
        }

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{

    RowTransactionsBinding binding;

    public TransactionViewHolder(@NonNull View itemView){
        super(itemView);
        binding = RowTransactionsBinding.bind(itemView);


    }




    }




}
