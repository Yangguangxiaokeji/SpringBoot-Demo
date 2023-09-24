package com.foogui.bootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq配置，直连模式
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Configuration
public class DirectConfig {

    public static final String DIRECT_QUEUE="directQueue";

    public static final String DIRECT_EXCHANGE="directExchange";

    public static final String DIRECT_ROUTING_KEY="directRoutingKey";
    public static final String LONELY_DIRECT_EXCHANGE = "lonelyDirectExchange";


    @Bean
    public Queue directQueue() {
        // durable:队列是否持久化,默认是false
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者连接此队列，该队列会自动删除。

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(DIRECT_QUEUE,true);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE,true,false);
    }


    /**
     * 绑定exchange和queue
     *
     * @return {@link Binding}
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(DIRECT_ROUTING_KEY);
    }



    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange(LONELY_DIRECT_EXCHANGE);
    }



}
