package com.misis.homework2.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.misis.homework2.models.Transaction;


import io.realm.Realm;
import io.realm.RealmResults;

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData<RealmResults<Transaction>> transactions = new MutableLiveData<>();



    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(this);
    }


    void setupDatabase() {

        realm = Realm.getDefaultInstance();
    }

}
