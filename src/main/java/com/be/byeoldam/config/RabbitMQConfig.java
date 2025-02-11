package com.be.byeoldam.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange("notification.exchange");
    }

    @Bean
    public Queue bookmarkRemindQueue() {
        return new Queue("bookmark.remind.queue");
    }

    @Bean
    public Queue inviteNotificationQueue() {
        return new Queue("invite.notification.queue");
    }

    @Bean
    public Binding bookmarkRemindBinding(DirectExchange exchange, Queue bookmarkRemindQueue) {
        return BindingBuilder.bind(bookmarkRemindQueue).to(exchange).with("bookmark.remind");
    }

    @Bean
    public Binding inviteNotificationBinding(DirectExchange exchange, Queue inviteNotificationQueue) {
        return BindingBuilder.bind(inviteNotificationQueue).to(exchange).with("invite.notification");
    }
}
