package com.foogui.bootevent.template.listener;

import com.foogui.bootevent.template.event.BaseEvent;
import org.springframework.context.ApplicationListener;

public interface IEventListener<T extends BaseEvent> extends ApplicationListener<T> {

    default void onApplicationEvent(T event){
        try {
            if (isNotified(event)){
                handleEvent(event);
            }
        } catch (Exception e) {
           handleException(e);
        }
    }
    //定义钩子函数,默认执行监听逻辑
    default boolean isNotified(T event) {
        return true;
    }

    /**
     * 真正实现业务逻辑的接口，给子类去实现。
     * 模板方法模式
     * @param event 事件
     */
    void handleEvent(T event);

    /**
     * 异常默认不处理,子类可以选择性实现
     * @param exception
     */
    default void handleException(Throwable exception) {
    }
}
