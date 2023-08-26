package com.foogui.bootrabbitmq.ack;


import com.foogui.bootrabbitmq.config.DirectConfig;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 消费者：消息手动确认配置
 *
 * @author Foogui
 * @date 2023/07/11
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private AckListener ackListener;//消息接收处理类

    /**
     * 简单消息监听器容器：
     * 在 AMQP 消息通道上注册 ChannelAwareMessageListener 并启动一个线程来监听消息通道。
     * 当消息到达时，SimpleMessageListenerContainer 会自动调用相应的 ChannelAwareMessageListener 来处理消息
     * @param connectionFactory 连接工厂
     * @return {@link SimpleMessageListenerContainer}
     */
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(CachingConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // RabbitMQ默认是自动确认，这里改为手动确认消息
        // 这里也可以通过yaml中进行配置
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置一个队列
        container.setQueueNames(DirectConfig.DIRECT_QUEUE);
        //如果同时设置多个如下： 前提是队列都是必须已经创建存在的
        // 如果多个队列都需要手动ack的话，就放开如下一行代码，实现一个监听器监听多个队列
        //  container.setQueueNames("TestDirectQueue","TestDirectQueue2","TestDirectQueue3");


        //另一种设置队列的方法,如果使用这种情况,那么要设置多个,就使用addQueues
        //container.setQueues(new Queue("TestDirectQueue",true));
        //container.addQueues(new Queue("TestDirectQueue2",true));
        //container.addQueues(new Queue("TestDirectQueue3",true));
        container.setMessageListener(ackListener);


        return container;
    }


}
