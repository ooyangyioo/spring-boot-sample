package org.yangyi.project.conf;

import org.yangyi.project.util.RabbitConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {

    private static final Logger logger = LoggerFactory.getLogger(DirectConfig.class);

    @Bean
    public Queue directQueue() {
        return new Queue(RabbitConstant.DIRECT_QUEUE);
    }

    @Bean
    //  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         RabbitTemplate.ConfirmCallback confirmCallback,
                                         RabbitTemplate.ReturnCallback returnCallback) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setConfirmCallback(confirmCallback);
        template.setReturnCallback(returnCallback);
        template.setMandatory(true);
        return template;
    }

    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                logger.info(" 回调id:" + correlationData);
                if (ack) {
                    logger.info("消息成功消费");
                } else {
                    logger.info("消息消费失败:" + cause);
                }
            }
        };
    }

    @Bean
    public RabbitTemplate.ReturnCallback returnCallback() {
        return new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                logger.info("returnedMessage" + routingKey);
            }
        };
    }

}
