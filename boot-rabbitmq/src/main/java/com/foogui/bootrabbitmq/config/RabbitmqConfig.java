package com.foogui.bootrabbitmq.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.Map;


/**
 * rabbitmq生产者应答机制：消息发送到broker后执行的回调
 *
 * @author Foogui
 * @date 2023/07/07
 */
@Configuration
public class RabbitmqConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        /**
         * ConfirmCallback触发条件：
         * 1.消息推送到server,但是在server里找不到交换机
         * 2.消息推送到server,找到交换机了，但是没找到队列
         * 3.消息推送到sever,交换机和队列啥都没找到
         * 4.消息推送成功（只触发该回调接口）
         **/
        // 总结生产者只要发送消息，ConfirmCallback回调必触发
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // correlationData如果在发送消息convertAndSend不传，这里就为null
            String id = null;
            if (correlationData != null) {
                id = correlationData.getId();
            }
            System.out.println("ConfirmCallback:     " + "相关消息元数据：" + correlationData);
            System.out.println("ConfirmCallback:     " + "消息id：" + id);
            System.out.println("ConfirmCallback:     " + "确认情况false or true：" + ack);
            System.out.println("ConfirmCallback:     " + "原因：" + cause);
            // 如果消息没有正常发送到queue中
            if (!ack) {
                // 直接补发，或者保存到数据库开始定时任务补发
            }
        });


        /**
         * ReturnsCallback触发条件
         * 1.消息推送到server,找到交换机了，但是没找到队列
         **/
        rabbitTemplate.setReturnsCallback(returned -> {

            try {
                // 获取到发送失败的消息的消息体
                byte[] body = returned.getMessage().getBody();
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
                Object obj = ois.readObject();
                ois.close();
                System.out.println("ReturnCallback:     " + "消息：" + obj);

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

}