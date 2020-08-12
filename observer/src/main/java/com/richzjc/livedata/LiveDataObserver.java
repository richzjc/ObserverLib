package com.richzjc.livedata;

import androidx.lifecycle.Observer;

public class LiveDataObserver implements Observer<Object[]> {

    public com.richzjc.livedata.Observer observer;
    public int id;
    public boolean isNeedSticky;
    public Object[] filterObject;

    public LiveDataObserver(com.richzjc.livedata.Observer observer, int id, boolean isNeedSticky){
        this.observer = observer;
        this.id = id;
        this.isNeedSticky = isNeedSticky;
    }

    @Override
    public void onChanged(Object[] objects) {
        if(isNeedSticky) {
            if (observer != null)
                observer.update(id, objects);
            filterObject = null;
        }else if(objects != filterObject){
            //判断数据是否有变化
            if (observer != null)
                observer.update(id, objects);
            filterObject = null;
        }
    }
}
