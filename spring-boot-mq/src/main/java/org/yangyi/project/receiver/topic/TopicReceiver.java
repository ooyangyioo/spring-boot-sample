package org.yangyi.project.receiver.topic;

import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.util.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiver {

    private static final Logger logger = LoggerFactory.getLogger(TopicReceiver.class);

    @RabbitListener(queues = RabbitConstant.TOPIC_QUEUE)
    @RabbitHandler
    public void topicQueue(MessageBean messageBean) {
        logger.info(messageBean.getHeader() + messageBean.getBody() + messageBean.getTail());
    }

    @RabbitListener(queues = RabbitConstant.TOPIC_QUEUES)
    @RabbitHandler
    public void topicQueues(MessageBean messageBean) {
        logger.info(messageBean.getHeader() + messageBean.getBody() + messageBean.getTail());
    }

}
