package com.kary.moviebooking.service.Impl;

import com.kary.moviebooking.service.Interface.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

        private final JavaMailSender mailSender;

        public EmailServiceImpl(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        public void sendEmail(String to, String subject, String body) {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("yourgmail@gmail.com");

            mailSender.send(message);
        }

}
