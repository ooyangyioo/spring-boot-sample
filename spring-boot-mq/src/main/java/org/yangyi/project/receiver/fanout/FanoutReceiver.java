package org.yangyi.project.receiver.fanout;

import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.util.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiver {

    private static final Logger logger = LoggerFactory.getLogger(FanoutReceiver.class);

    @RabbitListener(queues = RabbitConstant.FANOUT_QUEUE_A)
    @RabbitHandler
    public void processA(MessageBean messageBean) {
        logger.info("FanoutReceiverA : " + messageBean.getBody());
    }

    @RabbitListener(queues = RabbitConstant.FANOUT_QUEUE_B)
    @RabbitHandler
    public void processB(MessageBean messageBean) {
        logger.info("FanoutReceiverB : " + messageBean.getBody());
    }

    @RabbitListener(queues = RabbitConstant.FANOUT_QUEUE_C)
    @RabbitHandler
    public void processC(MessageBean messageBean) {
        logger.info("FanoutReceiverC : " + messageBean.getBody());
    }

}
