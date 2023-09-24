package com.foogui.bootrabbitmq.consumer;

import com.foogui.bootrabbitmq.config.DirectConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 直连消费，如果存在多个消费者，则轮训消费。
 * 特别需要注意的是，这种加注解的方式只能起到监听的作用，但是不能手动ack。
 * 如果关闭自动应答，且不手动ack，那么每次启动服务，消息都会再次被消费
 * @author Foogui
 * @date 2023/07/07
 */
@Component
@RabbitListener(queues = DirectConfig.DIRECT_QUEUE)
public class DirectConsumer {

    /**
     * 生产者发送的消息是什么类型，这里就写什么类型，例如Map，String和自定义DTO等
     *
     * @param jsonMsg 味精
     */
    @RabbitHandler
    public void process(String jsonMsg) {
        System.out.println("DirectReceiver消费者收到消息  : " + jsonMsg);
    }

}
