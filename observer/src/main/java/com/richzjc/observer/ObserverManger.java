package com.richzjc.observer;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.Map;

public class ObserverManger {

    final private HashMap<Integer, ObserverLiveData> liveDatas = new HashMap<>();
    final private Handler handler = new Handler(Looper.getMainLooper());


    private static volatile ObserverManger Instance = null;

    public static ObserverManger getInstance() {
        ObserverManger localInstance = Instance;
        if (localInstance == null) {
            synchronized (ObserverManger.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new ObserverManger();
                }
            }
        }
        return localInstance;
    }


    public void registerObserver(final Observer o, final int... ids) {
        registerObserver(o, false, ids);
    }

    public void registerObserver(final Observer o, final boolean isNeedSticky, final int... ids) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    realObserver(o, isNeedSticky, true, ids);
                }
            });
        } else {
            realObserver(o, isNeedSticky, true, ids);
        }
    }


    public void registerObserver(final Observer o, final boolean isNeedSticky, final boolean isIgnoreLastMsg, final int... ids) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    realObserver(o, isNeedSticky, isIgnoreLastMsg, ids);
                }
            });
        } else {
            realObserver(o, isNeedSticky, isIgnoreLastMsg, ids);
        }
    }


    private void realObserver(Observer o, boolean isNeedSticky, boolean isIgnoreLastMsg,  int[] ids) {
        if (o == null)
            return;

        for (int id : ids) {
            ObserverLiveData liveData;
            if (liveDatas.containsKey(id)) {
                liveData = liveDatas.get(id);
            } else {
                liveData = new ObserverLiveData(isIgnoreLastMsg);
                liveDatas.put(id, liveData);
            }

            if (o instanceof LifecycleOwner) {
                liveData.observe((LifecycleOwner) o, new LiveDataObserver(o, id, isNeedSticky));
            } else {
                liveData.observeForever(new LiveDataObserver(o, id, isNeedSticky));
            }
        }
    }

    public void removeObserver(Observer o, int... ids) {
        if (ids == null || ids.length == 0) {
            for (Map.Entry<Integer, ObserverLiveData> entry : liveDatas.entrySet()) {
                if (entry.getValue() != null)
                    entry.getValue().removeObserverByO(o);
            }
        } else {
            for (int id : ids) {
                ObserverLiveData liveData = liveDatas.get(id);
                if (liveData != null) {
                    liveData.removeObserverByO(o);
                }
            }
        }
    }

    public void notifyObserver(int id, Object... args) {
        ObserverLiveData liveData = liveDatas.get(id);
        if (liveData != null)
            liveData.setValue(args);
    }
}
