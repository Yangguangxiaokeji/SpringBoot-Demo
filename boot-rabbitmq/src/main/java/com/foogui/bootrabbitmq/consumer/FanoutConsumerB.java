package com.foogui.bootrabbitmq.consumer;

import com.foogui.bootrabbitmq.config.FanoutConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 发布订阅消费
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Component
@RabbitListener(queues = FanoutConfig.FANOUT_B)//监听的队列名称 fanout.B
public class FanoutConsumerB {

    @RabbitHandler
    public void process(Map map) {
        System.out.println("FanoutReceiverB消费者收到消息  : " + map.toString());
    }

}