package com.richzjc.observerlib;

import android.util.Log;

import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;

class MainPresenter extends BasePresenter<Object> implements Observer {

    public MainPresenter() {
        ObserverManger.getInstance().registerObserver(this, 0);
    }

    @Override
    public void update(int id, Object... args) {
        Log.i("MainPresenter", "今天的天气还是错的哟");
    }
}
