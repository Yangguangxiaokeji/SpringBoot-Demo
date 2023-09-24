package com.foogui.bootrabbitmq.producer;

import com.foogui.bootrabbitmq.boot.MyMessagePostProcessor;
import com.foogui.bootrabbitmq.config.DirectConfig;
import com.foogui.bootrabbitmq.config.FanoutConfig;
import com.foogui.bootrabbitmq.config.TopicConfig;
import com.foogui.bootrabbitmq.dto.Msg;
import com.foogui.bootrabbitmq.utils.RabbitHelper;
import com.foogui.common.model.Result;
import com.foogui.common.utils.JsonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * 消息生产者
 *
 * @author Foogui
 * @date 2023/07/07
 */
@RestController
public class ProduceController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitHelper rabbitHelper;

    @Autowired
    private MyMessagePostProcessor myMessagePostProcessor;

    @GetMapping("/sendDirectMessage")
    public Result<?> sendDirectMessage() {
        String msg = buildMsg();
        rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.DIRECT_ROUTING_KEY, msg);
        return Result.success();
    }


    @GetMapping("/sendTopicMessage")
    public Result<?> sendTopicMessage() {
        String msg = buildMsg();
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, TopicConfig.ROUTING_KEY_FIRST, msg);
        return Result.success();
    }

    @GetMapping("/sendFanoutMessage")
    public Result<?>  sendFanoutMessage() {
        String msg = buildMsg();
        // 对于fanout交换机，routingKey没有任何意义，因为不论是否设置了routingKey，交换机都会将消息推送到绑定的队列上
        rabbitTemplate.convertAndSend(FanoutConfig.FANOUT_EXCHANGE, "", msg);
        return Result.success();
    }

    @GetMapping("/testCallback")
    public Result<?> testCallback() {
        String msg = buildMsg();

        // 新增消息关联类，通常携带消息的元数据标识消息
        CorrelationData correlationData = new CorrelationData();

        Msg reMsg = JsonUtils.toObject(msg, Msg.class);
        correlationData.setId(reMsg.getMessageId());

        Message message = new Message(msg.getBytes(StandardCharsets.UTF_8));
        correlationData.setReturned(new ReturnedMessage(message,200,"确定生产者已经发出了消息",DirectConfig.LONELY_DIRECT_EXCHANGE,"anyKey"));

        // 很明显会找不到绑定any的队列，会触发ReturnsCallback回调
        rabbitTemplate.convertAndSend(DirectConfig.LONELY_DIRECT_EXCHANGE, "anyKey", msg, correlationData);
        return Result.success();
    }

    @GetMapping("/testMessagePostProcessor")
    public Result<?> testMessagePostProcessor() {
        String msg = buildMsg();
        rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.DIRECT_ROUTING_KEY, msg, myMessagePostProcessor);
        return Result.success();
    }




    private String buildMsg() {
        Msg msg = Msg.builder()
                .messageId(UUID.randomUUID().toString())
                .messageData("消息测试数据")
                .createTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return JsonUtils.toJson(msg);
    }
}
