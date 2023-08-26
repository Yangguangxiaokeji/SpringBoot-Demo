package com.foogui.bootrabbitmq.producer;

import com.foogui.bootrabbitmq.boot.MyMessagePostProcessor;
import com.foogui.bootrabbitmq.config.DirectConfig;
import com.foogui.bootrabbitmq.config.FanoutConfig;
import com.foogui.bootrabbitmq.config.TopicConfig;
import com.foogui.bootrabbitmq.dto.Msg;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 消息生产者
 *
 * @author Foogui
 * @date 2023/07/07
 */
@RestController
@RequiredArgsConstructor
public class ProduceController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 标记为final并使用@NonNull注解
    private final MyMessagePostProcessor myMessagePostProcessor;

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        // Map<String, Object> map = createMessageMap();
        Msg msg = buildMsg();
        rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.DIRECT_ROUTING_KEY, msg);
        return "ok";
    }



    @GetMapping("/sendTopicMessage")
    public String sendTopicMessage() {
        Map<String, Object> map = createMessageMap();
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE, TopicConfig.ROUTING_KEY_FIRST, map);
        return "ok";
    }

    @GetMapping("/sendFanoutMessage")
    public String sendFanoutMessage() {
        Map<String, Object> map = createMessageMap();
        rabbitTemplate.convertAndSend(FanoutConfig.FANOUT_EXCHANGE, null, map);
        return "ok";
    }

    @GetMapping("/testCallback")
    public String testCallback() {
        Map<String, Object> map = createMessageMap();
        // 新增消息关联类，通常携带消息的元数据标识消息
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());

        // 很明显会找不到绑定any的队列，会触发ReturnsCallback回调
        rabbitTemplate.convertAndSend(DirectConfig.LONELY_DIRECT_EXCHANGE, "any", map, correlationData);
        return "ok";
    }

    @GetMapping("/testMessagePostProcessor")
    public String testMessagePostProcessor() {
        Msg msg = buildMsg();
        rabbitTemplate.convertAndSend(DirectConfig.DIRECT_EXCHANGE, DirectConfig.DIRECT_ROUTING_KEY, msg,myMessagePostProcessor);
        return "ok";
    }

    private Map<String, Object> createMessageMap() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageBody = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageBody", messageBody);
        map.put("createTime", createTime);
        return map;
    }

    private Msg buildMsg() {
        Msg msg = Msg.builder()
                .messageId(UUID.randomUUID().toString())
                .messageData("消息数据")
                .createTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        return msg;
    }
}
