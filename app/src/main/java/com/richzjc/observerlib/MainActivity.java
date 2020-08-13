package com.richzjc.observerlib;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.richzjc.observer.Observer;
import com.richzjc.observer.ObserverManger;

public class MainActivity extends AppCompatActivity implements Observer {

    MainPresenter presenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObserverManger.registerObserver(this, 0);
        initListener();
        presenter.attachViewRef(this);
    }

    private void initListener(){
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                presenter.detachViewRef();
                ObserverManger.notifyObserver(0, "今天的天气还是不错的");
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    public void update(int id, Object... args) {
        TextView tv = findViewById(R.id.tv);
        tv.setText(args[0].toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachViewRef();
    }
}
