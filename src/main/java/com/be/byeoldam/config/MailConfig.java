package com.be.byeoldam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;


// 원래는 Spring이 application.properties를 통해 MailSender를 만들어주지만, secret.properties에 넣은 값 참조를 못해 직접 만듬.
@Configuration
@PropertySource("classpath:secret.properties")
public class MailConfig {
    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.port}")
    private int mailPort;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${mail.properties.mail.smtp.auth}")
    private boolean mailAuth;

    @Value("${mail.properties.mail.smtp.ssl.enable}")
    private boolean mailSslEnable;

    @Value("${mail.properties.mail.smtp.ssl.trust}")
    private String mailSslTrust;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailAuth);
        props.put("mail.smtp.ssl.enable", mailSslEnable);
        props.put("mail.smtp.ssl.trust", mailSslTrust);

        return mailSender;
    }
}
