package org.yangyi.project.conf;

import org.yangyi.project.util.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    @Bean
    public Queue topicQueue() {
        return new Queue(RabbitConstant.TOPIC_QUEUE);
    }

    @Bean
    public Queue topicQueues() {
        return new Queue(RabbitConstant.TOPIC_QUEUES);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(RabbitConstant.TOPIC_EXCHANGE);
    }

    /**
     * 只匹配"topic.queue"队列
     *
     * @param topicQueue
     * @param topicExchange
     * @return
     */
    @Bean
    Binding bindingExchangeMessage(Queue topicQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with(RabbitConstant.TOPIC_QUEUE);
    }

    /**
     * 同时匹配两个队列
     *
     * @param topicQueues
     * @param topicExchange
     * @return
     */
    @Bean
    Binding bindingExchangeMessages(Queue topicQueues, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicQueues).to(topicExchange).with("topic.#");
    }

}
