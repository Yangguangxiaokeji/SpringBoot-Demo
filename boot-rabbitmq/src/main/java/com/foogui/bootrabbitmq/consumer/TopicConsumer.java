package com.foogui.bootrabbitmq.consumer;

import com.foogui.bootrabbitmq.config.DirectConfig;
import com.foogui.bootrabbitmq.config.TopicConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 主题消费
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Component
@RabbitListener(queues = TopicConfig.TOPIC_FIRST_QUEUE)//监听的队列名称 topicFirstQueue
public class TopicConsumer {

    @RabbitHandler
    public void process(Map map) {
        System.out.println("TopicReceiver消费者收到消息  : " + map.toString());
    }

}
