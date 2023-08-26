package com.foogui.bootrabbitmq.ack;

import com.foogui.bootrabbitmq.dto.Msg;
import com.foogui.bootrabbitmq.producer.ProduceController;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * 消费者：消息监听器，用于消费端的消费逻辑
 *
 * @author Foogui
 * @date 2023/07/11
 */
@Component
@Slf4j
public class AckListener implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            byte[] body = message.getBody();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(body));
            Msg msg = (Msg) ois.readObject();
            ois.close();
            System.out.println("  AckListener  messageId:"+msg.getMessageId()+"  messageData:"+msg.getMessageData()+"  createTime:"+msg.getCreateTime());
            System.out.println("  消费的消息来自队列名为："+message.getMessageProperties().getConsumerQueue());
            System.out.println(" 经过消息后置处理，获取到头信息myHeader的值为："+message.getMessageProperties().getHeader("myHeader"));
            //第二个参数，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息,可能造成消息丢失，建议选false
            channel.basicAck(deliveryTag, false);
//			channel.basicReject(deliveryTag, true);
            // 这里可以利用if判断队列名来执行不同队列的消费逻辑，实现一个监听器执行不同队列的消费逻辑

        } catch (Exception e) {
            //第二个参数，true会重新放回队列，如果始终消费不掉，容器造成消息积压
            channel.basicReject(deliveryTag, true);
            log.error("Exception is {}",e);
        }
    }

}
