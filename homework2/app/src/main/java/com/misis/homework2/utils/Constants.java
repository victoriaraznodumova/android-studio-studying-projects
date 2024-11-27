package com.misis.homework2.utils;

import com.misis.homework2.R;
import com.misis.homework2.models.Category;

import java.util.ArrayList;

public class Constants {

    public static String INCOME = "INCOME";
    public static String EXPENSE = "EXPENSE";

    public static ArrayList<Category> categories;

    public static int DAILY = 0;
    public static int MONTHLY = 1;
    public static int YEAR = 2;
    public static int SUMMARY = 3;
    public static int NOTES = 4;

    public static int SELECTED_TAB = 0;

    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.statistics, R.color.category1));
        categories.add(new Category("Business", R.drawable.statistics, R.color.category2));
        categories.add(new Category("Investment", R.drawable.statistics, R.color.category3));
        categories.add(new Category("Loan", R.drawable.statistics, R.color.category4));
        categories.add(new Category("Rent", R.drawable.statistics, R.color.category5));
        categories.add(new Category("Other", R.drawable.statistics, R.color.category6));
    }

    public static Category getCategoryDetails(String categoryName){
        for (Category cat :
                categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }


    public static int getAccountsColor(String accountName){
        switch (accountName) {
            case "Т-Банк":
                return R.color.bank_color;
            case "Сбер":
                return R.color.cash_color;
            case "Наличные":
                return R.color.card_color;
            case "Альфа-банк":
                return R.color.card_color;
            default:
                return R.color.default_color;
        }
        //
    }




}