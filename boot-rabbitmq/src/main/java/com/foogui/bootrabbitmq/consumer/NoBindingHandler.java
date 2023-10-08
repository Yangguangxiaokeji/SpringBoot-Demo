package com.foogui.bootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class NoBindingHandler {

    public static final String FANOUT_NO_BINDINGS_QUEUE = "com.foogui.no.binding.handler.queue";

    public static final String FANOUT_NO_BINDINGS_EXCHANGE = "com.foogui.no.binding.handler.exchange";

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(
                            value = FANOUT_NO_BINDINGS_QUEUE,
                            durable = "true"
                    ),
                    exchange = @Exchange(
                            name = FANOUT_NO_BINDINGS_EXCHANGE,
                            type = ExchangeTypes.FANOUT,
                            ignoreDeclarationExceptions = "true"
                    ),
                    key = ""
            )
    )
    @RabbitHandler
    public void consumeMessage(Message message, Channel channel) throws IOException {

        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("NoBindingHandler consumes message's id: {}, json body: {}", message.getMessageProperties().getMessageId(), messageBody);

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        channel.basicAck(deliveryTag, false);
    }
}
