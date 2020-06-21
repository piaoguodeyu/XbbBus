package com.jdsbus.baseobservable;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.jdsbus.interfacs.Observer;
import com.jdsbus.interfacs.Register;
import com.jdsbus.interfacs.SchedulerType;
import com.jdsbus.interfacs.Subscriber;
import com.jdsbus.lifecycler.JdsBusLifeCycler;
import com.jdsbus.lifecycler.LifeObserver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author zhangxiaowei 2020/6/21
 */
public class BaseObservable implements Register, LifeObserver, Subscriber {
    JdsBusLifeCycler jdsBusLifeCycler;
    LifecycleOwner mLifecycleOwner;

    public BaseObservable(JdsBusLifeCycler lifeCycler, LifecycleOwner lifecycleOwner) {
        jdsBusLifeCycler = lifeCycler;
        mLifecycleOwner = lifecycleOwner;
        if (mLifecycleOwner != null)
            mLifecycleOwner.getLifecycle().addObserver(this);
    }


    @Override
    public <D> Subscriber<D> register(Class<D> subscriber) {
        return this;
    }

    @Override
    public void register(Object subscriber) {

    }

    @Override
    public void subscriber(Observer subscriber) {
        Type genType1 = subscriber.getClass().getGenericSuperclass();

        Class templatClazz1 = null;
        if (ParameterizedType.class.isInstance(genType1)) {
            //无法获取到User类，或者可能获取到错误的类型，如果有同名且不带包名的泛型存在
            ParameterizedType parameterizedType1 = (ParameterizedType) genType1;
            templatClazz1 = (Class) parameterizedType1.getActualTypeArguments()[0];
        }
        Field[] fields = subscriber.getClass().getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
//            if (BaseJdsPresenter.class.isAssignableFrom(field.getType())) {
//                field.setAccessible(true);
//                try {
//                    mPresent = (BaseJdsPresenter) field.get(this);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
        }
    }

    @Override
    public Subscriber observeOn(SchedulerType schedulerType) {
        return null;
    }


    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {
        if (jdsBusLifeCycler == JdsBusLifeCycler.onPause) {
            if (mLifecycleOwner != null)
                mLifecycleOwner.getLifecycle().removeObserver(this);
        }
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {

    }

    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {

    }
}
