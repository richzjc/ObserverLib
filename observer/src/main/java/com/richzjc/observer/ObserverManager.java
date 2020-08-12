package com.richzjc.observer;

import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.Map;

public class ObserverManager {

    final static private HashMap<Integer, ObserverLiveData> liveDatas = new HashMap<>();

    public static void registerObserver(final Observer o, LifecycleOwner lifecycleOwner, final int id) {
        registerObserver(o, lifecycleOwner, id, false);
    }

    public static void registerObserver(final Observer o, LifecycleOwner lifecycleOwner, final int id, boolean isNeedSticky) {
        if (o == null)
            return;

        ObserverLiveData liveData;
        if (liveDatas.containsKey(id)) {
            liveData = liveDatas.get(id);
        } else {
            liveData = new ObserverLiveData();
            liveDatas.put(id, liveData);
        }

        if (lifecycleOwner != null) {
            liveData.observe(lifecycleOwner, new LiveDataObserver(o, id, isNeedSticky));
        } else {
            liveData.observeForever(new LiveDataObserver(o, id, isNeedSticky));
        }
    }

    public static void removeObserver(Observer o, int id) {
        ObserverLiveData liveData = liveDatas.get(id);
        if(liveData != null){
            liveData.removeObserverByO(o);
        }
    }

    public static void removeObserver(LifecycleOwner lifecycleOwner) {
        for (Map.Entry<Integer, ObserverLiveData> entry : liveDatas.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().removeObservers(lifecycleOwner);
        }
    }

    public static void notifyObserver(int id, Object... args) {
        ObserverLiveData liveData = liveDatas.get(id);
        if(liveData != null)
            liveData.setValue(args);
    }
}
