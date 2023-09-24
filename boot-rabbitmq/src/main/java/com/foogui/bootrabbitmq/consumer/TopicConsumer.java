package com.foogui.bootrabbitmq.consumer;

import com.foogui.bootrabbitmq.config.TopicConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * 主题消费
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Component
@RabbitListener(queues = TopicConfig.TOPIC_FIRST_QUEUE)
public class TopicConsumer {

    @RabbitHandler
    public void process(String jsonMsg) {
        System.out.println("TopicReceiver消费者收到消息  : " + jsonMsg);
    }

}
