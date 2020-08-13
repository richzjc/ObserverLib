package com.richzjc.observer;

import androidx.lifecycle.LifecycleOwner;
import java.util.HashMap;
import java.util.Map;

public class ObserverManger {

    final static private HashMap<Integer, ObserverLiveData> liveDatas = new HashMap<>();

    public static void registerObserver(final Observer o, final int ... ids) {
        registerObserver(o, false, ids);
    }

    public static void registerObserver(final Observer o, boolean isNeedSticky, final int ... ids) {
        if (o == null)
            return;

        for(int id : ids){
            ObserverLiveData liveData;
            if (liveDatas.containsKey(id)) {
                liveData = liveDatas.get(id);
            } else {
                liveData = new ObserverLiveData();
                liveDatas.put(id, liveData);
            }

            if (o instanceof LifecycleOwner) {
                liveData.observe((LifecycleOwner) o, new LiveDataObserver(o, id, isNeedSticky));
            } else {
                liveData.observeForever(new LiveDataObserver(o, id, isNeedSticky));
            }
        }
    }

    public static void removeObserver(Observer o, int ... ids) {
        if(ids == null || ids.length == 0){
            for (Map.Entry<Integer, ObserverLiveData> entry : liveDatas.entrySet()) {
                if (entry.getValue() != null)
                    entry.getValue().removeObserverByO(o);
            }
        }else{
            for(int id : ids){
                ObserverLiveData liveData = liveDatas.get(id);
                if(liveData != null){
                    liveData.removeObserverByO(o);
                }
            }
        }
    }

    public static void notifyObserver(int id, Object... args) {
        ObserverLiveData liveData = liveDatas.get(id);
        if(liveData != null)
            liveData.setValue(args);
    }
}
