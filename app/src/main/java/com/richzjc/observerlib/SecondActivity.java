package com.richzjc.observerlib;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;

public class SecondActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObserverManger.registerObserver(this, false, 0);
        initListener();
    }

    private void initListener(){
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObserverManger.notifyObserver(0, "SecondActivity");
            }
        });
    }

    @Override
    public void update(int id, Object... args) {
        TextView tv = findViewById(R.id.tv);
        tv.setText(args[0].toString());
    }
}
