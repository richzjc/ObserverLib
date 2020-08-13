package com.richzjc.observerlib;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by zhangjianchuan on 2016/6/27.
 */
public class BasePresenter<T extends Object> implements LifecycleOwner {
    protected Reference<T> mViewRef;
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);

    public void attachViewRef(T view) {
        if (mViewRef == null) {
            mViewRef = new WeakReference<>(view);
        }

        ((LifecycleRegistry)getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    public T getViewRef() {
        if (null != mViewRef) {
            return mViewRef.get();
        }
        return null;
    }

    public boolean isViewRefAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachViewRef() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }

        ((LifecycleRegistry)getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
