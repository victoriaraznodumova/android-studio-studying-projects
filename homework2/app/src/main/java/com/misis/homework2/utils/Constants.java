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

    public static int SELECTED_TAB = 0;

    public static void setCategories() {
        categories = new ArrayList<>();
        categories.add(new Category("Заработная плата", R.drawable.ic_salary, R.color.category1));
        categories.add(new Category("Премия", R.drawable.prize, R.color.category2));
        categories.add(new Category("Подработка", R.drawable.ic_business, R.color.category3));
        categories.add(new Category("Донат", R.drawable.heart, R.color.category4));
        categories.add(new Category("Кредит", R.drawable.ic_loan, R.color.category5));
        categories.add(new Category("Прочее", R.drawable.ic_other, R.color.category6));
        categories.add(new Category("Продукты", R.drawable.fast_food, R.color.category7));
        categories.add(new Category("Транспорт", R.drawable.limousine, R.color.category8));
        categories.add(new Category("Онлайн-сервисы", R.drawable.setting, R.color.category9));
        categories.add(new Category("Кредиты", R.drawable.credit, R.color.category10));
        categories.add(new Category("Обучение", R.drawable.graduation, R.color.category11));
        categories.add(new Category("Ремонт", R.drawable.reparatur, R.color.category12));
        categories.add(new Category("Отпуск", R.drawable.sun_umbrella, R.color.category13));
        categories.add(new Category("Хобби, развлечения", R.drawable.unterhaltungen, R.color.category14));
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