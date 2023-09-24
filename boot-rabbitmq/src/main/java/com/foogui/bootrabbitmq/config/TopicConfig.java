package com.foogui.bootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq配置，主题模式（类似路由路径正则）
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Configuration
public class TopicConfig {

    public static final String TOPIC_FIRST_QUEUE = "topicFirstQueue";

    public static final String TOPIC_SECOND_QUEUE = "topicSecondQueue";

    public static final String TOPIC_EXCHANGE = "topicExchange";

    public static final String ROUTING_KEY_FIRST = "topic.first";

    public static final String ROUTING_KEY_ANY = "topic.#";


    @Bean
    public Queue firstQueue() {
        return new Queue(TOPIC_SECOND_QUEUE, true);
    }

    @Bean
    public Queue secondQueue() {
        return new Queue(TOPIC_FIRST_QUEUE, true);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    @Bean
    Binding bindingFirstQueue() {
        return BindingBuilder.bind(firstQueue()).to(topicExchange()).with(ROUTING_KEY_FIRST);
    }

    @Bean
    Binding bindingSecondQueue() {
        return BindingBuilder.bind(secondQueue()).to(topicExchange()).with(ROUTING_KEY_ANY);
    }


}
