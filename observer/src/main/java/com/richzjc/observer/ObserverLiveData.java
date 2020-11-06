package com.richzjc.observer;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Field;
import java.util.Map;

class ObserverLiveData extends MutableLiveData<Object[]> {

    private static Field mObserversField;
    private boolean isIgnoreLastMsg = true;

    private Iterable<Map.Entry> iterable;
    private Handler handler;

    public ObserverLiveData(boolean isIgnoreLastMsg) {
        try {
            this.isIgnoreLastMsg = isIgnoreLastMsg;
            if (!isIgnoreLastMsg) {
                handler = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        ObserverLiveData.super.setValue((Object[]) msg.obj);
                    }
                };
            }

            if (getmObserversField() != null)
                iterable = (Iterable) mObserversField.get(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getmObserversField() {
        if (mObserversField == null) {
            try {
                mObserversField = LiveData.class.getDeclaredField("mObservers");
                if (mObserversField != null) {
                    mObserversField.setAccessible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mObserversField;
    }

    public void removeObserverByO(Observer o) {
        if (o instanceof LifecycleOwner) {
            removeObservers((LifecycleOwner) o);
        } else {
            if (iterable != null) {
                LiveDataObserver liveDataObserver;
                for (Map.Entry entry : iterable) {
                    try {
                        liveDataObserver = (LiveDataObserver) entry.getKey();
                        if (liveDataObserver != null && liveDataObserver.observer == o) {
                            removeObserver(liveDataObserver);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull androidx.lifecycle.Observer<? super Object[]> observer) {
        if (observer instanceof LiveDataObserver) {
            ((LiveDataObserver) observer).filterObject = getValue();
        }
        super.observe(owner, observer);
    }


    @Override
    public void observeForever(@NonNull androidx.lifecycle.Observer<? super Object[]> observer) {
        if (observer instanceof LiveDataObserver) {
            ((LiveDataObserver) observer).filterObject = getValue();
        }
        super.observeForever(observer);
    }

    @Override
    public void setValue(Object[] value) {
        if (isIgnoreLastMsg)
            super.setValue(value);
        else {
            Message msg = Message.obtain();
            msg.obj = value;
            handler.sendMessage(msg);
        }
    }
}
