package org.yangyi.project.sender.fanout;

import org.yangyi.project.bean.MessageBean;
import org.yangyi.project.util.RabbitConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Fanout Exchange形式又叫广播形式。
 * 因此我们发送到路由器的消息会使得绑定到该路由器的每一个Queue接收到消息。
 * 这个时候就算指定了Key,或者规则(即上文中convertAndSend方法的参数2),也会被忽略
 */

@Component
public class FanoutSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        MessageBean messageBean = new MessageBean();
        messageBean.setHeader("fanout header");
        messageBean.setBody("fanout body");
        messageBean.setTail("fanout tail");
        rabbitTemplate.convertAndSend(RabbitConstant.FANOUT_EXCHANGE, RabbitConstant.FANOUT_QUEUE_A, messageBean);
    }
}
