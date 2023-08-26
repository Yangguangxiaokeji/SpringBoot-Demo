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

    // 主题模式的routingKey格式是固定的，单词与单词之间必须是点“.”
    // #表示后面可以是一个单词或者是多个单词，*表示必须是一个单词
    public static final String ROUTING_KEY_ANY = "topic.#";


    @Bean
    public Queue firstQueue() {
        return new Queue(TOPIC_SECOND_QUEUE, true);
    }

    @Bean
    public Queue secondQueue() {
        // durable:队列是否持久化,默认是false
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者连接此队列，该队列会自动删除。

        // 一般设置一下队列的持久化就好,其余两个就是默认false
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
