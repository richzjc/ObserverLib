package com.richzjc.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.lang.reflect.Field;
import java.util.Map;

class ObserverLiveData extends MutableLiveData<Object[]> {

    private static Field mObserversField;

    private Iterable<Map.Entry> iterable;
    public ObserverLiveData(){
        try {
            if(getmObserversField() != null)
                iterable = (Iterable) mObserversField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getmObserversField(){
        if(mObserversField == null){
            try {
                mObserversField = LiveData.class.getDeclaredField("mObservers");
                if(mObserversField != null){
                 mObserversField.setAccessible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mObserversField;
    }

    public void removeObserverByO(Observer o) {
        if(iterable != null){
            LiveDataObserver liveDataObserver;
            for (Map.Entry entry : iterable) {
                try {
                    liveDataObserver = (LiveDataObserver) entry.getKey();
                    if(liveDataObserver != null && liveDataObserver.observer == o){
                        removeObserver(liveDataObserver);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull androidx.lifecycle.Observer<? super Object[]> observer) {
        if(observer instanceof LiveDataObserver){
            ((LiveDataObserver) observer).filterObject = getValue();
        }
        super.observe(owner, observer);
    }


    @Override
    public void observeForever(@NonNull androidx.lifecycle.Observer<? super Object[]> observer) {
        if(observer instanceof LiveDataObserver){
            ((LiveDataObserver) observer).filterObject = getValue();
        }
        super.observeForever(observer);
    }
}
