package com.jdsbus.baseobservable;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Looper;

import com.jdsbus.JdsBus;
import com.jdsbus.interfacs.Observer;
import com.jdsbus.interfacs.PostMessage;
import com.jdsbus.interfacs.Register;
import com.jdsbus.interfacs.SchedulerType;
import com.jdsbus.interfacs.Subscriber;
import com.jdsbus.lifecycler.JdsBusLifeCycler;
import com.jdsbus.lifecycler.LifeObserver;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangxiaowei 2020/6/21
 */
public class BaseObservable implements Register, LifeObserver, Subscriber, PostMessage {
    JdsBusLifeCycler jdsBusLifeCycler;
    LifecycleOwner mLifecycleOwner;
    /**
     * 回调线程
     */
    SchedulerType schedulerType;
    Map<Class, Object> mapObservable;
    Class registerClass;

    public BaseObservable(JdsBusLifeCycler lifeCycler, LifecycleOwner lifecycleOwner) {
        jdsBusLifeCycler = lifeCycler;
        mLifecycleOwner = lifecycleOwner;
        if (mLifecycleOwner != null)
            mLifecycleOwner.getLifecycle().addObserver(this);
        mapObservable = new ConcurrentHashMap<>();
    }


    @Override
    public <D> Subscriber<D> register(Class<D> subscriber) {
        registerClass = subscriber;
        mapObservable.put(registerClass, registerClass);
        return this;
    }

//    @Override
//    public Subscriber register(Class subscriber) {
//        return null;
//    }

    @Override
    public void register(Object subscriber) {

    }

    @Override
    public void subscriber(Observer subscriber) {
        mapObservable.put(registerClass, subscriber);
//        Type genType1 = subscriber.getClass().getGenericSuperclass();
//        Class templatClazz1 = null;
//        if (ParameterizedType.class.isInstance(genType1)) {
//            //无法获取到User类，或者可能获取到错误的类型，如果有同名且不带包名的泛型存在
//            ParameterizedType parameterizedType1 = (ParameterizedType) genType1;
//            templatClazz1 = (Class) parameterizedType1.getActualTypeArguments()[0];
//        }
//        Field[] fields = subscriber.getClass().getDeclaredFields();
//        for (Field field : fields) {
//            System.out.println(field.getName());
////            if (BaseJdsPresenter.class.isAssignableFrom(field.getType())) {
////                field.setAccessible(true);
////                try {
////                    mPresent = (BaseJdsPresenter) field.get(this);
////                } catch (IllegalAccessException e) {
////                    e.printStackTrace();
////                }
////                break;
////            }
//        }
    }

    @Override
    public Subscriber observeOn(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
        return this;
    }


    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {
        if (jdsBusLifeCycler == JdsBusLifeCycler.onPause) {
            if (mLifecycleOwner != null)
                mLifecycleOwner.getLifecycle().removeObserver(this);
            mapObservable.clear();//此时表示生命周期结束，不再使用，清空数据
        }
    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        if (jdsBusLifeCycler == JdsBusLifeCycler.onDestroy) {
            if (mLifecycleOwner != null)
                mLifecycleOwner.getLifecycle().removeObserver(this);
            mapObservable.clear();//此时表示生命周期结束，不再使用，清空数据
        }
    }

    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void post(Object data) {
        Object value = mapObservable.get(data.getClass());
        if (value != null) {
            if (schedulerType == SchedulerType.main) {
                if (Thread.currentThread() == Looper.getMainLooper().getThread()) {//在主线程接收，并且当前线程也是主线程
                    if (value instanceof Observer) {//如果是Observer方式订阅
                        ((Observer) value).handMessage(value);
                    } else {

                    }
                } else {//当前线程不是主线程，但是接收在主线程

                }
            } else {//在IO线程接收该数据

            }
        }
    }

    @Override
    public void post(Object data, Class<?> toReceive) {

    }
}
