package org.yangyi.project.receiver.direct;

import com.rabbitmq.client.Channel;
import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.sender.direct.DirectSender;
import org.yangyi.project.util.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DirectReceiver {

    private static final Logger logger = LoggerFactory.getLogger(DirectSender.class);

//    @RabbitListener(queues = RabbitConstant.DIRECT_QUEUE)
//    @RabbitHandler
//    public void receive(MessageBean messageBean) {
//        System.out.println("Header: " + messageBean.getHeader());
//        System.out.println("Body: " + messageBean.getBody());
//        System.out.println("Tail: " + messageBean.getTail());
//    }

    @RabbitListener(queues = RabbitConstant.DIRECT_QUEUE)
    @RabbitHandler
    public void receive(MessageBean messageBean, Channel channel, Message message) {
        System.out.println("Header: " + messageBean.getHeader());
        System.out.println("Body: " + messageBean.getBody());
        System.out.println("Tail: " + messageBean.getTail());

        try {
            // 告诉服务器收到这条消息 已经被我消费了 可以在队列删掉 这样以后就不会再发了 否则消息服务器以为这条消息没处理掉 后续还会在发
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            logger.info("Receiver Success");
        } catch (IOException e) {
            e.printStackTrace();
            // 丢弃这条消息
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            logger.info("Receiver Fail");
        }

    }

}
