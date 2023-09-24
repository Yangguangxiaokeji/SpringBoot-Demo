package com.foogui.bootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;


/**
 * rabbitmq生产者应答机制：消息发送到broker后执行的回调
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Configuration
@Slf4j
public class RabbitmqConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(this.jackson2JsonMessageConverter());
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        /**
         * ConfirmCallback触发条件：
         * 1.消息推送到server,但是在server里找不到交换机
         * 2.消息推送到server,找到交换机了，但是没找到队列
         * 3.消息推送成功（只触发该回调接口）
         **/
        // 总结生产者只要发送消息，ConfirmCallback回调必触发
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // correlationData如果在发送消息convertAndSend不传，这里就为null
            String id=null;
            String jsonMsg=null;
            if (correlationData != null) {
                id = correlationData.getId();
                if (correlationData.getReturned() !=null){
                    jsonMsg=new String(correlationData.getReturned().getMessage().getBody(),StandardCharsets.UTF_8);
                }
            }
            System.out.println("ConfirmCallback:     " + "相关消息元数据：" + correlationData);
            System.out.println("ConfirmCallback:     " + "消息id：" + id);
            System.out.println("ConfirmCallback:     " + "确认情况false or true：" + ack);
            System.out.println("ConfirmCallback:     " + "原因：" + cause);
            // 如果消息没有正常发送到queue中
            if (ack) {
                log.info("RabbitMq.message.send.success.id: {}", id);
            } else {
                // TODO:直接补发，或者保存到数据库开始定时任务补发
                log.error("RabbitMq.message.send.failed.id: {}", id);
                // 获取消息体，但必须在传递消息的时候，将消息放在CorrelationData.returnedMessage中
                log.info("sent message is {}",jsonMsg);
                log.error("RabbitMq.message.send.failed.cause: {}", cause);
            }
        });


        /**
         * ReturnsCallback触发条件
         * 1.消息推送到server,没找到交换机，或者没找到队列
         **/
        rabbitTemplate.setReturnsCallback(returned -> {

            try {
                // 获取到发送失败的消息的消息体
                String messageJsonBody = new String(returned.getMessage().getBody(), StandardCharsets.UTF_8);
                System.out.println("ReturnCallback:     " + "消息json格式为：" + messageJsonBody);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println("ReturnCallback:     " + "回应码：" + returned.getReplyCode());
            System.out.println("ReturnCallback:     " + "回应信息：" + returned.getReplyText());
            System.out.println("ReturnCallback:     " + "交换机：" + returned.getExchange());
            System.out.println("ReturnCallback:     " + "路由键：" + returned.getRoutingKey());

        });


        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}