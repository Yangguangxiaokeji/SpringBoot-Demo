package com.foogui.bootevent.template.publish;

import com.foogui.bootevent.template.config.ProjectExecutorPool;
import com.foogui.bootevent.template.event.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class EventPublish {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 同步阻塞发布事件
     * @param baseEvent 基础事件
     */
    public void syncPublish(BaseEvent baseEvent){

        applicationContext.publishEvent(baseEvent);
    }

    /**
     * 异步发布事件
     *
     * @param baseEvent 基础事件
     */
    public void asyncPublish(BaseEvent baseEvent) throws InterruptedException {

        threadPoolTaskExecutor.execute(()-> {
            applicationContext.publishEvent(baseEvent);
        });

    }
}
