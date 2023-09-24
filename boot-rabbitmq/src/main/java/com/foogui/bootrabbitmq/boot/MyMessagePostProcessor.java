package com.foogui.bootrabbitmq.boot;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

/**
 * 将消息发送到 AMQP 消息通道之前对消息进行后处理。
 * 它的主要作用是在消息发送之前修改消息的内容或元数据，例如添加自定义的消息头、修改消息体等等
 *
 * @author Foogui
 * @date 2023/07/12
 */
@Component
public class MyMessagePostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        // 获取消息的元数据属性
        MessageProperties messageProperties = message.getMessageProperties();
        // 设置消息头
        messageProperties.setHeader("myHeader", "myValue");

        // 修改消息体
        byte[] body = message.getBody();
        // TODO：String messageJsonBody = new String(body, StandardCharsets.UTF_8);
        // 可以对消息体进行对象转换然后进行修改
        return new Message(body, message.getMessageProperties());
    }
}
