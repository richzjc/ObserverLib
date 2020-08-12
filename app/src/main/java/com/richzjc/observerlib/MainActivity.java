package com.richzjc.observerlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.richzjc.livedata.Observer;
import com.richzjc.livedata.ObserverManager;

public class MainActivity extends AppCompatActivity implements Observer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObserverManager.registerObserver(this, this, 0);
        initListener();
    }

    private void initListener(){
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObserverManager.notifyObserver(0, "今天的天气还是不错的");
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    public void update(int id, Object... args) {
        TextView tv = findViewById(R.id.tv);
        tv.setText(args[0].toString());
    }
}
