package com.foogui.bootrabbitmq.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Component
public class RabbitHelper {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String exchange, String jsonMsg) {
        this.send(exchange, "", jsonMsg);
    }

    public void send(String exchange, String routingKey, String jsonMsg) {
        String messageId = UUID.randomUUID().toString();
        MessageProperties messageProperties = MessagePropertiesBuilder.newInstance()
                .setMessageId(messageId)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.send(exchange, routingKey, jsonMsg, messageProperties);
    }

    public void send(String exchange, String routingKey, String jsonMsg, MessageProperties messageProperties) {

        if (Objects.isNull(messageProperties)) {
            throw new IllegalStateException("messageProperties can not be null");
        }
        // 如果未传id就默认uuid
        if (StringUtils.isBlank(messageProperties.getMessageId())) {
            messageProperties.setMessageId(UUID.randomUUID().toString());
        }

        Message message = MessageBuilder
                .withBody(jsonMsg.getBytes(StandardCharsets.UTF_8))
                .andProperties(messageProperties)
                .build();
        // 新增消息关联类，通常携带消息的元数据标识消息
        CorrelationData correlationData = new CorrelationData();

        correlationData.setReturned(new ReturnedMessage(message, 200, "确定生产者已经发出了消息", exchange, routingKey));

        this.rabbitTemplate.send(exchange, routingKey, message, new CorrelationData(messageProperties.getMessageId()));
    }

}
