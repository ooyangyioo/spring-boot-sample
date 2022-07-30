package org.yangyi.project.sender.direct;

import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.util.RabbitConstant;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DirectSender {

    @Autowired
    private RabbitTemplate customRabbitTemplate;

    public void send() {
        MessageBean messageBean = new MessageBean();
        messageBean.setHeader("message header");
        messageBean.setBody("message body");
        messageBean.setTail("message tail");
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        customRabbitTemplate.convertAndSend(RabbitConstant.DIRECT_QUEUE, messageBean, correlationData);
    }

}
