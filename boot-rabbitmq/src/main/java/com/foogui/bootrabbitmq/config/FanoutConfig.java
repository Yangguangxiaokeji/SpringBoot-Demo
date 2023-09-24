package com.foogui.bootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitmq配置，发布订阅模式
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Configuration
public class FanoutConfig {

    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String FANOUT_A = "fanout.A";
    public static final String FANOUT_B = "fanout.B";

    @Bean
    public Queue queueA() {
        return new Queue(FANOUT_A);
    }

    @Bean
    public Queue queueB() {
        return new Queue(FANOUT_B);
    }


    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE,true,false);
    }

    //路由键无需配置,配置也不起作用
    @Bean
    Binding bindingQueueA() {
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }

    //路由键无需配置,配置也不起作用
    @Bean
    Binding bindingQueueB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

}
