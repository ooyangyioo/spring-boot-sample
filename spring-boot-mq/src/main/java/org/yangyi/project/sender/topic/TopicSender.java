package org.yangyi.project.sender.topic;

import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.util.RabbitConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send() {

        MessageBean messageBean1 = new MessageBean();
        messageBean1.setHeader("message header1");
        messageBean1.setBody("message body1");
        messageBean1.setTail("message tail1");

        MessageBean messageBean2 = new MessageBean();
        messageBean2.setHeader("message header2");
        messageBean2.setBody("message body2");
        messageBean2.setTail("message tail2");

        amqpTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.TOPIC_QUEUE, messageBean1);
        amqpTemplate.convertAndSend(RabbitConstant.TOPIC_EXCHANGE, RabbitConstant.TOPIC_QUEUES, messageBean2);
    }

}
